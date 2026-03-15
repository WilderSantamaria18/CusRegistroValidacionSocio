package com.idat.pe.Cus_Registro_Postulante.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "postulante")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Postulante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_postulante")
    private Long id;

    @NotBlank(message = "El DNI no puede estar vacío")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    @Column(unique = true, nullable = false)
    private String dni;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellido_paterno")
    private String apellidoPaterno;

    @Column(name = "apellido_materno")
    private String apellidoMaterno;

    @Email(message = "El correo debe ser válido")
    @Column(name = "correo_electronico", unique = true, nullable = false)
    private String correo;

    private String telefono;

    private String direccion;

    private String ciudad;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    /**
     * Área de interés del postulante dentro del club:
     * NAUTICO      → actividades en el mar / embarcaciones.
     * INSTALACIONES → uso de piscinas, restaurante, zonas sociales.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_interes", nullable = false)
    private TipoInteres tipoInteres = TipoInteres.INSTALACIONES;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_postulacion", nullable = false)
    private EstadoPostulante estado = EstadoPostulante.PENDIENTE;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @OneToOne(mappedBy = "postulante", cascade = CascadeType.ALL)
    private InformeValidacion informe;
}
