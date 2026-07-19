package it.uniroma3.siw.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Videogioco {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private Long rawgId;

    @Column(nullable = false)
    private String titolo;

    @Column(length = 2000)
    private String descrizione;
    
    private Integer annoUscita;
    
    private String urlCopertina;

    @ManyToOne
    private Sviluppatore sviluppatore;

    @OneToMany(mappedBy = "videogioco", cascade = CascadeType.ALL)
    private List<VideogiocoLibreria> videogiocoLibreria;

    @ManyToMany
    private List<Genere> generi;

    @ManyToMany
    private List<Piattaforma> piattaforme;
    
    public Videogioco() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRawgId() {
		return rawgId;
	}

	public void setRawgId(Long rawgId) {
		this.rawgId = rawgId;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Integer getAnnoUscita() {
		return annoUscita;
	}

	public void setAnnoUscita(Integer annoUscita) {
		this.annoUscita = annoUscita;
	}

	public String getUrlCopertina() {
		return urlCopertina;
	}

	public void setUrlCopertina(String urlCopertina) {
		this.urlCopertina = urlCopertina;
	}

	public Sviluppatore getSviluppatore() {
		return sviluppatore;
	}

	public void setSviluppatore(Sviluppatore sviluppatore) {
		this.sviluppatore = sviluppatore;
	}

	public List<VideogiocoLibreria> getRecensioni() {
		return videogiocoLibreria;
	}

	public void setRecensioni(List<VideogiocoLibreria> videogiocoLibreria) {
		this.videogiocoLibreria = videogiocoLibreria;
	}

	public List<Genere> getGeneri() {
		return generi;
	}

	public void setGeneri(List<Genere> generi) {
		this.generi = generi;
	}

	public List<Piattaforma> getPiattaforme() {
		return piattaforme;
	}

	public void setPiattaforme(List<Piattaforma> piattaforme) {
		this.piattaforme = piattaforme;
	}

}