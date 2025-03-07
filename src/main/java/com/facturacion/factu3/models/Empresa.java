package com.facturacion.factu3.models;

public class Empresa {
    private int id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String cif;
    private String logo;

    public Empresa(int id, String nombre, String direccion, String telefono, String email, String cif, String logo) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.cif = cif;
        this.logo = logo;
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

    public String getCif() { return cif; }
    public void setCif(String cif) { this.cif = cif; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    @Override
    public String toString() {
        return nombre;
    }

}
