package com.erickgozan.springboot.app.controllers;

import java.security.Principal;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.erickgozan.springboot.app.models.entity.Usuario;
import com.erickgozan.springboot.app.services.JpaUserDetailsService;

@Controller
public class LoginController {

	@Autowired
	private JpaUserDetailsService service;
	
	@Autowired
	private MessageSource messageSource;

	@GetMapping("/login")
	public String login(@RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "error", required = false) String error, Model model, Principal principal,
			RedirectAttributes flash, Locale locale) {
		
		//Obtener el usuario de la base de datos
		Usuario usuario = service.getUsuario();

		// Si el usuario inició sesión y quiere iniciar nueva mente, redirecciona a la
		// raiz del proyecto(Pagina principal).
		if (principal != null) {
			flash.addFlashAttribute("info", messageSource.getMessage("text.login.already",null ,locale));
			return "redirect:/";
		}

		// Paramatro proporcionado, por spring security para validar el usuario y la
		// contraseña o si el usuario existe
		if (error != null) {
			if (usuario == null) {
				model.addAttribute("error",
						 messageSource.getMessage("text.login.usuario.error",null ,locale));				
			}else {
			model.addAttribute("error",
					messageSource.getMessage("text.login.error",null ,locale));
			}
		}
		// Parametro proporcionado por spring security para cerrar la sesión
		if (logout != null) {
			model.addAttribute("success", messageSource.getMessage("text.login.logout",null ,locale));
		}

		return "login";
	}

}
