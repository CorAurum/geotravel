package com.geotravel.dto;

import com.geotravel.model.enums.EstadoRecorrido;
import com.geotravel.model.enums.TipoExperiencia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Month;
import java.util.List;

public class RecorridoTuristicoDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;
    private Integer duracionEstimada;
    private String guiaResponsable;
    private TipoExperiencia tipoExperiencia;
    private EstadoRecorrido estado;

    @NotNull(message = "El mes de inicio de temporada es obligatorio")
    private Month temporadaInicio;

    @NotNull(message = "El mes de fin de temporada es obligatorio")
    private Month temporadaFin;

    private String geometria; // GeoJSON string - LineString

    // Lista ordenada de IDs de atracciones que forman el recorrido
    private List<Long> atraccionIds;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getDuracionEstimada() { return duracionEstimada; }
    public void setDuracionEstimada(Integer duracionEstimada) { this.duracionEstimada = duracionEstimada; }

    public String getGuiaResponsable() { return guiaResponsable; }
    public void setGuiaResponsable(String guiaResponsable) { this.guiaResponsable = guiaResponsable; }

    public TipoExperiencia getTipoExperiencia() { return tipoExperiencia; }
    public void setTipoExperiencia(TipoExperiencia tipoExperiencia) { this.tipoExperiencia = tipoExperiencia; }

    public EstadoRecorrido getEstado() { return estado; }
    public void setEstado(EstadoRecorrido estado) { this.estado = estado; }

    public Month getTemporadaInicio() { return temporadaInicio; }
    public void setTemporadaInicio(Month temporadaInicio) { this.temporadaInicio = temporadaInicio; }

    public Month getTemporadaFin() { return temporadaFin; }
    public void setTemporadaFin(Month temporadaFin) { this.temporadaFin = temporadaFin; }

    public String getGeometria() { return geometria; }
    public void setGeometria(String geometria) { this.geometria = geometria; }

    public List<Long> getAtraccionIds() { return atraccionIds; }
    public void setAtraccionIds(List<Long> atraccionIds) { this.atraccionIds = atraccionIds; }
}