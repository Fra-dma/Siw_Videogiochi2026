package it.uniroma3.siw.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Piattaforma;
import it.uniroma3.siw.repo.RepositoryPiattaforma;

import java.util.List;

@Service
public class PiattaformaService {

    private final RepositoryPiattaforma piattaformaRepository;

    public PiattaformaService(RepositoryPiattaforma piattaformaRepository) {
        this.piattaformaRepository = piattaformaRepository;
    }

    @Transactional
    public void save(Piattaforma piattaforma) {
        piattaformaRepository.save(piattaforma);
    }

    public List<Piattaforma> findAll() {
        return (List<Piattaforma>) piattaformaRepository.findAll();
    }
}
