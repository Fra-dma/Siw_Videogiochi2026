package it.uniroma3.siw.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class VideogiocoLibreria {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Integer voto; 

    @Column(length = 1000)
    private String commento;
    
    private LocalDate dataAggiunta;

    @ManyToOne
    private Utente utente;

    @ManyToOne
    private Videogioco videogioco;
    
    public VideogiocoLibreria() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVoto() {
        return voto;
    }

    public void setVoto(Integer voto) {
        this.voto = voto;
    }

    public String getCommento() {
        return commento;
    }

    public void setCommento(String commento) {
        this.commento = commento;
    }

    public LocalDate getDataAggiunta() {
        return dataAggiunta;
    }

    public void setDataAggiunta(LocalDate dataAggiunta) {
        this.dataAggiunta = dataAggiunta;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Videogioco getVideogioco() {
        return videogioco;
    }

    public void setVideogioco(Videogioco videogioco) {
        this.videogioco = videogioco;
    }
}