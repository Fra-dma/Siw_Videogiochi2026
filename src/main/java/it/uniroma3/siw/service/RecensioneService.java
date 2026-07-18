package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Piattaforma;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.repo.RepositoryRecensione;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RecensioneService {

    @Autowired
    private RepositoryRecensione recensioneRepository;
    
    public List<Recensione> findAll() {
        return (List<Recensione>) recensioneRepository.findAll();
    }

    @Transactional
    public void save(Recensione recensione) {
        if (recensione.getDataRecensione() == null) {
            recensione.setDataRecensione(LocalDate.now());
        }
        recensioneRepository.save(recensione);
    }

    public Optional<Recensione> findById(Long id) {
        return recensioneRepository.findById(id);
    }

    public List<Recensione> findByVideogiocoId(Long videogiocoId) {
        return recensioneRepository.findByVideogiocoId(videogiocoId);
    }
    
    public List<Recensione> findByUtenteId(Long utenteId) {
        return recensioneRepository.findByUtenteId(utenteId);
    }

    @Transactional
    public void deleteById(Long id) {
        recensioneRepository.deleteById(id);
    }
}