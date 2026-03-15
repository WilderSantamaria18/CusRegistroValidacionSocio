package com.idat.pe.Cus_Registro_Postulante.service;

import com.idat.pe.Cus_Registro_Postulante.dto.RegistroPostulanteDTO;
import com.idat.pe.Cus_Registro_Postulante.dto.PostulanteResponseDTO;
import com.idat.pe.Cus_Registro_Postulante.model.Postulante;
import com.idat.pe.Cus_Registro_Postulante.model.EstadoPostulante;
import com.idat.pe.Cus_Registro_Postulante.model.TipoInteres;
import com.idat.pe.Cus_Registro_Postulante.model.InformeValidacion;
import com.idat.pe.Cus_Registro_Postulante.repository.PostulanteRepository;
import com.idat.pe.Cus_Registro_Postulante.repository.InformeValidacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostulanteService {

    @Autowired
    private PostulanteRepository postulanteRepository;

    @Autowired
    private InformeValidacionRepository informeRepository;

    @Autowired
    private ValidacionService validacionService;

    /**
     * Registra un nuevo postulante.
     *
     * Flujo principal (pasos 5-7 del CUS):
     *   1. Verifica que el DNI no exista.
     *   2. Verifica que el correo no exista.
     *   3. Realiza validación inicial (puede incluir otros clubes en el futuro).
     *   4. Persiste el postulante con estado PENDIENTE.
     *   5. Persiste el informe de validación.
     *   6. Retorna el DTO con el código de seguimiento (id).
     *
     * Lanza RuntimeException con mensaje "DNI" o "correo" para que el controlador
     * pueda distinguir FA2 de FA3.
     */
    public PostulanteResponseDTO registrarPostulante(RegistroPostulanteDTO dto) {

        // FA2 — Verificar DNI único
        if (postulanteRepository.findByDni(dto.getDni()).isPresent()) {
            throw new RuntimeException("El DNI " + dto.getDni() + " ya se encuentra registrado con una solicitud activa.");
        }

        // FA2 — Verificar correo único
        if (postulanteRepository.findByCorreo(dto.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo " + dto.getCorreo() + " ya está asociado a una solicitud existente.");
        }

        // Validación inicial (integración con otros clubes se hará en CUS_Validar_con_Otros_Clubes)
        var validacion = validacionService.validarPostulante(dto.getDni());

        // Convertir tipoInteres string → enum (default INSTALACIONES si null/inválido)
        TipoInteres interes;
        try {
            interes = TipoInteres.valueOf(dto.getTipoInteres());
        } catch (Exception ex) {
            interes = TipoInteres.INSTALACIONES;
        }

        // Parsear apellido completo en apellido_paterno y apellido_materno
        String[] apellidos = dto.getApellido().split(" ", 2);
        String apellidoPaterno = apellidos[0];
        String apellidoMaterno = apellidos.length > 1 ? apellidos[1] : "";

        // Crear y persistir postulante
        Postulante postulante = Postulante.builder()
                .dni(dto.getDni())
                .nombres(dto.getNombre())
                .apellidoPaterno(apellidoPaterno)
                .apellidoMaterno(apellidoMaterno)
                .correo(dto.getCorreo())
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .ciudad(dto.getCiudad())
                .fechaNacimiento(dto.getFechaNacimiento())
                .tipoInteres(interes)
                .estado(EstadoPostulante.PENDIENTE)
                .build();

        Postulante guardado = postulanteRepository.save(postulante);

        // Guardar informe de validación
        InformeValidacion informe = InformeValidacion.builder()
                .postulante(guardado)
                .dniValido(validacion.getDniValido())
                .sinDeudas(validacion.getSinDeudas())
                .validacionExitosa(validacion.getValidacionExitosa())
                .observaciones(validacion.getObservaciones())
                .build();

        informeRepository.save(informe);

        // Retornar respuesta con código de seguimiento = id
        return PostulanteResponseDTO.builder()
                .id(guardado.getId())
                .dni(guardado.getDni())
                .nombre(guardado.getNombres())
                .apellido(guardado.getApellidoPaterno() + " " + guardado.getApellidoMaterno())
                .correo(guardado.getCorreo())
                .estado(guardado.getEstado().toString())
                .mensaje("Solicitud de registro enviada exitosamente. Su número de seguimiento es " + guardado.getId())
                .validacion(validacion.getObservaciones())
                .build();
    }
}
