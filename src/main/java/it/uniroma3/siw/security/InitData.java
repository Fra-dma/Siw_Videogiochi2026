package it.uniroma3.siw.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repo.RepositoryUtente;

@Component
public class InitData implements CommandLineRunner {

    @Autowired
    private RepositoryUtente utenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        // Creazione di un singolo utente di test, se non esiste già
        if (utenteRepository.findByUsername("mario").isEmpty()) {
            Utente u = new Utente();
            u.setUsername("mario");
            u.setPassword(passwordEncoder.encode("password123"));
            u.setEmail("mario@example.com"); // Obbligatorio perché nel model hai nullable=false
            u.setRuolo("USER"); // Requisito standard di Spring Security
            
            utenteRepository.save(u);
        }
    }
}