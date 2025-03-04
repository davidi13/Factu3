package com.facturacion.factu3.utils;

import com.itextpdf.text.pdf.PdfPCell;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExportarPDF {

    public static void generarListadoPDF(TableView<?> tablaDatos, ComboBox<String> cmbCategoria) {
        Document documento = new Document();
        try {
            // Obtener la fecha actual para el nombre del archivo
            String fechaHoy = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            // Obtener la categoría seleccionada
            String categoria = (cmbCategoria.getSelectionModel().getSelectedItem() != null)
                    ? cmbCategoria.getSelectionModel().getSelectedItem()
                    : "Datos";

            // Formatear el nombre del archivo correctamente
            String nombreArchivo = "listado_" + categoria.replaceAll(" ", "") + "_" + fechaHoy + ".pdf";

            String userHome = System.getProperty("user.home");
            String rutaDestino = userHome + "/Downloads/" + nombreArchivo;

            PdfWriter.getInstance(documento, new FileOutputStream(rutaDestino));
            documento.open();

            // Título del documento con fuente más pequeña
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Paragraph titulo = new Paragraph("Listado: " + categoria, fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            documento.add(new Paragraph("\n"));

            // Crear tabla con el mismo número de columnas que la TableView
            PdfPTable tablaPDF = new PdfPTable(tablaDatos.getColumns().size());

            // Encabezados de la tabla con fuente más pequeña
            Font fontEncabezado = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            for (TableColumn<?, ?> columna : tablaDatos.getColumns()) {
                tablaPDF.addCell(new PdfPCell(new Phrase(columna.getText(), fontEncabezado)));
            }

            // Datos de la tabla obtenidos con reflexión
            for (Object fila : tablaDatos.getItems()) {
                for (TableColumn<?, ?> columna : tablaDatos.getColumns()) {
                    String nombreColumna = columna.getText(); // Nombre del encabezado de la columna
                    Object valorCelda = obtenerValorDesdeObjeto(fila, nombreColumna);
                    String textoCelda = (valorCelda != null) ? valorCelda.toString() : "N/A";
                    tablaPDF.addCell(new PdfPCell(new Phrase(textoCelda, new Font(Font.FontFamily.HELVETICA, 9))));
                }
            }

            documento.add(tablaPDF);
            documento.close();
            System.out.println("✅ PDF generado correctamente en: " + rutaDestino);
        } catch (DocumentException | IOException e) {
            System.err.println("⚠ Error al generar el PDF: " + e.getMessage());
        }
    }

    /**
     * Obtiene el valor de un campo en el objeto usando el nombre de la columna.
     */
    private static Object obtenerValorDesdeObjeto(Object fila, String nombreColumna) {
        try {
            Field campo = fila.getClass().getDeclaredField(nombreColumna.toLowerCase()); // Asegurar coincidencia
            campo.setAccessible(true);
            return campo.get(fila);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return "N/A"; // Si no se encuentra el campo, devolver "N/A"
        }
    }
}
