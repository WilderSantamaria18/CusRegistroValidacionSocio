package com.idat.pe.Cus_Registro_Postulante.controller;

import com.idat.pe.Cus_Registro_Postulante.dto.RegistroPostulanteDTO;
import com.idat.pe.Cus_Registro_Postulante.service.PostulanteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registro")
public class RegistroPostulanteController {

    @Autowired
    private PostulanteService postulanteService;

    /**
     * GET /registro — Muestra el formulario de registro (Flujo principal: paso 2)
     */
    @GetMapping("")
    public String mostrarFormulario(Model model) {
        model.addAttribute("registroDTO", new RegistroPostulanteDTO());
        return "registro/formulario-registro";
    }

    /**
     * POST /registro/guardar — Procesa el formulario (Flujo principal: pasos 4-7)
     *
     * FA1: BindingResult con errores → volvemos al formulario con mensajes de validación.
     * FA2: DNI o correo duplicado  → model attribute "errorDuplicado" para banner amarillo.
     * FA3: Error inesperado        → model attribute "error" para banner rojo genérico.
     */
    @PostMapping("/guardar")
    public String guardarRegistro(
            @Valid @ModelAttribute("registroDTO") RegistroPostulanteDTO registroDTO,
            BindingResult result,
            Model model) {

        // FA1 — Campos obligatorios incompletos o con formato incorrecto
        if (result.hasErrors()) {
            return "registro/formulario-registro";
        }

        try {
            var postulante = postulanteService.registrarPostulante(registroDTO);
            model.addAttribute("postulante", postulante);
            return "registro/registro-exitoso";

        } catch (RuntimeException e) {
            String msg = e.getMessage() != null ? e.getMessage() : "";

            // FA2 — DNI o correo ya registrado: lanza mensaje con "DNI" o "correo"
            if (msg.toLowerCase().contains("dni") || msg.toLowerCase().contains("correo")) {
                model.addAttribute("errorDuplicado", msg +
                        " Si ya registró una solicitud, puede consultar su estado con su código de seguimiento.");
            } else {
                // FA3 — Error genérico de servidor o red
                model.addAttribute("error", "Error al enviar la solicitud. Intente nuevamente más tarde.");
            }

            return "registro/formulario-registro";
        }
    }
}
