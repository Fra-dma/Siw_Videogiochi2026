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

    /**
     * Recupera una lista dei giochi più popolari (o aggiunti di recente).
     */
    public List<RawgGameDTO> getPopularGames() {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/games")
                .queryParam("key", apiKey)
                .queryParam("ordering", "-added") // Ordina per numero di persone che lo hanno aggiunto
                .queryParam("page_size", 20)      // Numero di risultati per pagina
                .toUriString();

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
