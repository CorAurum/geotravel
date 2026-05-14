package com.geotravel.service;

import com.geotravel.dto.ZonaTuristicaDTO;
import com.geotravel.model.ZonaTuristica;
import com.geotravel.repository.ZonaTuristicaRepository;
import com.geotravel.util.GeoJsonUtils;
import jakarta.persistence.EntityNotFoundException;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ZonaTuristicaService {

    private final ZonaTuristicaRepository repository;

    public ZonaTuristicaService(ZonaTuristicaRepository repository) {
        this.repository = repository;
    }

    public List<ZonaTuristicaDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public ZonaTuristicaDTO findById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con id: " + id));
    }

    public ZonaTuristicaDTO create(ZonaTuristicaDTO dto) {
        Geometry geom = GeoJsonUtils.fromGeoJson(dto.getGeometria());
        validarSinSuperposicion(geom, -1L);
        ZonaTuristica zona = toEntity(dto, geom);
        return toDTO(repository.save(zona));
    }

    public ZonaTuristicaDTO update(Long id, ZonaTuristicaDTO dto) {
        ZonaTuristica zona = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con id: " + id));
        Geometry geom = GeoJsonUtils.fromGeoJson(dto.getGeometria());
        validarSinSuperposicion(geom, id);
        zona.setNombre(dto.getNombre());
        zona.setDescripcion(dto.getDescripcion());
        zona.setNivelAtractivo(dto.getNivelAtractivo());
        zona.setObservaciones(dto.getObservaciones());
        zona.setGeometria((Polygon) geom);
        return toDTO(repository.save(zona));
    }

    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Zona no encontrada con id: " + id);
        repository.deleteById(id);
    }

    public List<ZonaTuristicaDTO> findZonasConMasRecorridos() {
        return repository.findZonasConMasRecorridos().stream()
                .map(this::toDTO)
                .toList();
    }

    // Valida que la geometría no se superponga con otras zonas
    private void validarSinSuperposicion(Geometry geom, Long excludeId) {
        List<ZonaTuristica> superpuestas = repository.findZonasSuperpuestas(geom, excludeId);
        if (!superpuestas.isEmpty()) {
            throw new IllegalArgumentException(
                    "La zona se superpone con: " + superpuestas.get(0).getNombre()
            );
        }
    }

    // Entidad → DTO
    public ZonaTuristicaDTO toDTO(ZonaTuristica zona) {
        ZonaTuristicaDTO dto = new ZonaTuristicaDTO();
        dto.setId(zona.getId());
        dto.setNombre(zona.getNombre());
        dto.setDescripcion(zona.getDescripcion());
        dto.setNivelAtractivo(zona.getNivelAtractivo());
        dto.setObservaciones(zona.getObservaciones());
        dto.setGeometria(GeoJsonUtils.toGeoJson(zona.getGeometria()));
        return dto;
    }

    // DTO → Entidad
    private ZonaTuristica toEntity(ZonaTuristicaDTO dto, Geometry geom) {
        ZonaTuristica zona = new ZonaTuristica();
        zona.setNombre(dto.getNombre());
        zona.setDescripcion(dto.getDescripcion());
        zona.setNivelAtractivo(dto.getNivelAtractivo());
        zona.setObservaciones(dto.getObservaciones());
        zona.setGeometria((Polygon) geom);
        return zona;
    }
}