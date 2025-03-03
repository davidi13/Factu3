package com.facturacion.factu3.utils;

import com.facturacion.factu3.models.Factura;
import com.facturacion.factu3.models.LineaFactura;
import com.facturacion.factu3.models.Cliente;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class GenerarFacturaPDF {

    public static void generarFacturaPDF(Factura factura, Cliente cliente) {
        // Obtener la carpeta de Descargas del usuario
        String carpetaDescargas = System.getProperty("user.home") + File.separator + "Downloads";
        String rutaDestino = carpetaDescargas + File.separator + "factura_" + factura.getNumeroFactura() + ".pdf";

        Document documento = new Document();
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(rutaDestino));
            documento.open();

            // Encabezado de la factura
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("Factura Nº " + factura.getNumeroFactura(), fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);

            documento.add(new Paragraph("\nFecha: " + new SimpleDateFormat("dd/MM/yyyy").format(factura.getFecha())));
            documento.add(new Paragraph("Cliente: " + cliente.getId() + " - " + cliente.getNombreCliente())); // Muestra ID + Nombre
            documento.add(new Paragraph("\n"));

            // Tabla de líneas de factura
            PdfPTable tabla = new PdfPTable(5);
            tabla.addCell("Cantidad");
            tabla.addCell("Descripción");
            tabla.addCell("Precio Unitario");
            tabla.addCell("IVA");
            tabla.addCell("Total");

            for (LineaFactura linea : factura.getLineasFactura()) {
                tabla.addCell(formatearCantidad(linea.getCantidad()));
                tabla.addCell(linea.getDescripcion());
                tabla.addCell(formatearEuros(linea.getPrecioUnitario()));
                tabla.addCell(formatearPorcentaje(linea.getIva()));
                tabla.addCell(formatearEuros(linea.getTotal()));
            }

            documento.add(tabla);

            // Totales
            documento.add(new Paragraph("\nBase Imponible: " + formatearEuros(factura.getBaseImponible())));
            documento.add(new Paragraph("IVA: " + formatearEuros(factura.getIva())));
            documento.add(new Paragraph("Total: " + formatearEuros(factura.getTotal())));

            documento.close();
            System.out.println("Factura generada con éxito en: " + rutaDestino);
        } catch (DocumentException | IOException e) {
            System.err.println("Error al generar el PDF: " + e.getMessage());
        }
    }

    /**
     * Formatea un número en euros con separación de miles y dos decimales.
     * Ejemplo: 89298.00 → 89.298,00 €
     */
    private static String formatearEuros(double cantidad) {
        DecimalFormat formatoEuro = new DecimalFormat("#,##0.00");
        return formatoEuro.format(cantidad) + " €";
    }

    /**
     * Formatea la cantidad sin decimales, añadiendo separadores de miles.
     * Ejemplo: 1234 → 1.234
     */
    private static String formatearCantidad(double cantidad) {
        DecimalFormat formatoCantidad = new DecimalFormat("#,##0");
        return formatoCantidad.format(cantidad);
    }

    /**
     * Formatea el porcentaje de IVA sin decimales.
     * Ejemplo: 21.00 → 21 %
     */
    private static String formatearPorcentaje(double porcentaje) {
        DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0");
        return formatoPorcentaje.format(porcentaje) + " %";
    }
}
