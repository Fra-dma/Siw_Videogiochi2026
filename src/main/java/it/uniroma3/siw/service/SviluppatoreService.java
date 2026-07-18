package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Sviluppatore;
import it.uniroma3.siw.repo.RepositorySviluppatore;

import java.util.List;
import java.util.Optional;

@Service
public class SviluppatoreService {

    @Autowired
    private RepositorySviluppatore repositorySviluppatore;

    @Transactional
    public void save(Sviluppatore sviluppatore) {
        repositorySviluppatore.save(sviluppatore);
    }

    public List<Sviluppatore> findAll() {
        return (List<Sviluppatore>) repositorySviluppatore.findAll();
    }
    
    public Optional<Sviluppatore> findById(Long id) {
        return repositorySviluppatore.findById(id);
    }
}

