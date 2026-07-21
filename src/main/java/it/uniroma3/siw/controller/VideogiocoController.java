package it.uniroma3.siw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.Videogioco;
import it.uniroma3.siw.model.VideogiocoLibreria;
import it.uniroma3.siw.repo.RepositoryUtente;
import it.uniroma3.siw.repo.RepositoryVideogiocoLibreria;
import it.uniroma3.siw.service.UtenteService;
import it.uniroma3.siw.service.VideogiocoService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Controller
public class VideogiocoController {

    private final VideogiocoService videogiocoService;
    private final RepositoryUtente utenteRepository;
    private final RepositoryVideogiocoLibreria videogiocoLibreriaRepository;
    private final UtenteService utenteService;

    public VideogiocoController(VideogiocoService videogiocoService,
                                RepositoryUtente utenteRepository,
                                RepositoryVideogiocoLibreria videogiocoLibreriaRepository,
                                UtenteService utenteService) {
        this.videogiocoService = videogiocoService;
        this.utenteRepository = utenteRepository;
        this.videogiocoLibreriaRepository = videogiocoLibreriaRepository;
        this.utenteService = utenteService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        String username;
        String email = "default@example.com";
        if (authentication.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            username = oauth2User.getAttribute("email");
            email = oauth2User.getAttribute("email");
            if (username == null) username = authentication.getName();
        } else {
            username = authentication.getName();
        }
        
        Utente u = utenteService.findByUsername(username).orElse(null);
        if (u == null) {
            u = new Utente();
            u.setUsername(username);
            u.setEmail(email);
            u.setPassword("oauth2_placeholder");
            u.setRuolo("USER");
            utenteService.save(u);
        }
        return u.getId();
    }

    @GetMapping("/videogiochi")
    public String showVideogiochi(Model model) {
        model.addAttribute("videogiochi", videogiocoService.findAll());
        return "videogiochi"; 
    }

    @GetMapping("/videogioco/{id}")
    public String showVideogioco(@PathVariable Long id, Model model) {
        Videogioco videogioco = videogiocoService.findById(id).orElse(null);
        model.addAttribute("videogioco", videogioco);
        
        Long idUtenteAttuale = getCurrentUserId(); 

        if (videogioco != null && idUtenteAttuale != null) {
            Utente utente = utenteRepository.findById(idUtenteAttuale).orElse(null);
            
            if (utente != null) {
                // Cerchiamo la recensione (il collegamento nella libreria) per questo utente e questo gioco
                VideogiocoLibreria recensione = videogiocoLibreriaRepository
                        .findByUtenteAndVideogioco(utente, videogioco)
                        .orElse(null);
                
                // Passiamo la recensione al model (l'HTML la userà con ${recensione})
                model.addAttribute("recensione", recensione);
            }
        }
        
        return "videogioco"; 
    } 
    
    @GetMapping("/")
    public String showHomePage() {
        return "redirect:/rawg/popolari"; 
    }
}