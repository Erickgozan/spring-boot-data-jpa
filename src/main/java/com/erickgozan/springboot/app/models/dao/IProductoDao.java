package com.erickgozan.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.erickgozan.springboot.app.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long>{
	
	//Metodo 1.- se usa la anotacion query genera la consulta personalizada usando JPQL
	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> findByNombre(String term);
	//Metodo 2 .- Se crea el query usando nomenclaturas establecidas por spring framework
	public List<Producto> findByNombreLike(String term);
	
}
