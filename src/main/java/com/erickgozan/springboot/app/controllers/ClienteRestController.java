package com.erickgozan.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erickgozan.springboot.app.services.IClienteService;
import com.erickgozan.springboot.app.view.xml.ClienteList;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;
	
	@GetMapping("/listar")
	public ClienteList listar() {		
		return new ClienteList(clienteService.findAll());
	}
	
}
