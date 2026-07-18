package it.uniroma3.siw.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Recensione;

public interface RepositoryRecensione extends CrudRepository<Recensione, Long>{
	
	// Trova le recensioni dato l'id del videogioco
    List<Recensione> findByVideogiocoId(Long videogiocoId);

    // Trova le recensioni dato l'id dell'utente
    List<Recensione> findByUtenteId(Long utenteId);
    
}