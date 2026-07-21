package it.uniroma3.siw.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Commento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer voto;

    @Column(length = 1000)
    private String testo;

    private LocalDate dataScrittura;

    @ManyToOne
    private Utente autore;

    @ManyToOne
    private Videogioco videogioco;

    public Commento() {
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

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public LocalDate getDataScrittura() {
        return dataScrittura;
    }

    public void setDataScrittura(LocalDate dataScrittura) {
        this.dataScrittura = dataScrittura;
    }

    public Utente getAutore() {
        return autore;
    }

    public void setAutore(Utente autore) {
        this.autore = autore;
    }

    public Videogioco getVideogioco() {
        return videogioco;
    }

    public void setVideogioco(Videogioco videogioco) {
        this.videogioco = videogioco;
    }
}
