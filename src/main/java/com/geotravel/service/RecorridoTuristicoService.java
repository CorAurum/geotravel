package com.geotravel.service;

import com.geotravel.dto.HistorialEstadoDTO;
import com.geotravel.dto.RecorridoTuristicoDTO;
import com.geotravel.model.*;
import com.geotravel.model.enums.EstadoRecorrido;
import com.geotravel.repository.*;
import com.geotravel.util.GeoJsonUtils;
import jakarta.persistence.EntityNotFoundException;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RecorridoTuristicoService {

    private final RecorridoTuristicoRepository recorridoRepository;
    private final AtraccionTuristicaRepository atraccionRepository;
    private final HistorialEstadoRepository historialRepository;

    public RecorridoTuristicoService(
            RecorridoTuristicoRepository recorridoRepository,
            AtraccionTuristicaRepository atraccionRepository,
            HistorialEstadoRepository historialRepository) {
        this.recorridoRepository = recorridoRepository;
        this.atraccionRepository = atraccionRepository;
        this.historialRepository = historialRepository;
    }

    public List<RecorridoTuristicoDTO> findAll() {
        return recorridoRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public List<RecorridoTuristicoDTO> findByEstado(EstadoRecorrido estado) {
        return recorridoRepository.findByEstado(estado).stream()
                .map(this::toDTO)
                .toList();
    }

    public RecorridoTuristicoDTO findById(Long id) {
        return recorridoRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Recorrido no encontrado con id: " + id));
    }

    public RecorridoTuristicoDTO create(RecorridoTuristicoDTO dto) {
        RecorridoTuristico recorrido = new RecorridoTuristico();
        mapDtoToEntity(dto, recorrido);
        recorrido.setEstado(EstadoRecorrido.PENDIENTE);

        // Registrar estado inicial en historial
        RecorridoTuristico saved = recorridoRepository.save(recorrido);
        registrarHistorial(saved, EstadoRecorrido.PENDIENTE);

        return toDTO(saved);
    }

    public RecorridoTuristicoDTO update(Long id, RecorridoTuristicoDTO dto) {
        RecorridoTuristico recorrido = recorridoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recorrido no encontrado con id: " + id));
        mapDtoToEntity(dto, recorrido);
        return toDTO(recorridoRepository.save(recorrido));
    }

    public void delete(Long id) {
        if (!recorridoRepository.existsById(id))
            throw new EntityNotFoundException("Recorrido no encontrado con id: " + id);
        recorridoRepository.deleteById(id);
    }

    // Avanzar estado respetando secuencia lógica
    public RecorridoTuristicoDTO avanzarEstado(Long id) {
        RecorridoTuristico recorrido = recorridoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recorrido no encontrado con id: " + id));

        EstadoRecorrido siguiente = calcularSiguienteEstado(recorrido.getEstado());
        recorrido.setEstado(siguiente);
        registrarHistorial(recorrido, siguiente);

        return toDTO(recorridoRepository.save(recorrido));
    }

    // Actualizar estados estacionales — llamar periódicamente o al consultar
    public void actualizarEstadosEstacionales() {
        Month mesActual = LocalDateTime.now().getMonth();
        List<RecorridoTuristico> todos = recorridoRepository.findAll();

        for (RecorridoTuristico r : todos) {
            if (r.getEstado() == EstadoRecorrido.CANCELADO) continue;
            boolean enTemporada = estaEnTemporada(r, mesActual);

            if (!enTemporada && r.getEstado() == EstadoRecorrido.DISPONIBLE) {
                r.setEstado(EstadoRecorrido.FUERA_DE_ESTACION);
                registrarHistorial(r, EstadoRecorrido.FUERA_DE_ESTACION);
                recorridoRepository.save(r);
            } else if (enTemporada && r.getEstado() == EstadoRecorrido.FUERA_DE_ESTACION) {
                r.setEstado(EstadoRecorrido.DISPONIBLE);
                registrarHistorial(r, EstadoRecorrido.DISPONIBLE);
                recorridoRepository.save(r);
            }
        }
    }

    public List<RecorridoTuristicoDTO> findRecorridosEnZona(Long zonaId) {
        return recorridoRepository.findRecorridosEnZona(zonaId).stream()
                .map(this::toDTO)
                .toList();
    }

    public RecorridoTuristicoDTO findMasCercanoA(double lat, double lng) {
        org.locationtech.jts.geom.GeometryFactory factory =
                new org.locationtech.jts.geom.GeometryFactory(
                        new org.locationtech.jts.geom.PrecisionModel(), 4326);
        Point punto = factory.createPoint(new org.locationtech.jts.geom.Coordinate(lng, lat));
        return recorridoRepository.findMasCercanoA(punto)
                .map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("No hay recorridos registrados"));
    }

    public List<HistorialEstadoDTO> findHistorial(Long recorridoId) {
        return historialRepository.findByRecorridoIdOrderByFechaCambioDesc(recorridoId)
                .stream().map(this::toHistorialDTO).toList();
    }

    // --- Helpers privados ---

    private void mapDtoToEntity(RecorridoTuristicoDTO dto, RecorridoTuristico recorrido) {
        recorrido.setNombre(dto.getNombre());
        recorrido.setDescripcion(dto.getDescripcion());
        recorrido.setDuracionEstimada(dto.getDuracionEstimada());
        recorrido.setGuiaResponsable(dto.getGuiaResponsable());
        recorrido.setTipoExperiencia(dto.getTipoExperiencia());
        recorrido.setTemporadaInicio(dto.getTemporadaInicio());
        recorrido.setTemporadaFin(dto.getTemporadaFin());

        if (dto.getGeometria() != null) {
            recorrido.setGeometria((LineString) GeoJsonUtils.fromGeoJson(dto.getGeometria()));
        }

        // Sincronizar puntos/atracciones del recorrido
        if (dto.getAtraccionIds() != null) {
            recorrido.getPuntos().clear();
            for (int i = 0; i < dto.getAtraccionIds().size(); i++) {
                Long atraccionId = dto.getAtraccionIds().get(i);
                AtraccionTuristica atraccion = atraccionRepository.findById(atraccionId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Atracción no encontrada con id: " + atraccionId));
                RecorridoPunto punto = new RecorridoPunto();
                punto.setRecorrido(recorrido);
                punto.setAtraccion(atraccion);
                punto.setOrden(i + 1);
                recorrido.getPuntos().add(punto);
            }
        }
    }

    private EstadoRecorrido calcularSiguienteEstado(EstadoRecorrido actual) {
        return switch (actual) {
            case PENDIENTE -> EstadoRecorrido.DISPONIBLE;
            case DISPONIBLE -> EstadoRecorrido.CANCELADO;
            case FUERA_DE_ESTACION -> EstadoRecorrido.CANCELADO;
            case CANCELADO -> throw new IllegalStateException(
                    "Un recorrido cancelado no puede avanzar de estado");
        };
    }

    private boolean estaEnTemporada(RecorridoTuristico r, Month mes) {
        if (r.getTemporadaInicio() == null || r.getTemporadaFin() == null) return true;
        int inicio = r.getTemporadaInicio().getValue();
        int fin = r.getTemporadaFin().getValue();
        int actual = mes.getValue();
        // Maneja temporadas que cruzan el año (ej: NOV a FEB)
        if (inicio <= fin) {
            return actual >= inicio && actual <= fin;
        } else {
            return actual >= inicio || actual <= fin;
        }
    }

    private void registrarHistorial(RecorridoTuristico recorrido, EstadoRecorrido estado) {
        HistorialEstado h = new HistorialEstado();
        h.setRecorrido(recorrido);
        h.setEstado(estado);
        h.setFechaCambio(LocalDateTime.now());
        historialRepository.save(h);
    }

    public RecorridoTuristicoDTO toDTO(RecorridoTuristico r) {
        RecorridoTuristicoDTO dto = new RecorridoTuristicoDTO();
        dto.setId(r.getId());
        dto.setNombre(r.getNombre());
        dto.setDescripcion(r.getDescripcion());
        dto.setDuracionEstimada(r.getDuracionEstimada());
        dto.setGuiaResponsable(r.getGuiaResponsable());
        dto.setTipoExperiencia(r.getTipoExperiencia());
        dto.setEstado(r.getEstado());
        dto.setTemporadaInicio(r.getTemporadaInicio());
        dto.setTemporadaFin(r.getTemporadaFin());
        dto.setGeometria(GeoJsonUtils.toGeoJson(r.getGeometria()));
        dto.setAtraccionIds(
                r.getPuntos().stream()
                        .map(p -> p.getAtraccion().getId())
                        .toList()
        );
        return dto;
    }

    private HistorialEstadoDTO toHistorialDTO(HistorialEstado h) {
        HistorialEstadoDTO dto = new HistorialEstadoDTO();
        dto.setId(h.getId());
        dto.setRecorridoId(h.getRecorrido().getId());
        dto.setEstado(h.getEstado());
        dto.setFechaCambio(h.getFechaCambio());
        return dto;
    }
}