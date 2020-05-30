package com.erickgozan.springboot.app.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erickgozan.springboot.app.models.dao.IUsuarioDao;
import com.erickgozan.springboot.app.models.entity.Role;
import com.erickgozan.springboot.app.models.entity.Usuario;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IUsuarioDao usuarioDao;
	
	private Usuario usuario;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		 usuario = usuarioDao.findByUsername(username);

		// Verifica si el usuario existe en la base de datos
		if (usuario == null) {
			//logger.error("Error login: no existe el usuario: " + username);
			throw new UsernameNotFoundException("Username " + username + " no existe en el sistema");
		}

		// Agrega a una lista los roles que tiene el usuario
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		// Recorre los roles del usuario
		for (Role role : usuario.getRole()) {
			logger.info("Role: ".concat(role.getAuthority()));
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		
		/* Forma 2 de obtener los roles
		 * 
		 * List<SimpleGrantedAuthority> authorities = usuario.getRole().
				stream().
				map(rol -> new SimpleGrantedAuthority(rol.getAuthority())).collect(Collectors.toList());
		 * */
		
		
		// Verifica si el usuario no tiene roles asiganados
		if (authorities.isEmpty()) {
			logger.error("Error login: usuario " + username + " no tiene roles asignado!");
			throw new UsernameNotFoundException("Username " + username + " no tiene roles asignados");
		}

		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true,
				authorities);
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

}
