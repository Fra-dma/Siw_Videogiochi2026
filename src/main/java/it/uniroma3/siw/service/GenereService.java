package it.uniroma3.siw.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Genere;
import it.uniroma3.siw.repo.RepositoryGenere;

import java.util.List;

@Service
public class GenereService {

    private final RepositoryGenere piattaformaRepository;

    public GenereService(RepositoryGenere piattaformaRepository) {
        this.piattaformaRepository = piattaformaRepository;
    }

    @Transactional
    public void save(Genere piattaforma) {
        piattaformaRepository.save(piattaforma);
    }

    public List<Genere> findAll() {
        return (List<Genere>) piattaformaRepository.findAll();
    }
}
