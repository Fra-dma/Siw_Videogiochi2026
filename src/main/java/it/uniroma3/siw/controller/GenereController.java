package it.uniroma3.siw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.service.GenereService;

@Controller
public class GenereController {

    private final GenereService genereService;

    public GenereController(GenereService genereService) {
        this.genereService = genereService;
    }

    @GetMapping("/generi")
    public String showGeneri(Model model) {
        model.addAttribute("generi", genereService.findAll());
        return "generi";
    }
}
