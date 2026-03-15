package com.idat.pe.Cus_Registro_Postulante.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "informes_validacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformeValidacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "postulante_id", nullable = false, unique = true)
    private Postulante postulante;
    
    @Column(nullable = false)
    private Boolean dniValido = false;
    
    @Column(nullable = false)
    private Boolean sinDeudas = false;
    
    @Column(nullable = false)
    private Boolean validacionExitosa = false;
    
    @Column(length = 500)
    private String observaciones;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaValidacion = LocalDateTime.now();
}
