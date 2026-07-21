package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.Videogioco;
import it.uniroma3.siw.model.VideogiocoLibreria;
import it.uniroma3.siw.service.UtenteService;
import it.uniroma3.siw.service.VideogiocoLibreriaService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Controller
public class VideogiocoLibreriaController {

    private final VideogiocoLibreriaService videogiocoLibreriaService;
    private final UtenteService utenteService;

    public VideogiocoLibreriaController(VideogiocoLibreriaService videogiocoLibreriaService,
            UtenteService utenteService) {
        this.videogiocoLibreriaService = videogiocoLibreriaService;
        this.utenteService = utenteService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
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
            if (username == null)
                username = authentication.getName();
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
        Long idUtenteAttuale = getCurrentUserId();
        // Salviamo il gioco
        videogiocoLibreriaService.aggiungiDaRawgALibreria(idUtenteAttuale, rawgId);
        return "redirect:/rawg/gioco/" + rawgId;
    }

    // Modifica la rotta esistente per rimanere sulla pagina del gioco
    @PostMapping("/libreria/recensisci")
    public String recensisciGioco(@RequestParam("videogiocoId") Long videogiocoId,
            @RequestParam("voto") Integer voto,
            @RequestParam("commento") String commento) {
        Long idUtenteAttuale = getCurrentUserId();
        videogiocoLibreriaService.aggiornaVotoECommento(idUtenteAttuale, videogiocoId, voto, commento);

        // Ora ti reindirizza alla pagina del gioco!
        return "redirect:/videogioco/" + videogiocoId;
    }

    // Aggiungi questa nuova rotta per eliminare la recensione
    @PostMapping("/libreria/recensione/rimuovi")
    public String rimuoviRecensione(@RequestParam("videogiocoId") Long videogiocoId) {
        Long idUtenteAttuale = getCurrentUserId();

        // Passando null resettiamo i campi senza eliminare il gioco dalla libreria
        videogiocoLibreriaService.aggiornaVotoECommento(idUtenteAttuale, videogiocoId, null, null);

        return "redirect:/videogioco/" + videogiocoId;
    }

    // Questa rotta gestisce la rimozione di un gioco dalla libreria personale
    @PostMapping("/libreria/rimuovi")
    public String rimuoviGiocoDaLibreria(@RequestParam("videogiocoId") Long videogiocoId,
            @RequestParam(value = "idUtente", required = false) Long idUtente) {

        Long idUtenteAttuale = getCurrentUserId();
        // Chiamiamo il service per rimuovere il gioco
        videogiocoLibreriaService.rimuoviDaLibreria(idUtenteAttuale, videogiocoId);

        // Una volta rimosso, reindirizziamo l'utente alla vista generale della libreria
        return "redirect:/libreria";
    }

    // Questa rotta mostra la pagina della libreria personale (Il tuo nuovo HTML)
    @GetMapping("/libreria")
    public String mostraLibreriaPersonale(Model model) {

        Long idUtenteAttuale = getCurrentUserId();

        // Chiamiamo il SERVICE, rispettando l'architettura corretta!
        List<Videogioco> videogiochiDellUtente = videogiocoLibreriaService
                .ottieniVideogiochiLibreriaUtente(idUtenteAttuale);

        // Passiamo la lista al tuo HTML
        model.addAttribute("videogiochi", videogiochiDellUtente);

        // Restituisce il file "libreria.html"
        return "libreria";
    }
}