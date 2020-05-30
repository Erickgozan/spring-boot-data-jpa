package com.erickgozan.springboot.app.view.pdf;

import java.awt.Color;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.erickgozan.springboot.app.models.entity.Factura;
import com.erickgozan.springboot.app.models.entity.ItemFactura;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Component("factura/ver.pdf")
public class FacturaPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		// Forma alternativa de generar el mesaje con la llave del archivo
		// message.properties
		MessageSourceAccessor message = getMessageSourceAccessor();

		Factura factura = (Factura) model.get("factura");
		String tituloFactura = String.format(message.getMessage("text.factura.ver.titulo"), factura.getDescripcion());
		Paragraph titulo = new Paragraph(new Phrase(tituloFactura,FontFactory.getFont("Arial",20)));
		titulo.setSpacingAfter(20);
		titulo.setAlignment(Element.ALIGN_CENTER);
		document.add(titulo);

		PdfPCell cell = null; // Esta celda servira para colocar los efectos a todas las tablas
		// tabla datos del cliente
		PdfPTable tabla = new PdfPTable(1);
		tabla.setSpacingAfter(20);
		cell = new PdfPCell(new Phrase(message.getMessage("text.factura.ver.datos.cliente")));
		cell.setBackgroundColor(new Color(184, 218, 255));
		cell.setPadding(8);
		tabla.addCell(cell);
		tabla.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
		tabla.addCell(factura.getCliente().getEmail());

		// tabla datos de la factura
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		PdfPTable tabla2 = new PdfPTable(1);
		tabla2.setSpacingAfter(20);
		cell = new PdfPCell(new Phrase(message.getMessage("text.factura.ver.datos.factura")));
		cell.setBackgroundColor(new Color(195, 230, 203));
		cell.setPadding(8);
		tabla2.addCell(cell);
		tabla2.addCell(message.getMessage("text.cliente.factura.folio") + ": " + factura.getId());
		tabla2.addCell(message.getMessage("text.cliente.factura.descripcion") + ": " + factura.getDescripcion());
		tabla2.addCell(
				message.getMessage("text.cliente.factura.fecha") + ": " + dateFormat.format(factura.getCreateAt()));

		document.add(tabla);
		document.add(tabla2);

		// Tabla de productos
		PdfPTable tabla3 = new PdfPTable(4);// 4 Columnas
		tabla3.setWidths(new float[] { 3.5f, 1, 1, 1 });
		tabla3.addCell(message.getMessage("text.factura.form.item.nombre"));// Columna 1
		tabla3.addCell(message.getMessage("text.factura.form.item.precio"));// Columna 2
		tabla3.addCell(message.getMessage("text.factura.form.item.cantidad"));// Columna 3
		tabla3.addCell(message.getMessage("text.factura.form.item.total"));// Columna 4

		DecimalFormat numero = new DecimalFormat("$ ###,###.###");

		for (ItemFactura item : factura.getItems()) {
			tabla3.addCell(item.getProducto().getNombre());
			tabla3.addCell(numero.format(item.getProducto().getPrecio()));
			cell = new PdfPCell(new Phrase(item.getCantidad().toString()));
			cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			tabla3.addCell(cell);
			tabla3.addCell(numero.format(item.calcularImporte()));
		}
		// Capo total
		cell = new PdfPCell(new Phrase(message.getMessage("text.factura.form.item.total")));
		cell.setColspan(3);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tabla3.addCell(cell);
		tabla3.addCell(numero.format(factura.getTotal()));

		document.add(tabla3);
		// Tabla de obserbaciones
		PdfPTable tabla4 = new PdfPTable(2);
		tabla4.setWidths(new float[] { 1, 3 });
		tabla4.setSpacingBefore(20);
		cell = new PdfPCell(new Phrase(message.getMessage("text.factura.form.observacion") + ": "));
		cell.setBorderColor(new Color(23, 162, 184));
		cell.setPadding(8);
		tabla4.addCell(cell);
		if (factura.getObservacion() != null) {
			cell = new PdfPCell(new Phrase(factura.getObservacion()));
			cell.setBorderColor(new Color(23, 162, 184));
			tabla4.addCell(cell);
		} else {
			cell = new PdfPCell(new Phrase(message.getMessage("text.factura.ver.no.observaciones")));
			cell.setBorderColor(new Color(23, 162, 184));
			tabla4.addCell(cell);
		}
		document.add(tabla4);
	}

}
