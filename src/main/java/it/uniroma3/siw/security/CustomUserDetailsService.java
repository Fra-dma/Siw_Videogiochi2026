package it.uniroma3.siw.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repo.RepositoryUtente; // Assicurati che il nome coincida

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
   private RepositoryUtente utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));

        return User.builder()
                .username(utente.getUsername())
                .password(utente.getPassword())
                .authorities(utente.getRuolo()) 
                .build();
    }
}