package it.uniroma3.siw.security;


import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repo.RepositoryUtente;

@Component
public class InitData implements CommandLineRunner {

    private final RepositoryUtente utenteRepository;
    private final PasswordEncoder passwordEncoder;

    public InitData(RepositoryUtente utenteRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        
        if (utenteRepository.findByUsername("mario").isEmpty()) {
            Utente u = new Utente();
            u.setUsername("mario");
            u.setPassword(passwordEncoder.encode("password123"));
            u.setEmail("mario@example.com"); // Obbligatorio
            u.setRuolo("USER");
            
            utenteRepository.save(u);
        }
    }
}