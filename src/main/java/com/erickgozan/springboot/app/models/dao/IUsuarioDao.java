package com.erickgozan.springboot.app.models.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.erickgozan.springboot.app.models.entity.Usuario;
//JpaRepository implementa los metodos de CrudRepository y de PagingAndSortingRepository 
public interface IUsuarioDao extends JpaRepository<Usuario, Long>{
	
	public Usuario findByUsername(String username);
	
}
