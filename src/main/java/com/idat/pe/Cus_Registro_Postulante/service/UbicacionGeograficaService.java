package com.idat.pe.Cus_Registro_Postulante.service;

import com.idat.pe.Cus_Registro_Postulante.entity.UbicacionGeografica;
import java.util.Optional;

/**
 * Servicio para gestionar ubicaciones geográficas
 * Responsable de buscar o crear ubicaciones en la jerarquía:
 * PAÍS -> DEPARTAMENTO -> PROVINCIA -> DISTRITO
 */
public interface UbicacionGeograficaService {
    
    /**
     * Busca o crea una ubicación completa basada en la jerarquía
     * Retorna el ID del DISTRITO
     * 
     * @param pais Nombre del país
     * @param departamento Nombre del departamento
     * @param provincia Nombre de la provincia
     * @param distrito Nombre del distrito
     * @return ID de la ubicación (distrito)
     */
    Integer obtenerOCrearUbicacion(String pais, String departamento, String provincia, String distrito);
    
    /**
     * Busca una ubicación por su ruta completa
     */
    Optional<UbicacionGeografica> buscarPorRutaCompleta(String pais, String departamento, String provincia, String distrito);
    
    /**
     * Obtiene una ubicación por ID
     */
    Optional<UbicacionGeografica> buscarPorId(Integer id);
    
    /**
     * Crea o busca una ubicación por nombre y tipo
     */
    UbicacionGeografica obtenerOCrearUbicacion(String nombre, String tipoUbicacion, Integer idPadre);
}
