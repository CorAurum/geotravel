package com.geotravel.model;

import com.geotravel.model.enums.EstadoRecorrido;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_estados")
public class HistorialEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorrido_id", nullable = false)
    private RecorridoTuristico recorrido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRecorrido estado;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio = LocalDateTime.now();

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RecorridoTuristico getRecorrido() { return recorrido; }
    public void setRecorrido(RecorridoTuristico recorrido) { this.recorrido = recorrido; }

    public EstadoRecorrido getEstado() { return estado; }
    public void setEstado(EstadoRecorrido estado) { this.estado = estado; }

    public LocalDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(LocalDateTime fechaCambio) { this.fechaCambio = fechaCambio; }
}