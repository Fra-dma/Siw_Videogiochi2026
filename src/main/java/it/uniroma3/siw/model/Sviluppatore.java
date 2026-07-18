package it.uniroma3.siw.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Sviluppatore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @OneToMany(mappedBy = "sviluppatore")
    private List<Videogioco> videogiochi;
    
    public Sviluppatore() {
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