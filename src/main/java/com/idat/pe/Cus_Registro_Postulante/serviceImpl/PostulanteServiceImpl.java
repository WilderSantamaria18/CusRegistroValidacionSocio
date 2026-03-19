package com.idat.pe.Cus_Registro_Postulante.serviceImpl;

import com.idat.pe.Cus_Registro_Postulante.dto.PostulanteDTO;
import com.idat.pe.Cus_Registro_Postulante.dto.RegistroPostulanteDTO;
import com.idat.pe.Cus_Registro_Postulante.entity.Postulante;
import com.idat.pe.Cus_Registro_Postulante.entity.EstadoPostulante;
import com.idat.pe.Cus_Registro_Postulante.entity.TipoDocumento;
import com.idat.pe.Cus_Registro_Postulante.mapper.PostulanteMapper;
import com.idat.pe.Cus_Registro_Postulante.repository.PostulanteRepository;
import com.idat.pe.Cus_Registro_Postulante.service.PostulanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostulanteServiceImpl implements PostulanteService {

    @Autowired
    private PostulanteRepository postulanteRepository;

    @Autowired
    private PostulanteMapper postulanteMapper;

    @Override
    @Transactional
    public PostulanteDTO registrarPostulante(RegistroPostulanteDTO dto) {
        if (postulanteRepository.findByNumeroDocumento(dto.getNumeroDocumento()).isPresent()) {
            throw new RuntimeException("El documento " + dto.getNumeroDocumento() + " ya se encuentra registrado.");
        }
        if (postulanteRepository.findByCorreoElectronico(dto.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo " + dto.getCorreo() + " ya se encuentra registrado.");
        }

        Postulante postulante = postulanteMapper.toEntity(dto);
        Postulante guardado = postulanteRepository.save(postulante);
        return postulanteMapper.toDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostulanteDTO> listarPostulantes() {
        List<Postulante> postulantes = postulanteRepository.findAll();
        List<PostulanteDTO> dtos = new java.util.ArrayList<>();
        
        for (Postulante p : postulantes) {
            dtos.add(postulanteMapper.toDTO(p));
        }
        
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public PostulanteDTO buscarPorId(Integer id) {
        Postulante postulante = postulanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Postulante no encontrado con id: " + id));
        return postulanteMapper.toDTO(postulante);
    }

    @Override
    @Transactional
    public PostulanteDTO actualizarDatos(Integer id, RegistroPostulanteDTO dto) {
        Postulante postulante = postulanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Postulante no encontrado con id: " + id));

        // Actualizar campos comunes
        postulante.setTipoDocumento(TipoDocumento.valueOf(dto.getTipoDocumento()));
        postulante.setNumeroDocumento(dto.getNumeroDocumento());
        postulante.setCorreoElectronico(dto.getCorreo());
        postulante.setTelefono(dto.getTelefono());
        postulante.setDireccion(dto.getDireccion());
        postulante.setIdCiudad(dto.getIdCiudad());
        postulante.setTipoInteres(dto.getTipoInteres());
        postulante.setCodigoPostal(dto.getCodigoPostal());

        // Campos según tipo
        if (postulante.getTipoDocumento() == TipoDocumento.DNI) {
            postulante.setNombres(dto.getNombre());
            postulante.setApellidoPaterno(dto.getApellidoPaterno());
            postulante.setApellidoMaterno(dto.getApellidoMaterno());
            postulante.setFechaNacimiento(dto.getFechaNacimiento());
            postulante.setRazonSocial(null);
        } else {
            postulante.setRazonSocial(dto.getRazonSocial());
            postulante.setNombres(null);
            postulante.setApellidoPaterno(null);
            postulante.setApellidoMaterno(null);
            postulante.setFechaNacimiento(null);
        }

        Postulante actualizado = postulanteRepository.save(postulante);
        return postulanteMapper.toDTO(actualizado);
    }

    @Override
    @Transactional
    public PostulanteDTO cambiarEstado(Integer id, String nuevoEstado) {
        Postulante postulante = postulanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Postulante no encontrado con id: " + id));

        try {
            postulante.setEstado(EstadoPostulante.valueOf(nuevoEstado));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado no válido: " + nuevoEstado);
        }

        Postulante actualizado = postulanteRepository.save(postulante);
        return postulanteMapper.toDTO(actualizado);
    }
}
