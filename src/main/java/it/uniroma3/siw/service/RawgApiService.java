package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import it.uniroma3.siw.model.dto.RawgGameDTO;
import it.uniroma3.siw.model.dto.RawgResponseDTO;

import java.util.Collections;
import java.util.List;

@Service
public class RawgApiService {

    @Value("${rawg.api.key}")
    private String apiKey;

    @Value("${rawg.api.base-url}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;
    
public RawgGameDTO getGameById(Long rawgId) {
        
        // Creiamo lo strumento di Spring Boot per fare richieste su Internet
        RestTemplate restTemplate = new RestTemplate();

        // Costruiamo l'indirizzo finale da chiamare
        String url = baseUrl + rawgId + "?key=" + apiKey;

        try {
            // Facciamo la richiesta HTTP GET. 
            RawgGameDTO giocoTrovato = restTemplate.getForObject(url, RawgGameDTO.class);
            return giocoTrovato;
            
        } catch (Exception e) {
            // Se RAWG ci dà un errore (es. gioco non trovato o chiave API errata), lo catturiamo qui
            System.out.println("Attenzione! Errore durante il recupero del gioco da RAWG: " + e.getMessage());
            return null;
        }
    }

    /**
     * Recupera una lista dei giochi più popolari (o aggiunti di recente).
     */
    public List<RawgGameDTO> getPopularGames() {
        return fetchGamesList(UriComponentsBuilder.fromUriString(baseUrl)
                .path("/games")
                .queryParam("key", apiKey)
                .queryParam("ordering", "-added")
                .queryParam("page_size", 20)
                .toUriString());
    }

    /**
     * Cerca giochi tramite query testuale.
     */
    public List<RawgGameDTO> searchGames(String query) {
        return fetchGamesList(UriComponentsBuilder.fromUriString(baseUrl)
                .path("/games")
                .queryParam("key", apiKey)
                .queryParam("search", query)
                .queryParam("page_size", 20)
                .toUriString());
    }

    private List<RawgGameDTO> fetchGamesList(String url) {
        try {
            RawgResponseDTO response = restTemplate.getForObject(url, RawgResponseDTO.class);
            if (response != null && response.getResults() != null) {
                return response.getResults();
            }
        } catch (Exception e) {
            System.err.println("Errore durante la comunicazione con RAWG: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * Recupera i dettagli completi di un singolo gioco tramite il suo ID di RAWG.
     */
    public RawgGameDTO getGameDetails(Long rawgId) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/games/" + rawgId)
                .queryParam("key", apiKey)
                .toUriString();

        try {
            return restTemplate.getForObject(url, RawgGameDTO.class);
        } catch (Exception e) {
            System.err.println("Errore durante il recupero dei dettagli del gioco: " + e.getMessage());
            return null;
        }
    }
}
