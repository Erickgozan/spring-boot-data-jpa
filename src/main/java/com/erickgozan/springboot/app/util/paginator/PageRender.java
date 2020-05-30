package com.erickgozan.springboot.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {
	// url donde se encuentra la lista
	private String url;
	// objeto Page donde se guarda la lista de objetos clientes que pasara en
	// controlador
	private Page<T> page;
	// total de paginas, numero de elementos por pagina y pagina actual obtenidas
	// usando el objeto Page
	private int totalPaginas;
	private int numElementosPorPagina;
	private int paginaActual;

	private List<PageItem> paginas;

	public PageRender(String url, Page<T> page) {

		this.url = url;
		this.page = page;// Colección de objetos usando pagiación
		this.paginas = new ArrayList<PageItem>();

		numElementosPorPagina = page.getSize();// 1
		totalPaginas = page.getTotalPages();// 24
		paginaActual = page.getNumber() + 1;

		int desde;
		int hasta;
		/*
		 * condicionales que sirven para saber en que punto de la paginacion estoy y en
		 * base a eso establecer el ancho del paginador
		 */
		if (totalPaginas <= numElementosPorPagina) {
			desde = 1;
			hasta = totalPaginas;
		} else {
			if (paginaActual <= (numElementosPorPagina / 2)) {
				desde = 1;
				hasta = numElementosPorPagina;
			} else if (paginaActual >= totalPaginas - (numElementosPorPagina / 2)) {
				desde = totalPaginas - numElementosPorPagina + 1;
				hasta = numElementosPorPagina;
			} else {
				desde = paginaActual - (numElementosPorPagina / 2);//1
				hasta = numElementosPorPagina;//1
			}
		}

		for (int i = 0; i < hasta; i++) {
			paginas.add(new PageItem(desde + i, paginaActual == desde + i));
			//1 - true, 
		}
	}

	public String getUrl() {
		return url;
	}

	public int getTotalPaginas() {
		return totalPaginas;
	}

	public int getPaginaActual() {
		return paginaActual;
	}

	public List<PageItem> getPaginas() {
		return paginas;
	}

	// Metodo para saber si es la primer pagina.
	public boolean isFirst() {
		return page.isFirst();
	}

	// Matodo para saber si es la ultima pagina.
	public boolean isLast() {
		return page.isLast();
	}

	// Metodo que retorna a la siguiente pagina.
	public boolean isHasNext() {
		return page.hasNext();
	}

	// Metodo que retorna la pagina anterior
	public boolean isHasPrevious() {
		return page.hasPrevious();
	}
}

//Clase interna que sirve para saber en que pagina estoy y si esta activada
//con el fin de poblar los items numerados en la vista 
class PageItem {

	private int numero;
	private boolean actual;

	public PageItem(int numero, boolean actual) {
		this.numero = numero;
		this.actual = actual;
	}

	public int getNumero() {
		return numero;
	}

	public boolean isActual() {
		return actual;
	}

}
