package it.uniroma3.siw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.Videogioco;
import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.service.UtenteService;
import it.uniroma3.siw.service.VideogiocoService;
import it.uniroma3.siw.service.CommentoService;
import it.uniroma3.siw.service.VideogiocoLibreriaService;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class VideogiocoController {

    private final VideogiocoService videogiocoService;
    private final UtenteService utenteService;
    private final CommentoService commentoService;
    private final VideogiocoLibreriaService videogiocoLibreriaService;

    public VideogiocoController(VideogiocoService videogiocoService,
            UtenteService utenteService,
            CommentoService commentoService,
            VideogiocoLibreriaService videogiocoLibreriaService) {
        this.videogiocoService = videogiocoService;
        this.utenteService = utenteService;
        this.commentoService = commentoService;
        this.videogiocoLibreriaService = videogiocoLibreriaService;
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

        Long idUtenteAttuale = utenteService.getCurrentUserId();

        if (videogioco != null && idUtenteAttuale != null) {
            Utente utente = utenteService.findById(idUtenteAttuale).orElse(null);

            if (utente != null) {
                // Controlla se il gioco è nella libreria
                boolean inLibreria = videogiocoLibreriaService.isGiocoInLibreria(utente, videogioco);
                model.addAttribute("inLibreria", inLibreria);

                // Cerchiamo la recensione (il commento) per questo utente e questo gioco
                Commento recensione = commentoService.findRecensione(utente, videogioco);
                model.addAttribute("recensione", recensione);
            }
        }

        if (videogioco != null) {
            List<Commento> tuttiICommenti = commentoService.findRecensioniByVideogioco(videogioco);
            List<Commento> altreRecensioni = tuttiICommenti.stream()
                    .filter(c -> c.getAutore() == null || !c.getAutore().getId().equals(idUtenteAttuale))
                    .collect(Collectors.toList());
            model.addAttribute("altreRecensioni", altreRecensioni);
        }

        return "videogioco";
    }

    @GetMapping("/")
    public String showHomePage() {
        return "redirect:/rawg/popolari";
    }
}