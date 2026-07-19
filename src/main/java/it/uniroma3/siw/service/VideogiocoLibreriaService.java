package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.Videogioco;
import it.uniroma3.siw.model.VideogiocoLibreria;
import it.uniroma3.siw.model.dto.RawgGameDTO;
import it.uniroma3.siw.repo.RepositoryUtente;
import it.uniroma3.siw.repo.RepositoryVideogioco;
import it.uniroma3.siw.repo.RepositoryVideogiocoLibreria;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VideogiocoLibreriaService {

    @Autowired
    private RepositoryVideogiocoLibreria videogiocoLibreriaRepository;
    
    @Autowired
    private RawgApiService rawgApiService;

    @Autowired
    private RepositoryVideogioco videogiocoRepository;

    @Autowired
    private RepositoryUtente utenteRepository;
    
    @Transactional
    public List<Videogioco> ottieniVideogiochiLibreriaUtente(Long idUtente) {
        
        // 1. Recuperiamo tutti i collegamenti Utente-Gioco dal database
        List<VideogiocoLibreria> elementiLibreria = videogiocoLibreriaRepository.findByUtenteId(idUtente);
        
        // 2. Estraiamo solo gli oggetti "Videogioco" dalla libreria e li restituiamo
        return elementiLibreria.stream()
                .map(VideogiocoLibreria::getVideogioco)
                .collect(Collectors.toList());
    }

    @Transactional
    public void aggiungiDaRawgALibreria(Long utenteId, Long rawgId) {
        Utente utente = utenteRepository.findById(utenteId).orElse(null);
        
        // Se l'utente non esiste, fermiamo tutto
        if (utente == null) return; 

        // 1. Cerchiamo se il gioco esiste già nel NOSTRO database
        Videogioco videogioco = videogiocoRepository.findByRawgId(rawgId).orElse(null);

        // 2. Se non esiste, lo "importiamo" da RAWG
        if (videogioco == null) {
            // Sostituisci "getGameById" con il nome esatto del metodo nel tuo RawgApiService
            RawgGameDTO dto = rawgApiService.getGameById(rawgId); 
            
            if (dto != null) {
                videogioco = new Videogioco();
                videogioco.setRawgId(dto.getId());
                videogioco.setTitolo(dto.getName());
                videogioco.setUrlCopertina(dto.getBackground_image());
                
                // Convertiamo la data "YYYY-MM-DD" per prendere solo l'anno (es. "2015")
                if (dto.getReleased() != null && dto.getReleased().length() >= 4) {
                    videogioco.setAnnoUscita(Integer.parseInt(dto.getReleased().substring(0, 4)));
                }
                
                // Salviamo il nuovo gioco nel nostro database!
                videogioco = videogiocoRepository.save(videogioco); 
            } else {
                return; // Se l'API fallisce, ci fermiamo
            }
        }

        // 3. Ora che il gioco esiste localmente, lo aggiungiamo alla libreria (evitando doppioni)
        if (!videogiocoLibreriaRepository.existsByUtenteAndVideogioco(utente, videogioco)) {
            VideogiocoLibreria nuovaAggiunta = new VideogiocoLibreria();
            nuovaAggiunta.setUtente(utente);
            nuovaAggiunta.setVideogioco(videogioco);
            nuovaAggiunta.setDataAggiunta(LocalDate.now());
            
            videogiocoLibreriaRepository.save(nuovaAggiunta);
        }
    }
    
    public List<VideogiocoLibreria> findAll() {
        return (List<VideogiocoLibreria>) videogiocoLibreriaRepository.findAll();
    }

    @Transactional
    public void save(VideogiocoLibreria videogiocoLibreria) {
        if (videogiocoLibreria.getDataAggiunta() == null) {
            videogiocoLibreria.setDataAggiunta(LocalDate.now());
        }
        videogiocoLibreriaRepository.save(videogiocoLibreria);
    }

    public Optional<VideogiocoLibreria> findById(Long id) {
        return videogiocoLibreriaRepository.findById(id);
    }

    public List<VideogiocoLibreria> findByVideogiocoId(Long videogiocoId) {
        return videogiocoLibreriaRepository.findByVideogiocoId(videogiocoId);
    }
    
    public List<VideogiocoLibreria> findByUtenteId(Long utenteId) {
        return videogiocoLibreriaRepository.findByUtenteId(utenteId);
    }

    @Transactional
    public void deleteById(Long id) {
        videogiocoLibreriaRepository.deleteById(id);
    }
}