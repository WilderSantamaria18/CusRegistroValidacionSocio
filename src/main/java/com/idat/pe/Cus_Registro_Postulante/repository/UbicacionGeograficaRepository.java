package com.idat.pe.Cus_Registro_Postulante.repository;

import com.idat.pe.Cus_Registro_Postulante.entity.UbicacionGeografica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UbicacionGeograficaRepository extends JpaRepository<UbicacionGeografica, Integer> {
    
    Optional<UbicacionGeografica> findByNombreAndTipoUbicacion(String nombre, String tipoUbicacion);
    
    @Query("SELECT u FROM UbicacionGeografica u WHERE u.nombre = :nombre AND u.tipoUbicacion = :tipo AND u.estado = 'activo'")
    Optional<UbicacionGeografica> findByNombreAndTipoActivo(@Param("nombre") String nombre, @Param("tipo") String tipo);
    
    @Query("""
        SELECT d FROM UbicacionGeografica d 
        WHERE d.nombre = :distrito AND d.tipoUbicacion = 'DISTRITO' 
        AND d.estado = 'activo'
        AND d.idPadre = (
            SELECT p.id FROM UbicacionGeografica p 
            WHERE p.nombre = :provincia AND p.tipoUbicacion = 'PROVINCIA' 
            AND p.estado = 'activo'
            AND p.idPadre = (
                SELECT dp.id FROM UbicacionGeografica dp 
                WHERE dp.nombre = :departamento AND dp.tipoUbicacion = 'DEPARTAMENTO' 
                AND dp.estado = 'activo'
                AND dp.idPadre = (
                    SELECT pa.id FROM UbicacionGeografica pa 
                    WHERE pa.nombre = :pais AND pa.tipoUbicacion = 'PAIS' 
                    AND pa.estado = 'activo'
                )
            )
        )
    """)
    Optional<UbicacionGeografica> findDistritoByFullPath(
        @Param("pais") String pais,
        @Param("departamento") String departamento,
        @Param("provincia") String provincia,
        @Param("distrito") String distrito
    );
}
