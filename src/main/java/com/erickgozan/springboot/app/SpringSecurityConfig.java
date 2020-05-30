package com.erickgozan.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.erickgozan.springboot.app.auth.handler.LoginSuccessHandler;
import com.erickgozan.springboot.app.services.JpaUserDetailsService;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private LoginSuccessHandler successHandler; //Objeto que valida que el usuario inicio sesión con éxito.

	@Autowired
	private JpaUserDetailsService userDetailService;//Objeto que obtiene el usurio y sus credenciales(roles).

	// El codificador de contraseñas BCrypt es el recomendado en spring 5 y
	// sping-boot 2
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	//Define el modo de como se autentica a los usuarios
	@Autowired
	public void configure(AuthenticationManagerBuilder builder) throws Exception {
		// Objeto que guarda el BCrypt encoder para encriptar las contraseñas de los
		// usuarios
		
		PasswordEncoder encoder = passwordEncoder();
		
		
		builder.userDetailsService(userDetailService).passwordEncoder(encoder);
	}
	

	// Habilitar la protección de las URL y requerimos autenticarnos para poder acceder a cualquier parte del sitio
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Petición con autorización:
		http.authorizeRequests().
				antMatchers("/", "/css/**", "/js/**", "/img/**", "/listar**","/locale","/api/clientes/**").permitAll().// Rutas permitidas para todos los usuarios																									
				anyRequest().authenticated().// Asigna las autenticaciones
				and().formLogin().successHandler(successHandler).// Mensaje para el usuario de que inicio de sesión con éxito
				loginPage("/login").permitAll().// Muestra el formulario de login(personalizado) a todos los usuarios
				and().logout().permitAll();// Permite el cierre de sesión a todos los usuarios

	}

}

/*
 * //Configurar usuario,conraseña y roles de forma manual en la memoria
 * 
 * Codifica las contraseñas de los usuarios contenidos en el BCrypt UserBuilder
 * users =
 * User.builder().passwordEncoder(password->encoder.encode(password));//encoder:
 * :encode Crea los usuarios con su nombre,contraseña y rol en memoria y los
 * encripta builder.inMemoryAuthentication()
 * .withUser(users.username("admin").password("12345").roles("ADMIN","USER"))
 * .withUser(users.username("erickgozan").password("54321").roles("USER"));
 * 
 * Configurar las rutas y los accesos de forma programatica
 *
 * antMatchers("/ver/**").hasAnyRole("USER").//Ruta permitida para el rol USER
 * antMatchers("/uploads/**").hasAnyRole("USER").//Ruta permitida para el rol USER
 * antMatchers("/form/**").hasAnyRole("ADMIN").//Ruta permitida para el rol ADMIN
 * antMatchers("/eliminar/**").hasAnyRole("ADMIN").//Ruta permitida para el rol ADMIN
 * antMatchers("/factura/**").hasAnyRole("ADMIN").//Ruta permitida para el rol ADMIN
 * 
 *Configurar los usarios, roles y contraseña con JDBC
 *
 *@Autowired
	private DataSource dataSource;
 *
 *builder.jdbcAuthentication().
 *dataSource(dataSource).passwordEncoder(encoder)
				.usersByUsernameQuery("SELECT username,password,enabled FROM users WHERE username=?")
				.authoritiesByUsernameQuery("SELECT u.username, a.authority FROM authorities a INNER JOIN users u ON(a.user_id=u.id) WHERE u.username=?");

 *
 */