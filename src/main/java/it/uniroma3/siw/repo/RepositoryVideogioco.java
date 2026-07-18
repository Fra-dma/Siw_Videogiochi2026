package it.uniroma3.siw.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Videogioco;

public interface RepositoryVideogioco extends CrudRepository<Videogioco, Long>{
	
	@Query("SELECT v FROM Videogioco v LEFT JOIN FETCH v.recensioni WHERE v.id = :id")
    Optional<Videogioco> findByIdWithRecensioni(@Param("id") Long id);
	
}
