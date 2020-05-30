package com.erickgozan.springboot.app.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.erickgozan.springboot.app.models.entity.Cliente;
import com.erickgozan.springboot.app.models.entity.Factura;
import com.erickgozan.springboot.app.models.entity.Producto;

public interface IClienteService {
	
	public List<Cliente> findAll();
	
	public Page<Cliente> findAll(Pageable pageable);

	public void save(Cliente cliente);

	public Cliente findById(Integer id);
	
	public Cliente fetchClienteByIdWithFacturas(Integer id);

	public void delate(Integer id);
	
	public List<Producto> findByNombre(String term);
	
	public void saveFactura(Factura factura);
	
	public Producto findProductoById(Long id);
	
	public Factura findFacturaById(Long id);
	
	public void deleteFactura(Long id);
	
	public Factura fetchFacturaByIdWithClienteWithItemFacturaWithProducto(Long id);
}
