package com.erickgozan.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.erickgozan.springboot.app.models.entity.Cliente;
import com.erickgozan.springboot.app.services.IClienteService;
import com.erickgozan.springboot.app.services.IUploadFileService;
import com.erickgozan.springboot.app.util.paginator.PageRender;
import com.erickgozan.springboot.app.view.xml.ClienteList;


@Controller
@SessionAttributes("cliente") // Muestra todos los atributos del cliente en el alcace de sesión
public class ClienteController {

	private static Logger LOG = LoggerFactory.getLogger(ClienteController.class);

	@Autowired
	private IClienteService clienteService;
	@Autowired
	private IUploadFileService uploadFileService;
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("/listar-rest")
	public @ResponseBody ClienteList listarRest() {		
		return new ClienteList(clienteService.findAll());
	}

	/**
	 * Lista todos los clientes de la bd y crea la paginación.
	 * 
	 * @param page  - Obtenemos la ruta actual con el atributo page.url de la clase
	 *              PageRender
	 * @param model
	 * @return
	 */
	@GetMapping({ "/listar", "/" })
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			HttpServletRequest request, Locale locale) {
		// La interfaz Pageable establece el indice actual(número de pagina) y el número
		// de elementos por pagina.
		Pageable pageRequest = PageRequest.of(page, 5);
		// La interfaz Page recupera todos los objetos cliente paginados.
		Page<Cliente> clientesPages = clienteService.findAll(pageRequest);
		// PageRender es una clase propia capaz de crear la logica de la paginación.
		PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientesPages);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		// Forma 1.- para obtener los roles de los usuarios
		if (hasRole("ROLE_ADMIN")) {
			LOG.info("Hola ".concat(auth.getName().concat(" tienes acceso!")));
		} else {
			LOG.info("Hola ".concat(auth.getName().concat(" no tienes acceso!")));
		}

		// Forma 2.- para obtener los roles de los usuarios
		if (request.isUserInRole("ROLE_ADMIN")) {
			LOG.info("Forma usando HttpServletRequest. " + "Hola ".concat(auth.getName().concat(" tienes acceso!")));
		} else {
			LOG.info("Forma usando HttpServletRequest. " + "Hola ".concat(auth.getName().concat(" no tienes acceso!")));
		}

		// Forma 3.- para obtener los roles de los usuarios
		SecurityContextHolderAwareRequestWrapper securitycontext = new SecurityContextHolderAwareRequestWrapper(request,
				"ROLE_");
		if (securitycontext.isUserInRole("ADMIN")) {
			LOG.info("Forma usando SecurityContextHolderAwareRequestWrapper. "
					+ "Hola ".concat(auth.getName().concat(" tienes acceso!")));
		} else {
			LOG.info("Forma usando SecurityContextHolderAwareRequestWrapper. "
					+ "Hola ".concat(auth.getName().concat(" no tienes acceso!")));
		}

		model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));
		model.addAttribute("clientes", clientesPages);
		model.addAttribute("page", pageRender);

		return "listar";
	}

	/**
	 * Muestra el formulario para agregar clientes, y crea el objeto cliente para
	 * ser mapeado en el formulario.
	 * 
	 * @param model
	 * @return
	 */
	@Secured("ROLE_ADMIN") // Forma 1.- de hacer una autorización
	@RequestMapping(value = "/form")
	public String crear(Model model, Locale locale) {

		Cliente cliente = new Cliente();

		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.form.titulo.crear", null, locale));
		return "form";
	}

	/**
	 * Encuentra el cliente y lo coloca en el formulario
	 * 
	 * @param id    - @PathVariable se usa para pasar guardar el id que viene por la
	 *              url
	 * @param model - paso los valores a la vista utilizando un Map
	 * @param flash - muestra al usuario el mensaje de error
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable Integer id, Map<String, Object> model, RedirectAttributes flash, Locale locale) {

		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findById(id);

			if (cliente == null) {
				flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.id.error", null, locale));
			return "redirect:/listar";
		}
		model.put("titulo", messageSource.getMessage("text.cliente.form.titulo.editar", null, locale));
		model.put("cliente", cliente);
		return "form";
	}

	/**
	 * crea o edita el cliente, ademas de validar todos los campos del formulario.
	 * 
	 * @param cliente
	 * @param result  - Objeto que recibe los errores.
	 * @param model   - Paso valores a la vista usando Model.
	 * @param flash   - muestra al usuario el mensaje de éxito.
	 * @param sesion  - se completa el alcance de sesión.
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, @RequestParam(name = "file") MultipartFile foto,
			Model model, RedirectAttributes flash, SessionStatus sesion, Locale locale) {

		if (result.hasErrors()) {

			model.addAttribute("titulo", messageSource.getMessage("text.cliente.form.titulo", null, locale));
			return "form";
		}

		if (!foto.isEmpty()) {

			if (cliente.getId() > 0 && cliente.getFoto() != null && cliente.getFoto().length() > 0) {
				// Si la foto existe la borra
				uploadFileService.deleteFile(cliente.getFoto());
			}

			String uniqueFilename = null;
			try {
				// Guarda la foto o la actualiza
				uniqueFilename = uploadFileService.copyFile(foto);
			} catch (IOException e) {

				e.printStackTrace();
			}
			flash.addFlashAttribute("info",
					messageSource.getMessage("text.cliente.flash.foto.subir.success", null, locale) + " "
							+ uniqueFilename);
			cliente.setFoto(uniqueFilename);
		}

		/*
		 * Si el cliente es mayor a cero significa que el cliente existe por lo cual
		 * debe de mostrar el mesnaje 'Cliente deitado con éxito' de lo contrario es que
		 * el cliente apanes va ser creado.
		 */
		String mensajeFlash = (cliente.getId() > 0) ? "text.cliente.flash.editar.success"
				: "text.cliente.flash.crear.success";

		clienteService.save(cliente);
		sesion.setComplete();
		flash.addFlashAttribute("success", messageSource.getMessage(mensajeFlash, null, locale));
		return "redirect:/listar";
	}

	/**
	 * Ver la informacion del cliente
	 * 
	 * @param id
	 * @param model
	 * @param flash
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_USER')") // Forma 2 de hacer una autorización
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable Integer id, Model model, RedirectAttributes flash, Locale locale) {

		Cliente cliente = clienteService.fetchClienteByIdWithFacturas(id);

		if (cliente == null) {

			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
			return "redirect:/listar";
		}

		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.detalle.titulo", null, locale)
				+ cliente.getNombre() + " " + cliente.getApellido());
		return "ver";
	}

	/**
	 * Ver la foto del usuario sin tener que crear una clase de configuración
	 * 
	 * @param filename
	 * @return
	 */
	// Ruta de la foto
	@Secured("ROLE_USER")
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;
		try {
			recurso = uploadFileService.loadFile(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

	/**
	 * Elimina un cliente
	 * 
	 * @param id
	 * @param flash
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") int id, RedirectAttributes flash, Locale locale) {

		if (id > 0) {

			Cliente cliente = clienteService.findById(id);
			// Elimina el cliente
			clienteService.delate(id);
			// Elimina la foto
			if (uploadFileService.deleteFile(cliente.getFoto())) {
				String mensageFoto = String.format(
						messageSource.getMessage("text.cliente.flash.foto.eliminar.success", null, locale),
						cliente.getFoto());
				flash.addFlashAttribute("success",
						messageSource.getMessage("text.cliente.flash.eliminar.success", null, locale));
				flash.addFlashAttribute("info", mensageFoto);
			}
		}

		return "redirect:/listar";
	}

	// Mostrar el rol del usuario logeado
	private boolean hasRole(String role) {

		SecurityContext context = SecurityContextHolder.getContext();

		if (context == null)
			return false;

		Authentication auth = context.getAuthentication();

		if (auth == null)
			return false;

		// Toda clase que represente un rol en spring security debe de implementar la
		// interfaz GrantedAuthority
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

		// forma 1.- Nos permite obtener el nombre del o los roles
		for (GrantedAuthority authority : authorities) {
			if (role.equals(authority.getAuthority())) {
				LOG.info(
						"Hola usuario ".concat(auth.getName()).concat(" tu rol es: ".concat(authority.getAuthority())));
				return true;
			}
		}

		// Forma 2.- Forma mas siple, pero no muestra el nombre del rol
		// return authorities.contains(new SimpleGrantedAuthority(role));
		return false;
	}

}
