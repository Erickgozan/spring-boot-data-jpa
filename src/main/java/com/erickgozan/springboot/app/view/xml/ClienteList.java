package com.erickgozan.springboot.app.view.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.erickgozan.springboot.app.models.entity.Cliente;

//Clase wrapper que sirve para convertir en un archivo xml
@XmlRootElement(name = "clientes")
public class ClienteList {
	
	@XmlElement(name="cliente")
	private List<Cliente> clientes;

	public ClienteList() {
	}

	public ClienteList(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

}
