package com.geotravel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "atracciones_turisticas")
public class AtraccionTuristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotBlank
    @Column(nullable = false)
    private String clasificacion;

    // Almacenamos la foto como URL o path, el archivo en sí va en el filesystem
    private String foto;

    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point geometria;

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

    public Point getGeometria() { return geometria; }
    public void setGeometria(Point geometria) { this.geometria = geometria; }
}