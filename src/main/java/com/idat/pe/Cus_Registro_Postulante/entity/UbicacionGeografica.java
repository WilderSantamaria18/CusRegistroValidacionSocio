package com.idat.pe.Cus_Registro_Postulante.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ubicacion_geografica")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UbicacionGeografica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "tipo_ubicacion", nullable = false, length = 20)
    private String tipoUbicacion; // PAIS, DEPARTAMENTO, PROVINCIA, DISTRITO

    @Column(name = "id_padre")
    private Integer idPadre;

    @Column(name = "codigo_postal", length = 20)
    private String codigoPostal;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado; // activo, inactivo
}
