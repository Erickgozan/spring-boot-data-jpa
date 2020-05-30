package com.erickgozan.springboot.app.view.xlsx;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.erickgozan.springboot.app.models.entity.Factura;
import com.erickgozan.springboot.app.models.entity.ItemFactura;

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setHeader("Content-Disposition", "attachment; filename=\"Reporte_factura.xlsx\"");

		MessageSourceAccessor message = getMessageSourceAccessor();

		// Obtenemos la factura del controlador definida en el addAtribute.
		Factura factura = (Factura) model.get("factura");
		Sheet sheet = workbook.createSheet("Facura");
		Row row; // Fila
		Cell cell; // Columna

		// Estilo cabecera datos cliente
		CellStyle theadDatosCliente = workbook.createCellStyle();
		theadDatosCliente.setBorderBottom(BorderStyle.MEDIUM);
		theadDatosCliente.setBorderTop(BorderStyle.MEDIUM);
		theadDatosCliente.setBorderRight(BorderStyle.MEDIUM);
		theadDatosCliente.setBorderLeft(BorderStyle.MEDIUM);
		theadDatosCliente.setFillForegroundColor(IndexedColors.AQUA.index);
		theadDatosCliente.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		// Estilo cabecera datos factura
		CellStyle theadDatosFactura = workbook.createCellStyle();
		theadDatosFactura.setBorderBottom(BorderStyle.MEDIUM);
		theadDatosFactura.setBorderTop(BorderStyle.MEDIUM);
		theadDatosFactura.setBorderRight(BorderStyle.MEDIUM);
		theadDatosFactura.setBorderLeft(BorderStyle.MEDIUM);
		theadDatosFactura.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
		theadDatosFactura.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		// Estilo cuerpo de cliente y factura
		CellStyle tbodyDatos = workbook.createCellStyle();
		tbodyDatos.setBorderBottom(BorderStyle.THIN);
		tbodyDatos.setBorderTop(BorderStyle.THIN);
		tbodyDatos.setBorderRight(BorderStyle.THIN);
		tbodyDatos.setBorderLeft(BorderStyle.THIN);
		// Cabecera datos cliente
		row = sheet.createRow(0);// Fila 1
		cell = row.createCell(0);// Columna A
		cell.setCellValue(message.getMessage("text.factura.ver.datos.cliente"));
		cell.setCellStyle(theadDatosCliente);
		// Contenido
		row = sheet.createRow(1);// Fila 2
		cell = row.createCell(0);// Columna A
		cell.setCellValue(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
		cell.setCellStyle(tbodyDatos);
		row = sheet.createRow(2);// Fila 3
		cell = row.createCell(0);// Columna A
		cell.setCellValue(factura.getCliente().getEmail());
		cell.setCellStyle(tbodyDatos);
		// Cabecera datos factura
		row = sheet.createRow(4);// Fila 5
		cell = row.createCell(0);// Columna A
		cell.setCellValue(message.getMessage("text.factura.ver.datos.factura"));
		cell.setCellStyle(theadDatosFactura);
		// Contenido
		row = sheet.createRow(5);// Fila 6
		cell = row.createCell(0);// Columan A
		cell.setCellValue(message.getMessage("text.cliente.factura.folio") + ": " + factura.getId());
		cell.setCellStyle(tbodyDatos);
		row = sheet.createRow(6);// Fila 7
		cell = row.createCell(0);// Columna A
		cell.setCellValue(message.getMessage("text.cliente.factura.descripcion") + ": " + factura.getDescripcion());
		cell.setCellStyle(tbodyDatos);
		// Dar formato a la fecha
		DateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
		row = sheet.createRow(7);// Fila 8
		cell = row.createCell(0);// Columna A
		cell.setCellValue(
				message.getMessage("text.cliente.factura.fecha") + ": " + fecha.format(factura.getCreateAt()));
		cell.setCellStyle(tbodyDatos);

		// crear filas y columas y establecer su valor en una misma linea
		// sheet.createRow(5).createCell(0).setCellValue("Folio: " + factura.getId());

		// Estilo cabecera productos
		CellStyle theaderStyle = workbook.createCellStyle();
		theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
		theaderStyle.setBorderTop(BorderStyle.MEDIUM);
		theaderStyle.setBorderRight(BorderStyle.MEDIUM);
		theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
		theaderStyle.setFillForegroundColor(IndexedColors.GOLD.index);
		theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// Estilo cuerpo del productos
		CellStyle tbodyStyle = workbook.createCellStyle();
		tbodyStyle.setBorderBottom(BorderStyle.THIN);
		tbodyStyle.setBorderTop(BorderStyle.THIN);
		tbodyStyle.setBorderRight(BorderStyle.THIN);
		tbodyStyle.setBorderLeft(BorderStyle.THIN);

		Row header = sheet.createRow(9); // Fila 10
		header.createCell(0).setCellValue(message.getMessage("text.factura.form.item.nombre")); // Columna A
		header.createCell(1).setCellValue(message.getMessage("text.factura.form.item.precio"));// Columna B
		header.createCell(2).setCellValue(message.getMessage("text.factura.form.item.cantidad"));// Columna C
		header.createCell(3).setCellValue(message.getMessage("text.factura.form.item.total"));// Columna D
		// Establecer los estilos del encabezado de la tabla producto
		header.getCell(0).setCellStyle(theaderStyle);
		header.getCell(1).setCellStyle(theaderStyle);
		header.getCell(2).setCellStyle(theaderStyle);
		header.getCell(3).setCellStyle(theaderStyle);

		int rownum = 10;// Variable que empieza a colocar en las filas

		// Formato para el precio
		DecimalFormat precio = new DecimalFormat("$ ###,###.###");

		for (ItemFactura item : factura.getItems()) {// recorrer los productos
			row = sheet.createRow(rownum++);// Fila 11,12,13....
			cell = row.createCell(0);// Columa A
			cell.setCellValue(item.getProducto().getNombre());// Nombre del producto
			cell.setCellStyle(tbodyStyle);// Estilo del cuerpo de la tabla producto

			cell = row.createCell(1);// Columna B
			cell.setCellValue(precio.format(item.getProducto().getPrecio()));// Precio del producto
			cell.setCellStyle(tbodyStyle);// Estilo del cuerpo de la tabla producto

			cell = row.createCell(2);// Columna C
			cell.setCellValue(item.getCantidad());// Cantidad de productos
			cell.setCellStyle(tbodyStyle);// Estilo del cuerpo de la tabla producto

			cell = row.createCell(3);// Columna D
			cell.setCellValue(precio.format(item.calcularImporte()));// total de cada producto
			cell.setCellStyle(tbodyStyle);// Estilo del cuerpo de la tabla producto
		}
		// Total
		Row filaTotal = sheet.createRow(rownum);// Fila despues de terminar el for
		cell = filaTotal.createCell(2);// Columna C
		cell.setCellValue(message.getMessage("text.factura.form.item.total"));
		cell.setCellStyle(tbodyStyle);
		// Total de todos los productos
		cell = filaTotal.createCell(3);// Columna D
		cell.setCellValue(precio.format(factura.getTotal()));
		cell.setCellStyle(tbodyStyle);
		// Estilos Observacion
		CellStyle estiloObservacion = workbook.createCellStyle();
		estiloObservacion.setBorderBottom(BorderStyle.THIN);
		estiloObservacion.setBorderTop(BorderStyle.THIN);
		estiloObservacion.setBorderLeft(BorderStyle.THIN);
		estiloObservacion.setBorderRight(BorderStyle.THIN);
		estiloObservacion.setBottomBorderColor(IndexedColors.AQUA.index);
		estiloObservacion.setTopBorderColor(IndexedColors.AQUA.index);
		estiloObservacion.setLeftBorderColor(IndexedColors.AQUA.index);
		estiloObservacion.setRightBorderColor(IndexedColors.AQUA.index);
		// Encabezado observacion
		Row filaObservacion = sheet.createRow(rownum + 2);// Fila despues total
		cell = filaObservacion.createCell(0);// Columna A
		cell.setCellValue(message.getMessage("text.factura.form.observacion"));
		cell.setCellStyle(estiloObservacion);
		// Contenido observacion
		if (factura.getObservacion() != null) {
			filaObservacion = sheet.createRow(rownum + 3);// Fila despues del encabezado de obcervacion
			cell = filaObservacion.createCell(0);// Columna A
			cell.setCellValue(factura.getObservacion());
			cell.setCellStyle(estiloObservacion);
		} else {
			filaObservacion = sheet.createRow(rownum + 3);// Fila despues del encabezado de obcervacion
			cell = filaObservacion.createCell(0);// Columna A
			cell.setCellValue(message.getMessage("text.factura.ver.no.observaciones"));
			cell.setCellStyle(estiloObservacion);
		}
	}

}
