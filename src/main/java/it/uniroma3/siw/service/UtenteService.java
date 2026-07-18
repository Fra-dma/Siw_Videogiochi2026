package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repo.RepositoryUtente;

import java.util.List;
import java.util.Optional;

@Service
public class UtenteService {

    @Autowired
    private RepositoryUtente utenteRepository;

    @Transactional
    public void save(Utente utente) {
        utenteRepository.save(utente);
    }

    public Optional<Utente> findById(Long id) {
        return utenteRepository.findById(id);
    }
    
    public Optional<Utente> findByUsername(String username) {
        return utenteRepository.findByUsername(username);
    }

    public List<Utente> findAll() {
        return (List<Utente>) utenteRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        utenteRepository.deleteById(id);
    }
}
