package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.service.RecensioneService;

@Controller
public class RecensioneController {

    @Autowired
    private RecensioneService recensioneService;

    @GetMapping("/recensioni")
    public String showRecensioni(Model model) {
        model.addAttribute("recensioni", recensioneService.findAll());
        return "recensioni";
    }

    @PostMapping("/recensione")
    public String addRecensione(@ModelAttribute Recensione recensione) {
        recensioneService.save(recensione);
        return "redirect:/videogioco/" + recensione.getVideogioco().getId();
    }
}