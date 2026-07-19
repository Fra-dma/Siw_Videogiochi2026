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
        
        // Creazione utente standard
        if (utenteRepository.findByUsername("mario") == null) {
            Utente u = new Utente();
            u.setUsername("mario");
            u.setPassword(passwordEncoder.encode("password123"));
            u.setRuolo("USER"); // L'utente normale ha ruolo USER
            
            utenteRepository.save(u);
        }

        // Creazione utente amministratore
        if (utenteRepository.findByUsername("admin") == null) {
            Utente admin = new Utente();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRuolo("ADMIN"); // L'admin ha ruolo ADMIN
            
            utenteRepository.save(admin);
        }
    }
}