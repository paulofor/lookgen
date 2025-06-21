package com.lookgen.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

// FotoItem.java
@Entity
@Table(name = "foto_item")
public class FotoItem {

    @Id
    @Column(length = 36)              // CHAR(36)
    private String id;                // ← String, não Long

    @ManyToOne(fetch = FetchType.LAZY, optional = true)   // ← agora pode ser null
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    private CategoriaItem categoria = CategoriaItem.ROUPA;

    @Column(name = "peca_nome")
    private String pecaNome;

    private String cor;
    private String tipo;
    private String url;


    @Column(name = "data_upload",
            nullable = false,
            columnDefinition = "DATETIME")
    private LocalDateTime dataUpload;

    /* -------------------- gerador de UUID -------------------- */
    @PrePersist
    private void preInsert() {
        if (id == null)
            id = UUID.randomUUID().toString();
        if (dataUpload == null)
            dataUpload = LocalDateTime.now();
    }
    /* getters / setters … */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public CategoriaItem getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaItem categoria) {
        this.categoria = categoria;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(LocalDateTime dataUpload) {
        this.dataUpload = dataUpload;
    }

    public String getPecaNome() {
        return pecaNome;
    }

    public void setPecaNome(String pecaNome) {
        this.pecaNome = pecaNome;
    }
}
