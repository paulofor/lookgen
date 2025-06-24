package com.lookgen.styler.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "foto_item")
public class FotoItem {
    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "cliente_id")
    private Long clienteId;

    private String url;

    @Column(name = "data_upload")
    private LocalDateTime dataUpload;

    public FotoItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
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
}
