package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Videogioco;
import it.uniroma3.siw.model.VideogiocoLibreria;
import it.uniroma3.siw.service.UtenteService;
import it.uniroma3.siw.service.VideogiocoLibreriaService;
import it.uniroma3.siw.service.CommentoService;

@Controller
public class VideogiocoLibreriaController {

    private final VideogiocoLibreriaService videogiocoLibreriaService;
    private final UtenteService utenteService;
    private final CommentoService commentoService;

    public VideogiocoLibreriaController(VideogiocoLibreriaService videogiocoLibreriaService,
            UtenteService utenteService,
            CommentoService commentoService) {
        this.videogiocoLibreriaService = videogiocoLibreriaService;
        this.utenteService = utenteService;
        this.commentoService = commentoService;
    }

    // Questa rotta gestisce il salvataggio di un nuovo gioco nella libreria
    // (Aggiunta Manuale)
    @PostMapping("/libreria/aggiungi")
    public String addVideogiocoLibreria(@ModelAttribute VideogiocoLibreria videogiocoLibreria) {
        videogiocoLibreriaService.save(videogiocoLibreria);
        // Dopo averlo salvato, reindirizziamo l'utente alla pagina del gioco appena
        // aggiunto
        return "redirect:/videogioco/" + videogiocoLibreria.getVideogioco().getId();
    }

    // Questa rotta gestisce l'aggiunta di un gioco proveniente dalle API di RAWG
    @PostMapping("/libreria/aggiungiDaRawg")
    public String aggiungiGiocoDaRawg(@RequestParam("rawgId") Long rawgId,
            @RequestParam(value = "idUtente", required = false) Long idUtente) {
        Long idUtenteAttuale = utenteService.getCurrentUserId();
        // Salviamo il gioco
        videogiocoLibreriaService.aggiungiDaRawgALibreria(idUtenteAttuale, rawgId);
        return "redirect:/rawg/gioco/" + rawgId;
    }

    // Modifica la rotta esistente per rimanere sulla pagina del gioco
    @PostMapping("/libreria/recensisci")
    public String recensisciGioco(@RequestParam("videogiocoId") Long videogiocoId,
            @RequestParam("voto") Integer voto,
            @RequestParam("commento") String commento) {
        Long idUtenteAttuale = utenteService.getCurrentUserId();
        commentoService.aggiornaVotoECommento(idUtenteAttuale, videogiocoId, voto, commento);

        // Ora ti reindirizza alla pagina del gioco!
        return "redirect:/videogioco/" + videogiocoId;
    }

    // Aggiungi questa nuova rotta per eliminare la recensione
    @PostMapping("/libreria/recensione/rimuovi")
    public String rimuoviRecensione(@RequestParam("videogiocoId") Long videogiocoId) {
        Long idUtenteAttuale = utenteService.getCurrentUserId();

        commentoService.rimuoviCommento(idUtenteAttuale, videogiocoId);

        return "redirect:/videogioco/" + videogiocoId;
    }

    // Questa rotta gestisce la rimozione di un gioco dalla libreria personale
    @PostMapping("/libreria/rimuovi")
    public String rimuoviGiocoDaLibreria(@RequestParam("videogiocoId") Long videogiocoId,
            @RequestParam(value = "idUtente", required = false) Long idUtente) {

        Long idUtenteAttuale = utenteService.getCurrentUserId();
        // Chiamiamo il service per rimuovere il gioco
        videogiocoLibreriaService.rimuoviDaLibreria(idUtenteAttuale, videogiocoId);

        // Una volta rimosso, reindirizziamo l'utente alla vista generale della libreria
        return "redirect:/libreria";
    }

    // Questa rotta mostra la pagina della libreria personale (Il tuo nuovo HTML)
    @GetMapping("/libreria")
    public String mostraLibreriaPersonale(Model model) {

        Long idUtenteAttuale = utenteService.getCurrentUserId();

        // Chiamiamo il SERVICE, rispettando l'architettura corretta!
        List<Videogioco> videogiochiDellUtente = videogiocoLibreriaService
                .ottieniVideogiochiLibreriaUtente(idUtenteAttuale);

        // Passiamo la lista al tuo HTML
        model.addAttribute("videogiochi", videogiochiDellUtente);

        // Restituisce il file "libreria.html"
        return "libreria";
    }
}