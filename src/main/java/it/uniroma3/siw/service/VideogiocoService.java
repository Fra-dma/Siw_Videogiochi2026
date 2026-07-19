package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Videogioco;
import it.uniroma3.siw.repo.RepositoryVideogioco;

import java.util.List;
import java.util.Optional;

@Service
public class VideogiocoService {

    @Autowired
    private RepositoryVideogioco videogiocoRepository;

    @Transactional
    public void save(Videogioco videogioco) {
        videogiocoRepository.save(videogioco);
    }

    public Optional<Videogioco> findById(Long id) {
        return videogiocoRepository.findById(id);
    }

    public List<Videogioco> findAll() {
        return (List<Videogioco>) videogiocoRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        videogiocoRepository.deleteById(id);
    }

    public Optional<Videogioco> findByIdConRecensioni(Long id) {
        return videogiocoRepository.findByIdWithVideogiocoLibreria(id); 
    }
}
