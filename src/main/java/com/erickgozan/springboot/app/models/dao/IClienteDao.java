package com.erickgozan.springboot.app.models.dao;
	
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.erickgozan.springboot.app.models.entity.Cliente;

/*Esta interfaz al heredar de CrudRepository ya cuenta con los 
 metodos necesarios para realizar el CRUD y 
con la anotacion @Repository para que se pueda inyectar*/
public interface IClienteDao extends PagingAndSortingRepository<Cliente, Integer> {

	@Query("select c from Cliente c left join fetch c.facturas f where c.id=?1")
	public Cliente fetchClienteByIdWithFacturas(Integer id);
}
