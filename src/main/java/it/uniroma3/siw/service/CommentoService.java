package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.Videogioco;
import it.uniroma3.siw.repo.RepositoryCommento;
import it.uniroma3.siw.repo.RepositoryUtente;
import it.uniroma3.siw.repo.RepositoryVideogioco;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CommentoService {

    private final RepositoryCommento commentoRepository;
    private final RepositoryUtente utenteRepository;
    private final RepositoryVideogioco videogiocoRepository;

    public CommentoService(RepositoryCommento commentoRepository,
                           RepositoryUtente utenteRepository,
                           RepositoryVideogioco videogiocoRepository) {
        this.commentoRepository = commentoRepository;
        this.utenteRepository = utenteRepository;
        this.videogiocoRepository = videogiocoRepository;
    }

    public Commento findRecensione(Utente autore, Videogioco videogioco) {
        return commentoRepository.findByAutoreAndVideogioco(autore, videogioco).orElse(null);
    }

    public List<Commento> findRecensioniByVideogioco(Videogioco videogioco) {
        return commentoRepository.findByVideogioco(videogioco);
    }

    @Transactional
    public void aggiornaVotoECommento(Long idUtente, Long videogiocoId, Integer voto, String testo) {
        Utente utente = utenteRepository.findById(idUtente).orElse(null);
        Videogioco videogioco = videogiocoRepository.findById(videogiocoId).orElse(null);
        
        if (utente != null && videogioco != null) {
            Commento commento = commentoRepository.findByAutoreAndVideogioco(utente, videogioco).orElse(new Commento());
            
            commento.setAutore(utente);
            commento.setVideogioco(videogioco);
            commento.setVoto(voto);
            commento.setTesto(testo);
            
            if (commento.getDataScrittura() == null) {
                commento.setDataScrittura(LocalDate.now());
            }
            
            commentoRepository.save(commento);
        }
    }
    
    @Transactional
    public void rimuoviCommento(Long idUtente, Long videogiocoId) {
        Utente utente = utenteRepository.findById(idUtente).orElse(null);
        Videogioco videogioco = videogiocoRepository.findById(videogiocoId).orElse(null);
        
        if (utente != null && videogioco != null) {
            Commento commento = commentoRepository.findByAutoreAndVideogioco(utente, videogioco).orElse(null);
            if (commento != null) {
                commentoRepository.delete(commento);
            }
        }
    }
}
