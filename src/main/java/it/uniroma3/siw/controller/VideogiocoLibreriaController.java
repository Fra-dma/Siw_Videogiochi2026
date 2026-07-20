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
import it.uniroma3.siw.service.VideogiocoLibreriaService;

@Controller
public class VideogiocoLibreriaController {

    private final VideogiocoLibreriaService videogiocoLibreriaService;

    public VideogiocoLibreriaController(VideogiocoLibreriaService videogiocoLibreriaService) {
        this.videogiocoLibreriaService = videogiocoLibreriaService;
    }

    // Questa rotta gestisce il salvataggio di un nuovo gioco nella libreria (Aggiunta Manuale)
    @PostMapping("/libreria/aggiungi")
    public String addVideogiocoLibreria(@ModelAttribute VideogiocoLibreria videogiocoLibreria) {
        videogiocoLibreriaService.save(videogiocoLibreria);
        // Dopo averlo salvato, reindirizziamo l'utente alla pagina del gioco appena aggiunto
        return "redirect:/videogioco/" + videogiocoLibreria.getVideogioco().getId();
    }
    
    // Questa rotta gestisce l'aggiunta di un gioco proveniente dalle API di RAWG
    @PostMapping("/libreria/aggiungiDaRawg")
    public String aggiungiGiocoDaRawg(@RequestParam("rawgId") Long rawgId, @RequestParam("idUtente") Long idUtente) {
        // Salviamo il gioco
        videogiocoLibreriaService.aggiungiDaRawgALibreria(idUtente, rawgId);
        return "redirect:/rawg/gioco/" + rawgId; 
    }
    
 // Modifica la rotta esistente per rimanere sulla pagina del gioco
    @PostMapping("/libreria/recensisci")
    public String recensisciGioco(@RequestParam("videogiocoId") Long videogiocoId, 
                                  @RequestParam("voto") Integer voto, 
                                  @RequestParam("commento") String commento) {
        Long idUtenteAttuale = 1L; 
        videogiocoLibreriaService.aggiornaVotoECommento(idUtenteAttuale, videogiocoId, voto, commento);
        
        // Ora ti reindirizza alla pagina del gioco!
        return "redirect:/videogioco/" + videogiocoId; 
    }

    // Aggiungi questa nuova rotta per eliminare la recensione
    @PostMapping("/libreria/recensione/rimuovi")
    public String rimuoviRecensione(@RequestParam("videogiocoId") Long videogiocoId) {
        Long idUtenteAttuale = 1L; 
        
        // Passando null resettiamo i campi senza eliminare il gioco dalla libreria
        videogiocoLibreriaService.aggiornaVotoECommento(idUtenteAttuale, videogiocoId, null, null);
        
        return "redirect:/videogioco/" + videogiocoId; 
    }
    
    // Questa rotta gestisce la rimozione di un gioco dalla libreria personale
    @PostMapping("/libreria/rimuovi")
    public String rimuoviGiocoDaLibreria(@RequestParam("videogiocoId") Long videogiocoId, @RequestParam("idUtente") Long idUtente) {
        
        // Chiamiamo il service per rimuovere il gioco
        videogiocoLibreriaService.rimuoviDaLibreria(idUtente, videogiocoId);
        
        // Una volta rimosso, reindirizziamo l'utente alla vista generale della libreria
        return "redirect:/libreria"; 
    }
    
    // Questa rotta mostra la pagina della libreria personale (Il tuo nuovo HTML)
    @GetMapping("/libreria")
    public String mostraLibreriaPersonale(Model model) {
        
        // NOTA: Per ora simuliamo che l'utente loggato sia quello con ID 1.
        Long idUtenteAttuale = 1L; 
        
        // Chiamiamo il SERVICE, rispettando l'architettura corretta!
        List<Videogioco> videogiochiDellUtente = videogiocoLibreriaService.ottieniVideogiochiLibreriaUtente(idUtenteAttuale);
                
        // Passiamo la lista al tuo HTML
        model.addAttribute("videogiochi", videogiochiDellUtente);
        
        // Restituisce il file "libreria.html"
        return "libreria"; 
    }   
}