package com.geotravel.dto;

import jakarta.validation.constraints.NotBlank;

public class AtraccionTuristicaDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotBlank(message = "La clasificación es obligatoria")
    private String clasificacion;

    private String foto;

    @NotBlank(message = "La geometría es obligatoria")
    private String geometria; // GeoJSON string - Point

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getClasificacion() { return clasificacion; }
    public void setClasificacion(String clasificacion) { this.clasificacion = clasificacion; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public String getGeometria() { return geometria; }
    public void setGeometria(String geometria) { this.geometria = geometria; }
}