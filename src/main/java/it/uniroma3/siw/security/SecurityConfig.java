package it.uniroma3.siw.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize              
                // Permettiamo l'accesso a tutti (loggati e non) alla home, alla lista e al singolo gioco
                .requestMatchers("/", "/videogiochi", "/videogioco/**", "/css/**", "/images/**", "/js/**", "/api/**").permitAll()
                
                // Rotte protette per l'inserimento/modifica dei giochi (solo per ADMIN)
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                
                // Qualsiasi altra richiesta richiede di aver fatto il login (es. aggiungere alla libreria)
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") 
                .defaultSuccessUrl("/") // Dopo il login ti rimanda alla home page
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout") 
                .logoutSuccessUrl("/") // Dopo il logout ti rimanda alla home page
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}