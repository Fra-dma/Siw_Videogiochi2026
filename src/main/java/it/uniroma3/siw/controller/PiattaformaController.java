package it.uniroma3.siw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.service.PiattaformaService;

@Controller
public class PiattaformaController {

    private final PiattaformaService piattaformaService;

    public PiattaformaController(PiattaformaService piattaformaService) {
        this.piattaformaService = piattaformaService;
    }

    @GetMapping("/piattaforme")
    public String showPiattaforme(Model model) {
        model.addAttribute("piattaforme", piattaformaService.findAll());
        return "piattaforme";
    }
}
