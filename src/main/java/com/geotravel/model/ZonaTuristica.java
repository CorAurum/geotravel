package com.geotravel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.locationtech.jts.geom.Polygon;

@Entity
@Table(name = "zonas_turisticas")
public class ZonaTuristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotNull
    @Min(1) @Max(5)
    @Column(name = "nivel_atractivo", nullable = false)
    private Integer nivelAtractivo;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(columnDefinition = "geometry(Polygon, 4326)", nullable = false)
    private Polygon geometria;

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

    public Polygon getGeometria() { return geometria; }
    public void setGeometria(Polygon geometria) { this.geometria = geometria; }
}