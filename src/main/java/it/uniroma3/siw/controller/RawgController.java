package it.uniroma3.siw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import it.uniroma3.siw.service.RawgApiService;
import it.uniroma3.siw.service.UtenteService;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.VideogiocoLibreriaService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Controller
public class RawgController {

    private final RawgApiService rawgApiService;
    private final VideogiocoLibreriaService videogiocoLibreriaService;
    private final UtenteService utenteService;

    public RawgController(RawgApiService rawgApiService, VideogiocoLibreriaService videogiocoLibreriaService, UtenteService utenteService) {
        this.rawgApiService = rawgApiService;
        this.videogiocoLibreriaService = videogiocoLibreriaService;
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

    // Questa rotta restituisce la vista dei giochi popolari con filtri
    @GetMapping("/rawg/popolari")
    public String showPopularGames(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "dlc_filter", required = false) String dlcFilter,
            @RequestParam(value = "ordering", required = false) String ordering,
            Model model) {

        model.addAttribute("search", search);
        model.addAttribute("dlc_filter", dlcFilter != null ? dlcFilter : "all");
        model.addAttribute("ordering", ordering != null ? ordering : "-added");

        model.addAttribute("giochi", rawgApiService.getGamesWithFilters(search, dlcFilter, ordering, 1));

        return "rawg_popolari";
    }

    // Questa rotta mostra i dettagli di un singolo gioco preso da RAWG tramite il
    // suo ID
    @GetMapping("/rawg/gioco/{id}")
    public String showGameDetails(@PathVariable("id") Long id, Model model) {
        // Passiamo i dettagli del gioco all'HTML
        model.addAttribute("gioco", rawgApiService.getGameDetails(id));
        
        Long idUtenteAttuale = getCurrentUserId();
        boolean inLibreria = false;
        if (idUtenteAttuale != null) {
            inLibreria = videogiocoLibreriaService.esisteGiocoDaRawgInLibreria(idUtenteAttuale, id);
        }
        // Passiamo la variabile "isInLibreria" all'HTML
        model.addAttribute("isInLibreria", inLibreria);
        return "rawg_dettagli";
    }
}
