package com.geotravel.repository;

import com.geotravel.model.AtraccionTuristica;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtraccionTuristicaRepository extends JpaRepository<AtraccionTuristica, Long> {

    List<AtraccionTuristica> findByClasificacion(String clasificacion);

    // Atracciones dentro de una zona
    @Query(value = """
        SELECT a.* FROM atracciones_turisticas a, zonas_turisticas z
        WHERE z.id = :zonaId
        AND ST_Within(a.geometria, z.geometria)
        """, nativeQuery = true)
    List<AtraccionTuristica> findAtraccionesEnZona(@Param("zonaId") Long zonaId);

    // Puntos más populares (incluidos en más recorridos)
    @Query(value = """
        SELECT a.*, COUNT(rp.recorrido_id) as total_recorridos
        FROM atracciones_turisticas a
        LEFT JOIN recorrido_puntos rp ON rp.atraccion_id = a.id
        GROUP BY a.id
        ORDER BY total_recorridos DESC
        """, nativeQuery = true)
    List<AtraccionTuristica> findMasPopulares();
}