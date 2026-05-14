package com.geotravel.repository;

import com.geotravel.model.ZonaTuristica;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZonaTuristicaRepository extends JpaRepository<ZonaTuristica, Long> {

    Optional<ZonaTuristica> findByNombre(String nombre);

    // Zonas ordenadas por nivel de atractivo
    List<ZonaTuristica> findAllByOrderByNivelAtractivoAsc();

    // Zona que contiene un punto (ej: buscar zona por dirección/coordenada)
    @Query(value = """
        SELECT * FROM zonas_turisticas z
        WHERE ST_Contains(z.geometria, :punto)
        """, nativeQuery = true)
    Optional<ZonaTuristica> findZonaQueContiene(@Param("punto") Geometry punto);

    // Verificar si una geometría se superpone con alguna zona existente (excluyendo la propia)
    @Query(value = """
        SELECT * FROM zonas_turisticas z
        WHERE ST_Intersects(z.geometria, :geometria)
        AND z.id != :excludeId
        """, nativeQuery = true)
    List<ZonaTuristica> findZonasSuperpuestas(
            @Param("geometria") Geometry geometria,
            @Param("excludeId") Long excludeId
    );

    // Zonas con más recorridos activos (consulta geográfica)
    @Query(value = """
        SELECT z.*, COUNT(r.id) as total_recorridos
        FROM zonas_turisticas z
        LEFT JOIN recorridos_turisticos r
            ON ST_Intersects(r.geometria, z.geometria)
            AND r.estado = 'DISPONIBLE'
        GROUP BY z.id
        ORDER BY total_recorridos DESC
        """, nativeQuery = true)
    List<ZonaTuristica> findZonasConMasRecorridos();
}