package com.idat.pe.Cus_Registro_Postulante.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "deudas_externas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeudaExterna {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String dni;
    
    private Double monto;
    
    private String institucion;
    
    private String descripcion;
}
