package com.geotravel.repository;

import com.geotravel.model.HistorialEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialEstadoRepository extends JpaRepository<HistorialEstado, Long> {

    // Historial completo de un recorrido ordenado por fecha
    List<HistorialEstado> findByRecorridoIdOrderByFechaCambioDesc(Long recorridoId);
}