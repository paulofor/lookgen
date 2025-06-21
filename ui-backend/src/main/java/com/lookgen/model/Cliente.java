package com.lookgen.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false, length = 20)
    private String whatsapp;

    @Column(nullable = false)
    private Boolean ativo;                 // será setado no @PrePersist

    @Column(name = "data_criacao", nullable = false,
            columnDefinition = "TIMESTAMP")
    private LocalDateTime dataCriacao;     // idem

    /* ---------- Callback para valores padrão ---------- */
    @PrePersist
    private void prePersist() {
        if (ativo == null)          ativo = true;
        if (dataCriacao == null)    dataCriacao = LocalDateTime.now();
    }

    /* ---------- Construtores ---------- */
    public Cliente() {}                       // exigido pelo JPA

    public Cliente(String nome, String email, String whatsapp) {
        this.nome = nome;
        this.email = email;
        this.whatsapp = whatsapp;
    }

    /* ---------- Getters/Setters ---------- */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}
