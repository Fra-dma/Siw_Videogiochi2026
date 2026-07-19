package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    
    @GetMapping("/")
    public String showHomePage(Model model) {
        // Passa la lista dei giochi per la griglia
        model.addAttribute("videogiochi", videogiocoService.findAll());
        
        // Verifica se l'utente è loggato (sia in modo classico che con Google)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && 
                                  authentication.isAuthenticated() && 
                                  !(authentication instanceof AnonymousAuthenticationToken);
        
        model.addAttribute("isAuthenticated", isAuthenticated);
        
        return "index"; // Assicurati di avere il file home.html in src/main/resources/templates/
    }
}
