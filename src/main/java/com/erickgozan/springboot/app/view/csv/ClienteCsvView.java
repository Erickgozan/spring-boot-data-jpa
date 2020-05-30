package com.erickgozan.springboot.app.view.csv;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.erickgozan.springboot.app.models.entity.Cliente;


@Component("listar")
public class ClienteCsvView extends AbstractView {
	
	public ClienteCsvView() {
		setContentType("text/csv");
	}

	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setHeader("Content-Disposition", "attachment; filename=\"Clientes.csv\"");
		response.setContentType(getContentType());
		
		Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
		
		/*Este metodo sirve para almacenar la informacion y elegir su preferencia de formato csv*/
		//El primer parametro hace referencia a donde se va a almacenar la informacion en este caso se almacena en la respuesta http
		//El segundo parametro hace referencia al tipo de preferencia al formato del csv
		ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] header = { "id", "nombre", "apellido", "email", "createAt" };//Encabezado
		beanWriter.writeHeader(header);//Escribe el encabezado

		for (Cliente cliente : clientes) {
			beanWriter.write(cliente, header);
		}

		beanWriter.close();

	}

}
