package com.geotravel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "recorrido_puntos")
public class RecorridoPunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorrido_id", nullable = false)
    private RecorridoTuristico recorrido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atraccion_id", nullable = false)
    private AtraccionTuristica atraccion;

    @Column(nullable = false)
    private Integer orden;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RecorridoTuristico getRecorrido() { return recorrido; }
    public void setRecorrido(RecorridoTuristico recorrido) { this.recorrido = recorrido; }

    public AtraccionTuristica getAtraccion() { return atraccion; }
    public void setAtraccion(AtraccionTuristica atraccion) { this.atraccion = atraccion; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }
}