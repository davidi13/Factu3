package com.facturacion.factu3.models;

public class LineaFactura {
    private int id;
    private int idFactura;
    private int idArticulo;
    private String descripcion;
    private double cantidad;
    private double precioUnitario;
    private double iva;
    private double total;

    // Constructor vacío
    public LineaFactura() {}

    // Constructor con parámetros
    public LineaFactura(int idFactura, int idArticulo, String descripcion, double cantidad, double precioUnitario, double iva, double total) {
        this.idFactura = idFactura;
        this.idArticulo = idArticulo;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.iva = iva;
        this.total = total;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }

    public int getIdArticulo() { return idArticulo; }
    public void setIdArticulo(int idArticulo) { this.idArticulo = idArticulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getIva() { return iva; }
    public void setIva(double iva) { this.iva = iva; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }


}
