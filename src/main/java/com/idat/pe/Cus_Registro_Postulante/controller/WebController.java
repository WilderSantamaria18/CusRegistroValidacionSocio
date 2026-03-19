package com.idat.pe.Cus_Registro_Postulante.controller;

import com.idat.pe.Cus_Registro_Postulante.dto.RegistroPostulanteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

    @Autowired
    private com.idat.pe.Cus_Registro_Postulante.service.PostulanteService postulanteService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        if (!model.containsAttribute("registroDTO")) {
            model.addAttribute("registroDTO", new RegistroPostulanteDTO());
        }
        return "registro/formulario-registro";
    }

    @PostMapping("/registro/guardar")
    public String guardar(@ModelAttribute("registroDTO") RegistroPostulanteDTO dto, 
                          BindingResult result, 
                          org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "registro/formulario-registro";
        }
        try {
            com.idat.pe.Cus_Registro_Postulante.dto.PostulanteDTO saved = postulanteService.registrarPostulante(dto);
            redirectAttributes.addFlashAttribute("postulante", saved);
            return "redirect:/registro/exitoso";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("registroDTO", dto);
            return "redirect:/registro";
        }
    }

    @GetMapping("/registro/exitoso")
    public String exitoso(Model model) {
        if (!model.containsAttribute("postulante")) {
            return "redirect:/registro";
        }
        return "registro/registro-exitoso";
    }
}
