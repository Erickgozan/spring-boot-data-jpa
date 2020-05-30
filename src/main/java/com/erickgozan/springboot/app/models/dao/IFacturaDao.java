package com.erickgozan.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.erickgozan.springboot.app.models.entity.Factura;

public interface IFacturaDao extends CrudRepository<Factura, Long>{
	
	//Metodo personalizado para ejecutar una consulta join con factura,itemFactura,cliente y producto
	@Query("SELECT f FROM Factura f JOIN FETCH f.cliente c JOIN FETCH f.items i JOIN FETCH i.producto WHERE f.id=?1")
	public Factura fetchByIdWithClienteWithItemFacturaWithProducto(Long id); 
}
