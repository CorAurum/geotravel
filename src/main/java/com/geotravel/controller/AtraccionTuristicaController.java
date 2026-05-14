package com.geotravel.controller;

import com.geotravel.dto.AtraccionTuristicaDTO;
import com.geotravel.service.AtraccionTuristicaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atracciones")
@CrossOrigin(origins = "*")
public class AtraccionTuristicaController {

    private final AtraccionTuristicaService service;

    public AtraccionTuristicaController(AtraccionTuristicaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AtraccionTuristicaDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtraccionTuristicaDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<AtraccionTuristicaDTO> create(@Valid @RequestBody AtraccionTuristicaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AtraccionTuristicaDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AtraccionTuristicaDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Atracciones dentro de una zona
    @GetMapping("/zona/{zonaId}")
    public ResponseEntity<List<AtraccionTuristicaDTO>> findEnZona(@PathVariable Long zonaId) {
        return ResponseEntity.ok(service.findAtraccionesEnZona(zonaId));
    }

    // Puntos más populares
    @GetMapping("/populares")
    public ResponseEntity<List<AtraccionTuristicaDTO>> findMasPopulares() {
        return ResponseEntity.ok(service.findMasPopulares());
    }
}