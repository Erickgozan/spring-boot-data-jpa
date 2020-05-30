package com.erickgozan.springboot.app.controllers;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.erickgozan.springboot.app.models.entity.Cliente;
import com.erickgozan.springboot.app.models.entity.Factura;
import com.erickgozan.springboot.app.models.entity.ItemFactura;
import com.erickgozan.springboot.app.models.entity.Producto;
import com.erickgozan.springboot.app.services.IClienteService;

@Secured("ROLE_ADMIN")
@Controller
@SessionAttributes("factura")
@RequestMapping("/factura")
public class FacturaController {
	
	private static final Logger log = LoggerFactory.getLogger(FacturaController.class);

	
	@Autowired
	private IClienteService clienteService;
	@Autowired
	private MessageSource messageSource;
	
	//Ver los detalles de la factura del cliente asociado
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model model, RedirectAttributes flash, Locale locale) {
		
		Factura factura = clienteService.fetchFacturaByIdWithClienteWithItemFacturaWithProducto(id);
		
		if(factura==null) {
			flash.addFlashAttribute("error",messageSource.getMessage("text.factura.flash.db.error",null, locale));
			return "redirect:/listar";
		}
		String tituloFactura = String.format(messageSource.getMessage("text.factura.ver.titulo", null,locale), factura.getDescripcion());
		model.addAttribute("factura",factura);
		model.addAttribute("titulo",tituloFactura);
		return "factura/ver";
	}
	
	//Muestre el formulario de la factura con el cliente asociado a ella y mapea el objeto factura al form factura 
	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable(value = "clienteId") Integer clienteId,
			Model model, RedirectAttributes flash, Locale locale) {
		
		Cliente cliente = clienteService.findById(clienteId);
		
		if(cliente==null) {
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null,locale));
			return "redirect:/listar";
		}
		
		Factura factura = new Factura();
		factura.setCliente(cliente);
		model.addAttribute("factura",factura);
		model.addAttribute("titulo",messageSource.getMessage("text.factura.form.titulo", null, locale));
		
		return "factura/form";
	}
	
	//Devuelve un objeto json con el producto relacionado a su id
	@RequestMapping(value = "/cargar-productos/{term}", produces = {"application/json"})
	public @ResponseBody List<Producto> listarProductos(@PathVariable String term){			
			return clienteService.findByNombre(term);
	}
	
	//guarda la factura en la base de datos
	@PostMapping("/form")
	public String guardar(@Valid Factura factura,BindingResult result ,
			Model model,@RequestParam(name = "item_id[]", required = false) Long[] itemIds,
			@RequestParam(name = "cantidad[]",required = false) Integer[] cantidad,
			RedirectAttributes flash, SessionStatus status, Locale locale) {
			
			if(result.hasErrors()) {
				model.addAttribute("titulo",messageSource.getMessage("text.factura.form.titulo", null, locale));
				return "factura/form";
			}
			if(itemIds==null || itemIds.length==0) {
				model.addAttribute("titulo",messageSource.getMessage("text.factura.form.titulo", null, locale));
				model.addAttribute("error",messageSource.getMessage("text.factura.ver.no.lineas", null, locale));
				return "factura/form";
			}
		
			for (int i = 0; i < itemIds.length; i++) {
				
				Producto producto = clienteService.findProductoById(itemIds[i]);
				
				ItemFactura linea = new ItemFactura();
				linea.setCantidad(cantidad[i]);
				linea.setProducto(producto);
				factura.addItemFactura(linea);
				
				log.info("ID " + itemIds[i] + " Cantidad " + cantidad[i]);
			}
			
			clienteService.saveFactura(factura);
			status.setComplete();
			
			status.setComplete();
			
			flash.addFlashAttribute("success", messageSource.getMessage("text.factura.flash.crear.success", null, locale));
		
		return "redirect:/ver/"+factura.getCliente().getId();
	}
	
	//Elimina la factura de la base de datos
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Long id, RedirectAttributes flash,Locale locale) {
		
		Factura factura = clienteService.findFacturaById(id);
		
		if(factura!=null) {
			clienteService.deleteFactura(id);
			flash.addFlashAttribute("success",messageSource.getMessage("text.factura.flash.eliminar.success", null, locale));
			return "redirect:/ver/" + factura.getCliente().getId();
		}
		flash.addFlashAttribute("error","La factura no existe en la base de datos");	
		
		return "redirect:/listar";
	}
	
}
