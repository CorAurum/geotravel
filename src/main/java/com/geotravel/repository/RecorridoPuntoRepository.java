package com.geotravel.repository;

import com.geotravel.model.RecorridoPunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecorridoPuntoRepository extends JpaRepository<RecorridoPunto, Long> {

    List<RecorridoPunto> findByRecorridoIdOrderByOrdenAsc(Long recorridoId);

    void deleteByRecorridoId(Long recorridoId);
}