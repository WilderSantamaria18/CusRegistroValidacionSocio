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
        
        // Resolver idUbicacion basado en datos geográficos
        Integer idUbicacion = null;
        if (dto.getDistrito() != null && !dto.getDistrito().trim().isEmpty()) {
            String pais = normalizarTexto(dto.getPais() != null ? dto.getPais() : "Perú");
            String departamento = normalizarTexto(dto.getDepartamento() != null ? dto.getDepartamento() : "");
            String provincia = normalizarTexto(dto.getProvincia() != null ? dto.getProvincia() : "");
            String distrito = normalizarTexto(dto.getDistrito());
            
            try {
                idUbicacion = ubicacionRepository.findDistritoByFullPath(pais, departamento, provincia, distrito)
                        .map(u -> u.getId()).orElse(null);
            } catch (Exception e) {
                // Si hay error en la búsqueda, dejar idUbicacion como null
                idUbicacion = null;
            }
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

    private String normalizarTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return "";
        }
        // Capitalizar primera letra
        String normalizado = texto.trim();
        if (normalizado.length() > 0) {
            normalizado = normalizado.substring(0, 1).toUpperCase() + normalizado.substring(1).toLowerCase();
        }
        return normalizado;
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
