package com.idat.pe.Cus_Registro_Postulante.repository;

import com.idat.pe.Cus_Registro_Postulante.model.DeudaExterna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeudaExternaRepository extends JpaRepository<DeudaExterna, Long> {
    Optional<DeudaExterna> findByDni(String dni);
    List<DeudaExterna> findAllByDni(String dni);
}
