package com.facturacion.factu3.models;

public class Distribuidor {
    private int id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String zona;
    private String observaciones;

    public Distribuidor(int id, String nombre, String direccion, String telefono, String email, String zona, String observaciones) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.zona = zona;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
