// FotoGerada.java (Entidade)
package com.lookgen.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class FotoGerada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cliente cliente;

    private String promptUsado;
    private String descricaoLook;
    private String urlImagemFinal;
    private LocalDateTime dataGeracao = LocalDateTime.now();

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public String getPromptUsado() { return promptUsado; }
    public void setPromptUsado(String promptUsado) { this.promptUsado = promptUsado; }

    public String getDescricaoLook() { return descricaoLook; }
    public void setDescricaoLook(String descricaoLook) { this.descricaoLook = descricaoLook; }

    public String getUrlImagemFinal() { return urlImagemFinal; }
    public void setUrlImagemFinal(String urlImagemFinal) { this.urlImagemFinal = urlImagemFinal; }

    public LocalDateTime getDataGeracao() { return dataGeracao; }
    public void setDataGeracao(LocalDateTime dataGeracao) { this.dataGeracao = dataGeracao; }
}