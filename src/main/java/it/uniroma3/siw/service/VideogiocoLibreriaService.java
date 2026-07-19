package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.VideogiocoLibreria;
import it.uniroma3.siw.repo.RepositoryVideogiocoLibreria;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VideogiocoLibreriaService {

    @Autowired
    private RepositoryVideogiocoLibreria videogiocoLibreriaRepository;
    
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