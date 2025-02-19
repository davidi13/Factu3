package com.facturacion.factu3.models;

import java.util.Date;
import java.util.List;

public class Factura {
    private int id;
    private String numeroFactura;
    private Date fecha;
    private Empresa empresa;
    private Cliente cliente;
    private Proveedor proveedor;
    private List<LineaFactura> lineasFactura;
    private double total;
    private double iva;
    private double totalConIva;

    public Factura(int id, String numeroFactura, Date fecha, Empresa empresa, Cliente cliente, Proveedor proveedor, List<LineaFactura> lineasFactura, double total, double iva) {
        this.id = id;
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        this.empresa = empresa;
        this.cliente = cliente;
        this.proveedor = proveedor;
        this.lineasFactura = lineasFactura;
        this.total = total;
        this.iva = iva;
        this.totalConIva = total + (total * (iva / 100));
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }

    public List<LineaFactura> getLineasFactura() { return lineasFactura; }
    public void setLineasFactura(List<LineaFactura> lineasFactura) { this.lineasFactura = lineasFactura; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public double getIva() { return iva; }
    public void setIva(double iva) { this.iva = iva; }

    public double getTotalConIva() { return totalConIva; }
}
