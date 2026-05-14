package com.geotravel.repository;

import com.geotravel.model.RecorridoTuristico;
import com.geotravel.model.enums.EstadoRecorrido;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecorridoTuristicoRepository extends JpaRepository<RecorridoTuristico, Long> {

    List<RecorridoTuristico> findByEstado(EstadoRecorrido estado);

    // Recorridos dentro de una zona seleccionada
    @Query(value = """
        SELECT r.* FROM recorridos_turisticos r, zonas_turisticas z
        WHERE z.id = :zonaId
        AND ST_Intersects(r.geometria, z.geometria)
        """, nativeQuery = true)
    List<RecorridoTuristico> findRecorridosEnZona(@Param("zonaId") Long zonaId);

    // Recorridos dentro de una zona filtrados por estado
    @Query(value = """
        SELECT r.* FROM recorridos_turisticos r, zonas_turisticas z
        WHERE z.id = :zonaId
        AND ST_Intersects(r.geometria, z.geometria)
        AND r.estado = :estado
        """, nativeQuery = true)
    List<RecorridoTuristico> findRecorridosEnZonaPorEstado(
            @Param("zonaId") Long zonaId,
            @Param("estado") String estado
    );

    // Recorrido más cercano a un punto (ej: intersección de calles)
    @Query(value = """
        SELECT r.* FROM recorridos_turisticos r
        WHERE r.geometria IS NOT NULL
        ORDER BY ST_Distance(r.geometria, :punto)
        LIMIT 1
        """, nativeQuery = true)
    Optional<RecorridoTuristico> findMasCercanoA(@Param("punto") Point punto);
}