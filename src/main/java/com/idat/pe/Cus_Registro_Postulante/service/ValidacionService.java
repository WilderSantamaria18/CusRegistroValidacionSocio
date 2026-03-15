package com.idat.pe.Cus_Registro_Postulante.service;

import com.idat.pe.Cus_Registro_Postulante.dto.ValidacionResponseDTO;
import com.idat.pe.Cus_Registro_Postulante.repository.DeudaExternaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidacionService {
    
    @Autowired
    private DeudaExternaRepository deudaRepository;
    
    /**
     * Valida el DNI (formato básico)
     */
    public Boolean validarDNI(String dni) {
        if (dni == null || !dni.matches("\\d{8}")) {
            return false;
        }
        // Rechazar DNI conocidos como inválidos
        return !dni.equals("00000000");
    }
    
    /**
     * Consulta deudas en otros clubes (simulado)
     */
    public Boolean consultarDeudas(String dni) {
        long deudas = deudaRepository.findAllByDni(dni).size();
        return deudas == 0; // true si NO tiene deudas
    }
    
    /**
     * Validación básica del postulante
     */
    public ValidacionResponseDTO validarPostulante(String dni) {
        Boolean dniValido = validarDNI(dni);
        Boolean sinDeudas = consultarDeudas(dni);
        Boolean validacionExitosa = dniValido && sinDeudas;
        
        String observaciones = validacionExitosa 
            ? "Validación completada. Pendiente de evaluación."
            : "El postulante no cumple los requisitos previos.";
        
        return ValidacionResponseDTO.builder()
                .dniValido(dniValido)
                .sinDeudas(sinDeudas)
                .validacionExitosa(validacionExitosa)
                .mensaje(validacionExitosa ? "Validado" : "Rechazado")
                .observaciones(observaciones)
                .build();
    }
}

