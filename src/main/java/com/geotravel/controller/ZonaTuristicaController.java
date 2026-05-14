package com.geotravel.controller;

import com.geotravel.dto.ZonaTuristicaDTO;
import com.geotravel.service.ZonaTuristicaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zonas")
@CrossOrigin(origins = "*")
public class ZonaTuristicaController {

    private final ZonaTuristicaService service;

    public ZonaTuristicaController(ZonaTuristicaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ZonaTuristicaDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZonaTuristicaDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ZonaTuristicaDTO> create(@Valid @RequestBody ZonaTuristicaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ZonaTuristicaDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ZonaTuristicaDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Consulta geográfica — zonas con más recorridos activos
    @GetMapping("/mas-recorridos")
    public ResponseEntity<List<ZonaTuristicaDTO>> zonasConMasRecorridos() {
        return ResponseEntity.ok(service.findZonasConMasRecorridos());
    }
}