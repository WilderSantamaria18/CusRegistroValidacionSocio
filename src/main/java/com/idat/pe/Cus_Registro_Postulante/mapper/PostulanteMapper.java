package com.idat.pe.Cus_Registro_Postulante.mapper;

import com.idat.pe.Cus_Registro_Postulante.dto.PostulanteDTO;
import com.idat.pe.Cus_Registro_Postulante.dto.RegistroPostulanteDTO;
import com.idat.pe.Cus_Registro_Postulante.entity.Postulante;
import com.idat.pe.Cus_Registro_Postulante.entity.EstadoPostulante;
import com.idat.pe.Cus_Registro_Postulante.entity.TipoDocumento;
import com.idat.pe.Cus_Registro_Postulante.repository.UbicacionGeograficaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class PostulanteMapper {

    @Autowired
    private UbicacionGeograficaRepository ubicacionRepository;

    public Postulante toEntity(RegistroPostulanteDTO dto) {
        if (dto == null) return null;

        TipoDocumento tipo = TipoDocumento.valueOf(dto.getTipoDocumento());
        
        // Resolver idUbicacion basado en datos geográficos si no está proporcionado directamente
        Integer idUbicacion = dto.getIdUbicacion();
        if (idUbicacion == null && dto.getDistrito() != null && !dto.getDistrito().trim().isEmpty()) {
            idUbicacion = ubicacionRepository.findDistritoByFullPath(
                    dto.getPais() != null ? dto.getPais() : "Perú",
                    dto.getDepartamento() != null ? dto.getDepartamento() : "",
                    dto.getProvincia() != null ? dto.getProvincia() : "",
                    dto.getDistrito()
            ).map(u -> u.getId()).orElse(null);
        }
        
        Postulante.PostulanteBuilder builder = Postulante.builder()
                .tipoDocumento(tipo)
                .numeroDocumento(dto.getNumeroDocumento())
                .correoElectronico(dto.getCorreo())
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .idUbicacion(idUbicacion)
                .tipoInteres(dto.getTipoInteres())
                .codigoPostal(dto.getCodigoPostal())
                .fechaRegistro(LocalDate.now())
                .estado(EstadoPostulante.PENDIENTE);

        if (tipo == TipoDocumento.DNI) {
            builder.nombres(blankToNull(dto.getNombre()))
                   .apellidoPaterno(blankToNull(dto.getApellidoPaterno()))
                   .apellidoMaterno(blankToNull(dto.getApellidoMaterno()))
                   .fechaNacimiento(dto.getFechaNacimiento())
                   .razonSocial(null);
        } else {
            builder.razonSocial(blankToNull(dto.getRazonSocial()))
                   .nombres(null)
                   .apellidoPaterno(null)
                   .apellidoMaterno(null)
                   .fechaNacimiento(null);
        }

        return builder.build();
    }

    private String blankToNull(String s) {
        return (s == null || s.trim().isEmpty()) ? null : s;
    }

    public PostulanteDTO toDTO(Postulante entity) {
        if (entity == null) return null;

        return PostulanteDTO.builder()
                .id(entity.getId())
                .tipoDocumento(entity.getTipoDocumento().name())
                .numeroDocumento(entity.getNumeroDocumento())
                .nombres(entity.getNombres())
                .apellidoPaterno(entity.getApellidoPaterno())
                .apellidoMaterno(entity.getApellidoMaterno())
                .razonSocial(entity.getRazonSocial())
                .correo(entity.getCorreoElectronico())
                .telefono(entity.getTelefono())
                .direccion(entity.getDireccion())
                .idUbicacion(entity.getIdUbicacion())
                .fechaNacimiento(entity.getFechaNacimiento())
                .tipoInteres(entity.getTipoInteres())
                .codigoPostal(entity.getCodigoPostal())
                .fechaRegistro(entity.getFechaRegistro())
                .estado(entity.getEstado().name())
                .build();
    }
}
