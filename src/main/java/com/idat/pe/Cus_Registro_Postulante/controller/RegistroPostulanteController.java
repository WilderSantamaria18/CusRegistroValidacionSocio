package com.idat.pe.Cus_Registro_Postulante.controller;

import com.idat.pe.Cus_Registro_Postulante.dto.PostulanteDTO;
import com.idat.pe.Cus_Registro_Postulante.dto.RegistroPostulanteDTO;
import com.idat.pe.Cus_Registro_Postulante.genericResponse.GenericResponse;
import com.idat.pe.Cus_Registro_Postulante.service.PostulanteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postulantes")
public class RegistroPostulanteController {

    @Autowired
    private PostulanteService postulanteService;

    @PostMapping("/registrar")
    public ResponseEntity<GenericResponse<String, PostulanteDTO>> registrar(@Valid @RequestBody RegistroPostulanteDTO dto) {
        PostulanteDTO response = postulanteService.registrarPostulante(dto);
        return ResponseEntity.ok(GenericResponse.<String, PostulanteDTO>builder()
                .message("Postulante registrado con éxito")
                .body(response)
                .build());
    }

    @GetMapping("/listar")
    public ResponseEntity<GenericResponse<String, List<PostulanteDTO>>> listar() {
        List<PostulanteDTO> list = postulanteService.listarPostulantes();
        return ResponseEntity.ok(GenericResponse.<String, List<PostulanteDTO>>builder()
                .message("Lista de postulantes obtenida")
                .body(list)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<String, PostulanteDTO>> buscar(@PathVariable Integer id) {
        PostulanteDTO response = postulanteService.buscarPorId(id);
        return ResponseEntity.ok(GenericResponse.<String, PostulanteDTO>builder()
                .message("Postulante encontrado")
                .body(response)
                .build());
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<GenericResponse<String, PostulanteDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody RegistroPostulanteDTO dto) {
        PostulanteDTO response = postulanteService.actualizarDatos(id, dto);
        return ResponseEntity.ok(GenericResponse.<String, PostulanteDTO>builder()
                .message("Datos actualizados con éxito")
                .body(response)
                .build());
    }

    @PatchMapping("/cambiar-estado/{id}")
    public ResponseEntity<GenericResponse<String, PostulanteDTO>> cambiarEstado(@PathVariable Integer id, @RequestParam String nuevoEstado) {
        PostulanteDTO response = postulanteService.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok(GenericResponse.<String, PostulanteDTO>builder()
                .message("Estado actualizado con éxito")
                .body(response)
                .build());
    }
}
