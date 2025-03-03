package com.facturacion.factu3.models;

import java.util.Date;
import java.util.List;

public class Factura {
    private int id;
    private int numeroFactura;
    private Date fecha;
    private int idCliente;
    private double baseImponible;
    private double iva;
    private double total;
    private boolean cobrada;
    private int formaPago;
    private Date fechaCobro;
    private List<LineaFactura> lineasFactura;

    // Constructor vacío
    public Factura() {}

    // Constructor con parámetros
    public Factura(int numeroFactura, Date fecha, int idCliente, double baseImponible, double iva, double total, boolean cobrada, int formaPago, Date fechaCobro) {
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.baseImponible = baseImponible;
        this.iva = iva;
        this.total = total;
        this.cobrada = cobrada;
        this.formaPago = formaPago;
        this.fechaCobro = fechaCobro;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(int numeroFactura) { this.numeroFactura = numeroFactura; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public double getBaseImponible() { return baseImponible; }
    public void setBaseImponible(double baseImponible) { this.baseImponible = baseImponible; }

    public double getIva() { return iva; }
    public void setIva(double iva) { this.iva = iva; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public boolean isCobrada() { return cobrada; }
    public void setCobrada(boolean cobrada) { this.cobrada = cobrada; }

    public int getFormaPago() { return formaPago; }
    public void setFormaPago(int formaPago) { this.formaPago = formaPago; }

    public Date getFechaCobro() { return fechaCobro; }
    public void setFechaCobro(Date fechaCobro) { this.fechaCobro = fechaCobro; }

    public List<LineaFactura> getLineasFactura() { return lineasFactura; }
    public void setLineasFactura(List<LineaFactura> lineasFactura) { this.lineasFactura = lineasFactura; }
}
