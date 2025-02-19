package com.facturacion.factu3.models;

public class Articulo {
    private int id;
    private String codigoArticulo;
    private String codigoBarras;
    private String descripcion;
    private int familia;
    private double coste;
    private double margenComercial;
    private double pvp;
    private int proveedor;
    private double stock;
    private String observaciones;

    public Articulo(int id, String codigoArticulo, String codigoBarras, String descripcion,
                    int familia, double coste, double margenComercial, double pvp,
                    int proveedor, double stock, String observaciones) {
        this.id = id;
        this.codigoArticulo = codigoArticulo;
        this.codigoBarras = codigoBarras;
        this.descripcion = descripcion;
        this.familia = familia;
        this.coste = coste;
        this.margenComercial = margenComercial;
        this.pvp = pvp;
        this.proveedor = proveedor;
        this.stock = stock;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigoArticulo() { return codigoArticulo; }
    public void setCodigoArticulo(String codigoArticulo) { this.codigoArticulo = codigoArticulo; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getFamilia() { return familia; }
    public void setFamilia(int familia) { this.familia = familia; }

    public double getCoste() { return coste; }
    public void setCoste(double coste) { this.coste = coste; }

    public double getMargenComercial() { return margenComercial; }
    public void setMargenComercial(double margenComercial) { this.margenComercial = margenComercial; }

    public double getPvp() { return pvp; }
    public void setPvp(double pvp) { this.pvp = pvp; }

    public int getProveedor() { return proveedor; }
    public void setProveedor(int proveedor) { this.proveedor = proveedor; }

    public double getStock() { return stock; }
    public void setStock(double stock) { this.stock = stock; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
