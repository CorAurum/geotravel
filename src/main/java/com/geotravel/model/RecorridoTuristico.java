package com.geotravel.model;

import com.geotravel.model.enums.EstadoRecorrido;
import com.geotravel.model.enums.TipoExperiencia;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.locationtech.jts.geom.LineString;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recorridos_turisticos")
public class RecorridoTuristico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // En minutos
    @Column(name = "duracion_estimada")
    private Integer duracionEstimada;

    @Column(name = "guia_responsable")
    private String guiaResponsable;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_experiencia")
    private TipoExperiencia tipoExperiencia;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRecorrido estado = EstadoRecorrido.PENDIENTE;

    // Estacionalidad — mes de inicio y fin (ej: MARCH a DECEMBER)
    @Enumerated(EnumType.STRING)
    @Column(name = "temporada_inicio")
    private Month temporadaInicio;

    @Enumerated(EnumType.STRING)
    @Column(name = "temporada_fin")
    private Month temporadaFin;

    @Column(columnDefinition = "geometry(LineString, 4326)")
    private LineString geometria;

    @OneToMany(mappedBy = "recorrido", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC")
    private List<RecorridoPunto> puntos = new ArrayList<>();

    @OneToMany(mappedBy = "recorrido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialEstado> historial = new ArrayList<>();

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

    public LineString getGeometria() { return geometria; }
    public void setGeometria(LineString geometria) { this.geometria = geometria; }

    public List<RecorridoPunto> getPuntos() { return puntos; }
    public void setPuntos(List<RecorridoPunto> puntos) { this.puntos = puntos; }

    public List<HistorialEstado> getHistorial() { return historial; }
    public void setHistorial(List<HistorialEstado> historial) { this.historial = historial; }
}