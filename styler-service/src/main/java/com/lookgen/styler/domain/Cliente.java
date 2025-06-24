package com.lookgen.styler.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    @Column(name = "precisa_processamento")
    private boolean precisaProcessamento = true;

    public Cliente() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isPrecisaProcessamento() {
        return precisaProcessamento;
    }

    public void setPrecisaProcessamento(boolean precisaProcessamento) {
        this.precisaProcessamento = precisaProcessamento;
    }
}
