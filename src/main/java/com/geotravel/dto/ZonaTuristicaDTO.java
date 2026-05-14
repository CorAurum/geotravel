package com.geotravel.dto;

import jakarta.validation.constraints.*;

public class ZonaTuristicaDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El nivel de atractivo es obligatorio")
    @Min(value = 1, message = "El nivel mínimo es 1")
    @Max(value = 5, message = "El nivel máximo es 5")
    private Integer nivelAtractivo;

    private String observaciones;

    @NotBlank(message = "La geometría es obligatoria")
    private String geometria; // GeoJSON string

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getNivelAtractivo() { return nivelAtractivo; }
    public void setNivelAtractivo(Integer nivelAtractivo) { this.nivelAtractivo = nivelAtractivo; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getGeometria() { return geometria; }
    public void setGeometria(String geometria) { this.geometria = geometria; }
}