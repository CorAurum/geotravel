package com.geotravel.service;

import com.geotravel.dto.AtraccionTuristicaDTO;
import com.geotravel.model.AtraccionTuristica;
import com.geotravel.repository.AtraccionTuristicaRepository;
import com.geotravel.util.GeoJsonUtils;
import jakarta.persistence.EntityNotFoundException;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AtraccionTuristicaService {

    private final AtraccionTuristicaRepository repository;

    public AtraccionTuristicaService(AtraccionTuristicaRepository repository) {
        this.repository = repository;
    }

    public List<AtraccionTuristicaDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public AtraccionTuristicaDTO findById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Atracción no encontrada con id: " + id));
    }

    public AtraccionTuristicaDTO create(AtraccionTuristicaDTO dto) {
        AtraccionTuristica atraccion = toEntity(dto);
        return toDTO(repository.save(atraccion));
    }

    public AtraccionTuristicaDTO update(Long id, AtraccionTuristicaDTO dto) {
        AtraccionTuristica atraccion = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atracción no encontrada con id: " + id));
        atraccion.setNombre(dto.getNombre());
        atraccion.setDescripcion(dto.getDescripcion());
        atraccion.setClasificacion(dto.getClasificacion());
        atraccion.setFoto(dto.getFoto());
        atraccion.setGeometria((Point) GeoJsonUtils.fromGeoJson(dto.getGeometria()));
        return toDTO(repository.save(atraccion));
    }

    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Atracción no encontrada con id: " + id);
        repository.deleteById(id);
    }

    public List<AtraccionTuristicaDTO> findAtraccionesEnZona(Long zonaId) {
        return repository.findAtraccionesEnZona(zonaId).stream()
                .map(this::toDTO)
                .toList();
    }

    public List<AtraccionTuristicaDTO> findMasPopulares() {
        return repository.findMasPopulares().stream()
                .map(this::toDTO)
                .toList();
    }

    public AtraccionTuristicaDTO toDTO(AtraccionTuristica a) {
        AtraccionTuristicaDTO dto = new AtraccionTuristicaDTO();
        dto.setId(a.getId());
        dto.setNombre(a.getNombre());
        dto.setDescripcion(a.getDescripcion());
        dto.setClasificacion(a.getClasificacion());
        dto.setFoto(a.getFoto());
        dto.setGeometria(GeoJsonUtils.toGeoJson(a.getGeometria()));
        return dto;
    }

    private AtraccionTuristica toEntity(AtraccionTuristicaDTO dto) {
        AtraccionTuristica a = new AtraccionTuristica();
        a.setNombre(dto.getNombre());
        a.setDescripcion(dto.getDescripcion());
        a.setClasificacion(dto.getClasificacion());
        a.setFoto(dto.getFoto());
        a.setGeometria((Point) GeoJsonUtils.fromGeoJson(dto.getGeometria()));
        return a;
    }
}