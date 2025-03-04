package com.facturacion.factu3.utils;

import com.facturacion.factu3.models.Factura;
import com.facturacion.factu3.models.LineaFactura;
import com.facturacion.factu3.models.Cliente;
import com.facturacion.factu3.models.Empresa;
import com.facturacion.factu3.models.EmpresaDAO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class GenerarFacturaPDF {

    public static void generarFacturaPDF(Factura factura, Cliente cliente, String formaPago, String fechaCobro, String observaciones) {
        // Obtener la carpeta de Descargas del usuario
        String carpetaDescargas = System.getProperty("user.home") + File.separator + "Downloads";
        String rutaDestino = carpetaDescargas + File.separator + "factura_" + factura.getNumeroFactura() + ".pdf";

        Document documento = new Document(PageSize.A4.rotate()); // Documento en formato horizontal
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(rutaDestino));
            documento.open();

            // Obtener la información de la empresa
            Empresa empresa = EmpresaDAO.obtenerEmpresa(); // Suponiendo que hay un método para obtener la empresa
            String nombreEmpresa = empresa != null ? empresa.getNombre() : "Nombre de la Empresa";

            // Encabezado con el nombre de la empresa
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph tituloEmpresa = new Paragraph(nombreEmpresa, fontTitulo);
            tituloEmpresa.setAlignment(Element.ALIGN_CENTER);
            documento.add(tituloEmpresa);

            // Espaciado
            documento.add(new Paragraph("\n"));

            // Información de la factura
            Font fontSubtitulo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Paragraph tituloFactura = new Paragraph("Factura Nº " + factura.getNumeroFactura(), fontSubtitulo);
            tituloFactura.setAlignment(Element.ALIGN_CENTER);
            documento.add(tituloFactura);

            documento.add(new Paragraph("\nFecha: " + new SimpleDateFormat("dd/MM/yyyy").format(factura.getFecha())));
            documento.add(new Paragraph("Cliente: " + cliente.getNombreCliente()));

            // Espaciado antes de la tabla
            documento.add(new Paragraph("\n"));

            // Calcular el descuento del cliente
            double descuentoCliente = cliente.getDescuentoCliente(); // Descuento en porcentaje
            double montoDescuento = (factura.getBaseImponible() * descuentoCliente) / 100;
            double baseImponibleConDescuento = factura.getBaseImponible() - montoDescuento;

            // Calcular el IVA sobre la base imponible con descuento
            double iva = factura.getIva();
            double total = baseImponibleConDescuento + iva;

            // Tabla de líneas de factura (más ancha)
            PdfPTable tabla = new PdfPTable(7);
            tabla.setWidthPercentage(100); // Ajustar el ancho de la tabla al 100% de la página
            tabla.setSpacingBefore(10);
            tabla.setSpacingAfter(10);

            // Encabezados
            tabla.addCell("Cantidad");
            tabla.addCell("Descripción");
            tabla.addCell("Precio Unitario");
            tabla.addCell("IVA (%)");
            tabla.addCell("Total");
            tabla.addCell("Fecha Cobro");
            tabla.addCell("Observaciones");

            for (LineaFactura linea : factura.getLineasFactura()) {
                tabla.addCell(formatearCantidad(linea.getCantidad()));
                tabla.addCell(linea.getDescripcion());
                tabla.addCell(formatearEuros(linea.getPrecioUnitario()));
                tabla.addCell(formatearPorcentaje(linea.getIva()));
                tabla.addCell(formatearEuros(linea.getTotal()));
                tabla.addCell(fechaCobro != null ? fechaCobro : "No cobrado");
                tabla.addCell(observaciones != null ? observaciones : "Sin observaciones");
            }

            documento.add(tabla);

            // Totales
            documento.add(new Paragraph("\nBase Imponible: " + formatearEuros(baseImponibleConDescuento)));
            documento.add(new Paragraph("Descuento aplicado: -" + formatearEuros(montoDescuento)));
            documento.add(new Paragraph("IVA: " + formatearEuros(iva)));
            documento.add(new Paragraph("Total: " + formatearEuros(total)));

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
