package com.erickgozan.springboot.app.auth.handler;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.SessionFlashMapManager;


//Clase que sirve para mandar mensaje a la vista de que se ha iniciado sesión correctamente
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private LocaleResolver resolve;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		SessionFlashMapManager fhasMapManager = new SessionFlashMapManager();
		FlashMap flashMap = new FlashMap();
		
		Locale locale = resolve.resolveLocale(request);
		String mensaje = String.format(messageSource.getMessage("text.login.success",null, locale), authentication.getName());
		
		flashMap.put("success", mensaje);
		fhasMapManager.saveOutputFlashMap(flashMap, request, response);
		
		super.onAuthenticationSuccess(request, response, authentication);

	
	}

	
	
}
