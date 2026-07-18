package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.service.PiattaformaService;

@Controller
public class PiattaformaController {

    @Autowired
    private PiattaformaService piattaformaService;

    @GetMapping("/piattaforme")
    public String showPiattaforme(Model model) {
        model.addAttribute("piattaforme", piattaformaService.findAll());
        return "piattaforme";
    }
}
