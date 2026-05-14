package com.geotravel.controller;

import com.geotravel.dto.HistorialEstadoDTO;
import com.geotravel.dto.RecorridoTuristicoDTO;
import com.geotravel.model.enums.EstadoRecorrido;
import com.geotravel.service.RecorridoTuristicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recorridos")
@CrossOrigin(origins = "*")
public class RecorridoTuristicoController {

    private final RecorridoTuristicoService service;

    public RecorridoTuristicoController(RecorridoTuristicoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RecorridoTuristicoDTO>> findAll(
            @RequestParam(required = false) EstadoRecorrido estado) {
        if (estado != null) {
            return ResponseEntity.ok(service.findByEstado(estado));
        }
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecorridoTuristicoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<RecorridoTuristicoDTO> create(@Valid @RequestBody RecorridoTuristicoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecorridoTuristicoDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody RecorridoTuristicoDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Avanzar estado del recorrido
    @PatchMapping("/{id}/avanzar")
    public ResponseEntity<RecorridoTuristicoDTO> avanzarEstado(@PathVariable Long id) {
        return ResponseEntity.ok(service.avanzarEstado(id));
    }

    // Historial de estados
    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialEstadoDTO>> findHistorial(@PathVariable Long id) {
        return ResponseEntity.ok(service.findHistorial(id));
    }

    // Recorridos dentro de una zona
    @GetMapping("/zona/{zonaId}")
    public ResponseEntity<List<RecorridoTuristicoDTO>> findEnZona(@PathVariable Long zonaId) {
        return ResponseEntity.ok(service.findRecorridosEnZona(zonaId));
    }

    // Recorrido más cercano a coordenadas
    @GetMapping("/cercano")
    public ResponseEntity<RecorridoTuristicoDTO> findMasCercano(
            @RequestParam double lat,
            @RequestParam double lng) {
        return ResponseEntity.ok(service.findMasCercanoA(lat, lng));
    }

    // Actualizar estados estacionales manualmente
    @PostMapping("/actualizar-estacionalidad")
    public ResponseEntity<Void> actualizarEstacionalidad() {
        service.actualizarEstadosEstacionales();
        return ResponseEntity.ok().build();
    }
}