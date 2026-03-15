package com.idat.pe.Cus_Registro_Postulante.repository;

import com.idat.pe.Cus_Registro_Postulante.model.InformeValidacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InformeValidacionRepository extends JpaRepository<InformeValidacion, Long> {
    Optional<InformeValidacion> findByPostulanteId(Long postulanteId);
}
