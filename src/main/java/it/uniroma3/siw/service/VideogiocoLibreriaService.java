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
    
    public boolean esisteGiocoDaRawgInLibreria(Long idUtente, Long rawgId) {
        Utente utente = utenteRepository.findById(idUtente).orElse(null);
        Videogioco videogioco = videogiocoRepository.findByRawgId(rawgId).orElse(null);
        
        if (utente != null && videogioco != null) {
            return videogiocoLibreriaRepository.existsByUtenteAndVideogioco(utente, videogioco);
        }
        return false;
    }
    
    @Transactional
    public List<Videogioco> ottieniVideogiochiLibreriaUtente(Long idUtente) {
        
        List<VideogiocoLibreria> elementiLibreria = videogiocoLibreriaRepository.findByUtenteId(idUtente);
        
        return elementiLibreria.stream()
                .map(VideogiocoLibreria::getVideogioco)
                .collect(Collectors.toList());
    }

    @Transactional
    public void aggiungiDaRawgALibreria(Long utenteId, Long rawgId) {
        
        Utente utente = utenteRepository.findById(utenteId).orElse(null);
        
        if (utente == null) {
            return; 
        }

        Videogioco videogioco = videogiocoRepository.findByRawgId(rawgId).orElse(null);

        if (videogioco == null) {

            RawgGameDTO dto = rawgApiService.getGameDetails(rawgId); 
            
            if (dto != null) {
                videogioco = new Videogioco();
                videogioco.setRawgId(dto.getId());
                videogioco.setTitolo(dto.getName());
                videogioco.setUrlCopertina(dto.getBackground_image());
                
                if (dto.getReleased() != null && dto.getReleased().length() >= 4) {
                    videogioco.setAnnoUscita(Integer.parseInt(dto.getReleased().substring(0, 4)));
                }
                
                videogioco = videogiocoRepository.save(videogioco); 
            } else {
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
            VideogiocoLibreria collegamento = videogiocoLibreriaRepository.findByUtenteAndVideogioco(utente, videogioco).orElse(null);
            
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