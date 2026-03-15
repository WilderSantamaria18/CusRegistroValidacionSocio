package com.idat.pe.Cus_Registro_Postulante.repository;

import com.idat.pe.Cus_Registro_Postulante.model.Postulante;
import com.idat.pe.Cus_Registro_Postulante.model.EstadoPostulante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface PostulanteRepository extends JpaRepository<Postulante, Long> {
    Optional<Postulante> findByDni(String dni);
    Optional<Postulante> findByCorreo(String correo);
    List<Postulante> findByEstado(EstadoPostulante estado);
}
