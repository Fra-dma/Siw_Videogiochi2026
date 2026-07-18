package it.uniroma3.siw.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Piattaforma {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ManyToMany(mappedBy = "piattaforme")
    private List<Videogioco> videogiochi;
    
    public Piattaforma() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Videogioco> getVideogiochi() {
		return videogiochi;
	}

	public void setVideogiochi(List<Videogioco> videogiochi) {
		this.videogiochi = videogiochi;
	}

    
}
