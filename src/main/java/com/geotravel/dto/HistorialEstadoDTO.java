package com.geotravel.dto;

import com.geotravel.model.enums.EstadoRecorrido;
import java.time.LocalDateTime;

public class HistorialEstadoDTO {

    private Long id;
    private Long recorridoId;
    private EstadoRecorrido estado;
    private LocalDateTime fechaCambio;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRecorridoId() { return recorridoId; }
    public void setRecorridoId(Long recorridoId) { this.recorridoId = recorridoId; }

    public EstadoRecorrido getEstado() { return estado; }
    public void setEstado(EstadoRecorrido estado) { this.estado = estado; }

    public LocalDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(LocalDateTime fechaCambio) { this.fechaCambio = fechaCambio; }
}