package com.idat.pe.Cus_Registro_Postulante.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidacionResponseDTO {
    
    private Boolean dniValido;
    private Boolean sinDeudas;
    private Boolean validacionExitosa;
    private String mensaje;
    private String observaciones;
}
