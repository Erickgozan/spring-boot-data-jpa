package com.erickgozan.springboot.app;

import java.util.Locale;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	//private final Logger LOG = LoggerFactory.getLogger(getClass());

	//metodo que agrega recursos estaticos desde rutas externas al proyecto
	/*
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		WebMvcConfigurer.super.addResourceHandlers(registry);
		
		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
		
		LOG.info("resourcePath "+resourcePath);
		
		registry.addResourceHandler("/uploads/**").addResourceLocations(resourcePath);
	}*/
	
	//Resulave el locale, dode se va aguardar el paramatro del lenguaje en este caso en la sesión
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(new Locale("es","ES"));
		return localeResolver;
	}
	
	//Interceptor que se encarga de modificar el lenguaje, cada que se pasa por parametro el lang por URL
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
		localeInterceptor.setParamName("lang");
		return localeInterceptor;
	}

	//registra el interceptor
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
	
	//Convierte el objecto seleccionado en el metodo setClassesToBeBound en un documento xml
	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(new Class[] {com.erickgozan.springboot.app.view.xml.ClienteList.class});
		return marshaller;
	}
	

}
