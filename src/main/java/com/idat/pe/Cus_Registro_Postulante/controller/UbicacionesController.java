package com.idat.pe.Cus_Registro_Postulante.controller;

import com.idat.pe.Cus_Registro_Postulante.entity.UbicacionGeografica;
import com.idat.pe.Cus_Registro_Postulante.repository.UbicacionGeograficaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionesController {

    private static final Logger logger = LoggerFactory.getLogger(UbicacionesController.class);

    @Autowired
    private UbicacionGeograficaRepository ubicacionRepository;

    @GetMapping("/paises")
    public List<UbicacionDTO> getPaises() {
        logger.info("Obteniendo países");
        List<UbicacionDTO> paises = ubicacionRepository.findAll().stream()
                .filter(u -> "PAIS".equals(u.getTipoUbicacion()) && "activo".equals(u.getEstado()))
                .map(this::toDTO)
                .collect(Collectors.toList());
        logger.info("Países encontrados: {}", paises.size());
        return paises;
    }

    @GetMapping("/departamentos/{paisId}")
    public List<UbicacionDTO> getDepartamentos(@PathVariable Integer paisId) {
        logger.info("Obteniendo departamentos para país: {}", paisId);
        if (paisId == null || paisId <= 0) {
            return List.of();
        }
        List<UbicacionDTO> departamentos = ubicacionRepository.findAll().stream()
                .filter(u -> "DEPARTAMENTO".equals(u.getTipoUbicacion()) 
                        && paisId.equals(u.getIdPadre())
                        && "activo".equals(u.getEstado()))
                .map(this::toDTO)
                .collect(Collectors.toList());
        logger.info("Departamentos encontrados: {}", departamentos.size());
        return departamentos;
    }

    @GetMapping("/provincias/{paisId}/{departamentoId}")
    public List<UbicacionDTO> getProvincias(@PathVariable Integer paisId, @PathVariable Integer departamentoId) {
        logger.info("Obteniendo provincias para país: {}, departamento: {}", paisId, departamentoId);
        if (departamentoId == null || departamentoId <= 0) {
            return List.of();
        }
        List<UbicacionDTO> provincias = ubicacionRepository.findAll().stream()
                .filter(u -> "PROVINCIA".equals(u.getTipoUbicacion()) 
                        && departamentoId.equals(u.getIdPadre())
                        && "activo".equals(u.getEstado()))
                .map(this::toDTO)
                .collect(Collectors.toList());
        logger.info("Provincias encontradas: {}", provincias.size());
        return provincias;
    }

    @GetMapping("/distritos/{paisId}/{departamentoId}/{provinciaId}")
    public List<UbicacionDTO> getDistritos(@PathVariable Integer paisId, @PathVariable Integer departamentoId, @PathVariable Integer provinciaId) {
        logger.info("Obteniendo distritos para país: {}, departamento: {}, provincia: {}", paisId, departamentoId, provinciaId);
        if (provinciaId == null || provinciaId <= 0) {
            return List.of();
        }
        List<UbicacionDTO> distritos = ubicacionRepository.findAll().stream()
                .filter(u -> "DISTRITO".equals(u.getTipoUbicacion()) 
                        && provinciaId.equals(u.getIdPadre())
                        && "activo".equals(u.getEstado()))
                .map(this::toDTO)
                .collect(Collectors.toList());
        logger.info("Distritos encontrados: {}", distritos.size());
        return distritos;
    }

    private UbicacionDTO toDTO(UbicacionGeografica ubicacion) {
        return new UbicacionDTO(ubicacion.getId(), ubicacion.getNombre());
    }

    public static class UbicacionDTO {
        public Integer id;
        public String nombre;

        public UbicacionDTO(Integer id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public Integer getId() { return id; }
        public String getNombre() { return nombre; }
    }
}
