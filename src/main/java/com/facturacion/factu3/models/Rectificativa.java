package com.facturacion.factu3.models;

import java.time.LocalDate;

public class Rectificativa {
    private Integer id;
    private LocalDate fecha;
    private Double baseImponible;
    private Double iva;
    private Double total;
    private String observaciones;

    public Rectificativa(Integer id, LocalDate fecha, Double baseImponible, Double iva, Double total, String observaciones) {
        this.id = id;
        this.fecha = fecha;
        this.baseImponible = baseImponible;
        this.iva = iva;
        this.total = total;
        this.observaciones = observaciones;
    }

    // Getters y Setters
}
