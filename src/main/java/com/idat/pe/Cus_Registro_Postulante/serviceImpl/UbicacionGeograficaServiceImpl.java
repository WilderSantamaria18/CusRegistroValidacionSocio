package com.idat.pe.Cus_Registro_Postulante.serviceImpl;

import com.idat.pe.Cus_Registro_Postulante.entity.UbicacionGeografica;
import com.idat.pe.Cus_Registro_Postulante.repository.UbicacionGeograficaRepository;
import com.idat.pe.Cus_Registro_Postulante.service.UbicacionGeograficaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Implementación del servicio de ubicaciones geográficas
 * Responsible para gestionar la jerarquía: PAÍS -> DEPARTAMENTO -> PROVINCIA -> DISTRITO
 */
@Service
public class UbicacionGeograficaServiceImpl implements UbicacionGeograficaService {
    
    private static final Logger logger = LoggerFactory.getLogger(UbicacionGeograficaServiceImpl.class);
    
    @Autowired
    private UbicacionGeograficaRepository ubicacionRepository;
    
    /**
     * Busca o crea una ubicación completa basada en la jerarquía
     * Crea cada nivel si no existe: País -> Departamento -> Provincia -> Distrito
     */
    @Override
    @Transactional
    public Integer obtenerOCrearUbicacion(String pais, String departamento, String provincia, String distrito) {
        try {
            logger.info("🌍 Buscando/creando ubicación completa: {}/{}/{}/{}", pais, departamento, provincia, distrito);
            
            // 1. Buscar o crear PAÍS
            logger.info("   1️⃣  Procesando PAÍS: {}", pais);
            UbicacionGeografica ubicacionPais = obtenerOCrearUbicacion(pais, "PAIS", null);
            logger.info("   ✓ País ID: {}", ubicacionPais.getId());
            
            // 2. Buscar o crear DEPARTAMENTO con padre = PAÍS
            logger.info("   2️⃣  Procesando DEPARTAMENTO: {}", departamento);
            UbicacionGeografica ubicacionDept = obtenerOCrearUbicacion(departamento, "DEPARTAMENTO", ubicacionPais.getId());
            logger.info("   ✓ Departamento ID: {}", ubicacionDept.getId());
            
            // 3. Buscar o crear PROVINCIA con padre = DEPARTAMENTO
            logger.info("   3️⃣  Procesando PROVINCIA: {}", provincia);
            UbicacionGeografica ubicacionProv = obtenerOCrearUbicacion(provincia, "PROVINCIA", ubicacionDept.getId());
            logger.info("   ✓ Provincia ID: {}", ubicacionProv.getId());
            
            // 4. Buscar o crear DISTRITO con padre = PROVINCIA
            logger.info("   4️⃣  Procesando DISTRITO: {}", distrito);
            UbicacionGeografica ubicacionDist = obtenerOCrearUbicacion(distrito, "DISTRITO", ubicacionProv.getId());
            logger.info("   ✓ Distrito ID: {}", ubicacionDist.getId());
            
            logger.info("✅ Ubicación completa creada/encontrada. Retornando ID del DISTRITO: {}", ubicacionDist.getId());
            return ubicacionDist.getId();
            
        } catch (Exception e) {
            logger.error("❌ ERROR al procesar ubicación completa", e);
            throw new RuntimeException("Error al procesar ubicación: " + e.getMessage(), e);
        }
    }
    
    /**
     * Implementación del segundo método: buscar por ruta completa
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UbicacionGeografica> buscarPorRutaCompleta(String pais, String departamento, String provincia, String distrito) {
        logger.info("🔍 Buscando ubicación existente: {}/{}/{}/{}", pais, departamento, provincia, distrito);
        Optional<UbicacionGeografica> resultado = ubicacionRepository.findDistritoByFullPath(pais, departamento, provincia, distrito);
        logger.info("   Resultado: {}", resultado.isPresent() ? "Encontrada" : "No encontrada");
        return resultado;
    }
    
    /**
     * Implementación del tercero: buscar por ID
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UbicacionGeografica> buscarPorId(Integer id) {
        return ubicacionRepository.findById(id);
    }
    
    /**
     * Método privado: Busca o crea una ubicación por nombre, tipo y padre
     * Reutilizable para cualquier nivel de la jerarquía
     */
    @Override
    @Transactional
    public UbicacionGeografica obtenerOCrearUbicacion(String nombre, String tipoUbicacion, Integer idPadre) {
        try {
            logger.info("      🔎 Buscando {}: {} (padre: {})", tipoUbicacion, nombre, idPadre == null ? "NINGUNO" : idPadre);
            
            // Buscar existente
            Optional<UbicacionGeografica> existente = ubicacionRepository.findByNombreAndTipoActivo(nombre, tipoUbicacion);
            
            if (existente.isPresent()) {
                UbicacionGeografica ubicacion = existente.get();
                logger.info("         ✓ {} existente encontrada (ID: {})", tipoUbicacion, ubicacion.getId());
                return ubicacion;
            }
            
            // Crear nueva si no existe
            logger.info("         ➕ {} no encontrada, creando nueva", tipoUbicacion);
            UbicacionGeografica nueva = UbicacionGeografica.builder()
                    .nombre(nombre)
                    .tipoUbicacion(tipoUbicacion)
                    .idPadre(idPadre)
                    .estado("activo")
                    .build();
            
            UbicacionGeografica guardada = ubicacionRepository.save(nueva);
            logger.info("         ✅ {} creada exitosamente (ID: {})", tipoUbicacion, guardada.getId());
            
            return guardada;
            
        } catch (Exception e) {
            logger.error("         ❌ ERROR procesando {}: {}", tipoUbicacion, nombre, e);
            throw new RuntimeException("Error al procesar ubicación: " + e.getMessage(), e);
        }
    }
}
