package edu.ifrs.model;

import java.util.HashSet;
import java.util.Set;

public class Missao {
    private Long id;
    private String titulo;
    private String descricao;
    private Set<Personagem> tripulacao = new HashSet<>();

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public Set<Personagem> getTripulacao() {
        return tripulacao;
    }
    public void setTripulacao(Set<Personagem> tripulacao) {
        this.tripulacao = tripulacao;
    }
}
