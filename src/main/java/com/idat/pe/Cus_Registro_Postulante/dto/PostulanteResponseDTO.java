package com.idat.pe.Cus_Registro_Postulante.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostulanteResponseDTO {
    private Long id;
    private String dni;
    private String nombre;
    private String apellido;
    private String correo;
    private String estado;
    private String mensaje;
    private String validacion;
}
