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

@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionesController {

    @Autowired
    private UbicacionGeograficaRepository ubicacionRepository;

    @GetMapping("/paises")
    public List<UbicacionDTO> getPaises() {
        return ubicacionRepository.findAll().stream()
                .filter(u -> "PAIS".equals(u.getTipoUbicacion()) && "activo".equals(u.getEstado()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/departamentos/{paisId}")
    public List<UbicacionDTO> getDepartamentos(@PathVariable Integer paisId) {
        return ubicacionRepository.findAll().stream()
                .filter(u -> "DEPARTAMENTO".equals(u.getTipoUbicacion()) 
                        && paisId.equals(u.getIdPadre())
                        && "activo".equals(u.getEstado()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/provincias/{paisId}/{departamentoId}")
    public List<UbicacionDTO> getProvincias(@PathVariable Integer paisId, @PathVariable Integer departamentoId) {
        return ubicacionRepository.findAll().stream()
                .filter(u -> "PROVINCIA".equals(u.getTipoUbicacion()) 
                        && departamentoId.equals(u.getIdPadre())
                        && "activo".equals(u.getEstado()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/distritos/{paisId}/{departamentoId}/{provinciaId}")
    public List<UbicacionDTO> getDistritos(@PathVariable Integer paisId, @PathVariable Integer departamentoId, @PathVariable Integer provinciaId) {
        return ubicacionRepository.findAll().stream()
                .filter(u -> "DISTRITO".equals(u.getTipoUbicacion()) 
                        && provinciaId.equals(u.getIdPadre())
                        && "activo".equals(u.getEstado()))
                .map(this::toDTO)
                .collect(Collectors.toList());
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
