package it.uniroma3.siw.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Recensione {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Integer voto; 

    @Column(length = 1000)
    private String commento;

    private String statoGiocata;
    
    private LocalDate dataRecensione;

    @ManyToOne
    private Utente utente;

    @ManyToOne
    private Videogioco videogioco;
    
    public Recensione() {
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

	public String getStatoGiocata() {
		return statoGiocata;
	}

	public void setStatoGiocata(String statoGiocata) {
		this.statoGiocata = statoGiocata;
	}

	public LocalDate getDataRecensione() {
		return dataRecensione;
	}

	public void setDataRecensione(LocalDate dataRecensione) {
		this.dataRecensione = dataRecensione;
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