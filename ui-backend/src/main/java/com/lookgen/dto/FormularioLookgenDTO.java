// src/main/java/com/lookgen/dto/FormularioLookgenDTO.java
package com.lookgen.dto;

import java.util.List;

public class FormularioLookgenDTO {

    private String nome;
    private String email;
    private String whatsapp;
    private String estilo;

    private String        fotoPessoa;   // UUID ou null
    private List<String>  fotosRoupas;  // 5 UUIDs

    /* getters e setters */

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public String getFotoPessoa() {
        return fotoPessoa;
    }

    public void setFotoPessoa(String fotoPessoa) {
        this.fotoPessoa = fotoPessoa;
    }

    public List<String> getFotosRoupas() {
        return fotosRoupas;
    }

    public void setFotosRoupas(List<String> fotosRoupas) {
        this.fotosRoupas = fotosRoupas;
    }
}
