package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.service.VideogiocoService;

@Controller
public class VideogiocoController {

    @Autowired
    private VideogiocoService videogiocoService;

    @GetMapping("/videogiochi")
    public String showVideogiochi(Model model) {
        model.addAttribute("videogiochi", videogiocoService.findAll());
        return "videogiochi"; // Rimanda a videogiochi.html
    }

    @GetMapping("/videogioco/{id}")
    public String showVideogioco(@PathVariable Long id, Model model) {
        model.addAttribute("videogioco", videogiocoService.findById(id).orElse(null));
        return "videogioco"; // Rimanda a videogioco.html
    }
}
