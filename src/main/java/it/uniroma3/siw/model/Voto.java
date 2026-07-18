package it.uniroma3.siw.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Lob
    private String immagineBase64;

    @Column(length = 1000)
    private String testo;
    
    public Voto() {
    }
    
    public String getImmagineBase64() {
        return immagineBase64;
    }

    public void setImmagineBase64(String immagineBase64) {
        this.immagineBase64 = immagineBase64;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

}