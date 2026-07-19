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
<<<<<<< HEAD
                // Accesso libero a tutti (non loggati) per home, lista giochi, e file statici
                .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/api/**", "/rawg/**").permitAll()
                
                // Qualsiasi altra azione (es. aggiungere alla libreria) richiede SOLO di essere loggati
=======
            		.requestMatchers("/", "/videogioco/**", "/css/**", "/images/**", "/js/**", "/api/**", "/error", "/rawg/popolari", "/rawg/gioco/**").permitAll()
>>>>>>> ec1be612c5acd3695e4f8c3c11a5f893fbc72da6
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") 
                .defaultSuccessUrl("/") 
                .permitAll()
            )
            // --- NUOVO BLOCCO OAUTH2 ---
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login") // Usa la stessa pagina del login normale
                .defaultSuccessUrl("/") // Dove andare dopo il login con Google
            )
            // ---------------------------
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    String refererUrl = request.getHeader("Referer");
                    if (refererUrl != null) {
                        response.sendRedirect(refererUrl);
                    } else {
                        response.sendRedirect("/");
                    }
                })
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}