package com.facturacion.factu3.models;

import java.util.Date;

public class Rectificativa {
    private Integer numeroFactura;
    private Date fecha;
    private Integer idCliente;
    private Double baseImponible;
    private Double iva;
    private Double total;
    private String observaciones;

    // Constructor vacío
    public Rectificativa() {}

    // Constructor con parámetros
    public Rectificativa(Integer numeroFactura, Date fecha, Integer idCliente, Double baseImponible, Double iva, Double total, String observaciones) {
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.baseImponible = baseImponible;
        this.iva = iva;
        this.total = total;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public Integer getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(Integer numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Double getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(Double baseImponible) {
        this.baseImponible = baseImponible;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
