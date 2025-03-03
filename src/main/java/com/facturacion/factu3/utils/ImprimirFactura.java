package com.facturacion.factu3.utils;

import java.awt.print.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

public class ImprimirFactura {

    public static void imprimirPDF(String rutaArchivo) {
        try {
            File archivo = new File(rutaArchivo);
            FileInputStream fis = new FileInputStream(archivo);

            DocFlavor docFlavor = DocFlavor.INPUT_STREAM.PDF;
            Doc pdfDoc = new SimpleDoc(fis, docFlavor, null);

            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
            if (printService == null) {
                System.err.println("No se encontró una impresora predeterminada.");
                return;
            }

            DocPrintJob printJob = printService.createPrintJob();
            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
            attributes.add(new Copies(1));
            attributes.add(MediaSizeName.ISO_A4);
            attributes.add(OrientationRequested.PORTRAIT);

            printJob.print(pdfDoc, attributes);
            fis.close();

            System.out.println("Factura enviada a la impresora con éxito.");
        } catch (IOException | PrintException e) {
            System.err.println("Error al imprimir la factura: " + e.getMessage());
        }
    }
}