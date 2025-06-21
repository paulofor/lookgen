// src/main/java/com/lookgen/model/Estilo.java
package com.lookgen.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "estilo")
public class Estilo {

    /* ---------- Campos ---------- */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;

    /** FK → cliente.id */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;           // relacionamento N:1

    /* ---------- Construtores ---------- */
    public Estilo() { }                // JPA precisa do construtor vazio

    public Estilo(String nome, String descricao, Cliente cliente) {
        this.nome       = nome;
        this.descricao  = descricao;
        this.cliente    = cliente;
    }

    /* ---------- Getters / Setters ---------- */
    public Long getId()                           { return id; }
    public void setId(Long id)                    { this.id = id; }

    public String getNome()                       { return nome; }
    public void setNome(String nome)              { this.nome = nome; }

    public String getDescricao()                  { return descricao; }
    public void setDescricao(String descricao)    { this.descricao = descricao; }

    public Cliente getCliente()                   { return cliente; }
    public void setCliente(Cliente cliente)       { this.cliente = cliente; }

    /* ---------- equals / hashCode ---------- */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Estilo other)) return false;
        // considera iguais se o id não-nulo for igual
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /* ---------- toString (opcional, sem LAZY) ---------- */
    @Override
    public String toString() {
        return "Estilo{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", clienteId=" + (cliente != null ? cliente.getId() : null) +
                '}';
    }
}
