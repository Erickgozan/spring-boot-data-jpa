package com.erickgozan.springboot.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erickgozan.springboot.app.models.dao.IClienteDao;
import com.erickgozan.springboot.app.models.dao.IFacturaDao;
import com.erickgozan.springboot.app.models.dao.IProductoDao;
import com.erickgozan.springboot.app.models.entity.Cliente;
import com.erickgozan.springboot.app.models.entity.Factura;
import com.erickgozan.springboot.app.models.entity.Producto;

@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private IClienteDao clienteDao;
	@Autowired
	private IProductoDao productoDao;
	@Autowired
	private IFacturaDao facturaDao;

	// Listar clientes
	@Override
	@Transactional(readOnly = true) // Annotation que establece el metodo tendra un query de lectura de datos
	public List<Cliente> findAll() {

		return (List<Cliente>) clienteDao.findAll();

	}

	// Listar clientes con paginación
	@Override
	public Page<Cliente> findAll(Pageable pageable) {

		return clienteDao.findAll(pageable);
	}

	// Guardar clientes
	@Override
	@Transactional // Annotation que establece que el query tendar la funcion de editar o eliminar
	public void save(Cliente cliente) {

		clienteDao.save(cliente);
	}

	// Buscar cliente por id
	@Override
	@Transactional(readOnly = true) // Annotation que establece el metodo tendra un query de lectura de datos
	public Cliente findById(Integer id) {

		// Encuentra el cliente por su id, si no lo encuentra lo establece como null
		return clienteDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Cliente fetchClienteByIdWithFacturas(Integer id) {
		
		return clienteDao.fetchClienteByIdWithFacturas(id);
	}

	// Eliminar cliente
	@Override
	@Transactional // Annotation que establece que el query tendar la funcion de editar o eliminar
	public void delate(Integer id) {

		clienteDao.deleteById(id);

	}

	// Listar el nombre de producto
	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByNombre(String term) {
		return productoDao.findByNombreLike("%" + term + "%");
	}

	// guardar la factura
	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		facturaDao.save(factura);
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findProductoById(Long id) {

		return productoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Factura findFacturaById(Long id) {

		return facturaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteFactura(Long id) {

		facturaDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Factura fetchFacturaByIdWithClienteWithItemFacturaWithProducto(Long id) {
		return facturaDao.fetchByIdWithClienteWithItemFacturaWithProducto(id);
	}



}
