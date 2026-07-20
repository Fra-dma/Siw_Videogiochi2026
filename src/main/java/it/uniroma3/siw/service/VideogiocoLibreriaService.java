package it.uniroma3.siw.service;


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

    private final RepositoryVideogiocoLibreria videogiocoLibreriaRepository;
    private final RawgApiService rawgApiService;
    private final RepositoryVideogioco videogiocoRepository;
    private final RepositoryUtente utenteRepository;

    public VideogiocoLibreriaService(RepositoryVideogiocoLibreria videogiocoLibreriaRepository,
                                     RawgApiService rawgApiService,
                                     RepositoryVideogioco videogiocoRepository,
                                     RepositoryUtente utenteRepository) {
        this.videogiocoLibreriaRepository = videogiocoLibreriaRepository;
        this.rawgApiService = rawgApiService;
        this.videogiocoRepository = videogiocoRepository;
        this.utenteRepository = utenteRepository;
    }
    
    public boolean esisteGiocoDaRawgInLibreria(Long idUtente, Long rawgId) {
        Utente utente = utenteRepository.findById(idUtente).orElse(null);
        Videogioco videogioco = videogiocoRepository.findByRawgId(rawgId).orElse(null);
        
        if (utente != null && videogioco != null) {
            return videogiocoLibreriaRepository.existsByUtenteAndVideogioco(utente, videogioco);
        }
        return false; // Se non c'è l'utente o il gioco non è mai stato salvato, restituisce falso
    }
    
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
        
        // Se l'utente non c'è, interrompiamo l'operazione
        if (utente == null) {
            return; 
        }

        Videogioco videogioco = videogiocoRepository.findByRawgId(rawgId).orElse(null);

        // Se il gioco non esiste nel database locale, lo scarichiamo da RAWG
        if (videogioco == null) {
            
            // NOTA: Assicurati che "getGameDetails" sia il nome corretto del metodo nel tuo RawgApiService
            RawgGameDTO dto = rawgApiService.getGameDetails(rawgId); 
            
            if (dto != null) {
                videogioco = new Videogioco();
                videogioco.setRawgId(dto.getId());
                videogioco.setTitolo(dto.getName());
                videogioco.setUrlCopertina(dto.getBackground_image());
                
                // Estraiamo solo l'anno dalla data di rilascio
                if (dto.getReleased() != null && dto.getReleased().length() >= 4) {
                    videogioco.setAnnoUscita(Integer.parseInt(dto.getReleased().substring(0, 4)));
                }
                
                // Salviamo il nuovo gioco nel database locale
                videogioco = videogiocoRepository.save(videogioco); 
            } else {
                // Se l'API non restituisce dati validi, interrompiamo l'operazione
                return;
            }
        }

        // Se il gioco non è già presente nella libreria dell'utente, lo aggiungiamo
        if (!videogiocoLibreriaRepository.existsByUtenteAndVideogioco(utente, videogioco)) {
            VideogiocoLibreria nuovaAggiunta = new VideogiocoLibreria();
            nuovaAggiunta.setUtente(utente);
            nuovaAggiunta.setVideogioco(videogioco);
            nuovaAggiunta.setDataAggiunta(LocalDate.now());
            
            videogiocoLibreriaRepository.save(nuovaAggiunta);
        }
    }
    
    @Transactional
    public void rimuoviDaLibreria(Long idUtente, Long videogiocoId) {
        Utente utente = utenteRepository.findById(idUtente).orElse(null);
        Videogioco videogioco = videogiocoRepository.findById(videogiocoId).orElse(null);
        
        if (utente != null && videogioco != null) {
            // Cerchiamo il collegamento esatto nella libreria
            VideogiocoLibreria collegamento = videogiocoLibreriaRepository.findByUtenteAndVideogioco(utente, videogioco).orElse(null);
            
            // Se esiste, lo eliminiamo
            if (collegamento != null) {
                videogiocoLibreriaRepository.delete(collegamento);
            }
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
    
    @Transactional
    public void aggiornaVotoECommento(Long idUtente, Long videogiocoId, Integer voto, String commento) {
        Utente utente = utenteRepository.findById(idUtente).orElse(null);
        Videogioco videogioco = videogiocoRepository.findById(videogiocoId).orElse(null);
        
        if (utente != null && videogioco != null) {
            VideogiocoLibreria libreriaEntry = videogiocoLibreriaRepository
                    .findByUtenteAndVideogioco(utente, videogioco)
                    .orElse(null);
            
            if (libreriaEntry != null) {
                libreriaEntry.setVoto(voto);
                libreriaEntry.setCommento(commento);
                videogiocoLibreriaRepository.save(libreriaEntry);
            }
        }
    }
}