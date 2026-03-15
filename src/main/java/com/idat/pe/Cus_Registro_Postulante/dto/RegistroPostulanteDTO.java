package com.idat.pe.Cus_Registro_Postulante.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroPostulanteDTO {

    // ── Obligatorios ──────────────────────────────────────────────────────────

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    /**
     * Área de interés en el club:
     * NAUTICO       → orientado a actividades náuticas / embarcaciones
     * INSTALACIONES → orientado al uso de piscinas, restaurante, zonas sociales
     */
    @NotBlank(message = "Seleccione un área de interés")
    private String tipoInteres = "INSTALACIONES";

    // ── Opcionales ────────────────────────────────────────────────────────────

    private LocalDate fechaNacimiento;

    private String codigoPostal;

    private String departamento;
}
