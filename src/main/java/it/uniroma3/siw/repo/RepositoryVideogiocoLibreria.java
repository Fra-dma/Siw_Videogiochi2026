package it.uniroma3.siw.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.VideogiocoLibreria;

public interface RepositoryVideogiocoLibreria extends CrudRepository<VideogiocoLibreria, Long>{
	
	// Trova le recensioni dato l'id del videogioco
    List<VideogiocoLibreria> findByVideogiocoId(Long videogiocoId);

    // Trova le recensioni dato l'id dell'utente
    List<VideogiocoLibreria> findByUtenteId(Long utenteId);
    
}