package it.uniroma3.siw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.service.SviluppatoreService;

@Controller
public class SviluppatoreController {

    private final SviluppatoreService sviluppatoreService;

    public SviluppatoreController(SviluppatoreService sviluppatoreService) {
        this.sviluppatoreService = sviluppatoreService;
    }

    @GetMapping("/sviluppatori")
    public String showSviluppatori(Model model) {
        model.addAttribute("sviluppatori", sviluppatoreService.findAll());
        return "sviluppatori";
    }

    @GetMapping("/sviluppatore/{id}")
    public String showSviluppatore(@PathVariable Long id, Model model) {
        model.addAttribute("sviluppatore", sviluppatoreService.findById(id).orElse(null));
        return "sviluppatore";
    }
}
