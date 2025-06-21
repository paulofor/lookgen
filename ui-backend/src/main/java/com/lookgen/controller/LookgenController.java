package com.lookgen.controller;

import com.lookgen.dto.FormularioLookgenDTO;
import com.lookgen.model.*;
import com.lookgen.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
@RestController
@RequestMapping("/api")
public class LookgenController {

    private final ClienteRepository    clienteRepo;
    private final EstiloRepository     estiloRepo;
    private final FotoPessoaRepository fotoPessoaRepo;
    private final FotoItemRepository   fotoItemRepo;

    public LookgenController(ClienteRepository  clienteRepo,
                             EstiloRepository   estiloRepo,
                             FotoPessoaRepository fotoPessoaRepo,
                             FotoItemRepository   fotoItemRepo) {
        this.clienteRepo    = clienteRepo;
        this.estiloRepo     = estiloRepo;
        this.fotoPessoaRepo = fotoPessoaRepo;
        this.fotoItemRepo   = fotoItemRepo;
    }

    /* -----------------------------------------------------------
       /api/inscricao  —  front envia JSON (application/json)
    ----------------------------------------------------------- */
    @PostMapping(value = "/inscricao", consumes = "application/json")
    public ResponseEntity<?> receberInscricao(@RequestBody FormularioLookgenDTO dados) {

        /* 1. Cliente ------------------------------------------ */
        Cliente cliente = clienteRepo.save(
                new Cliente(dados.getNome(), dados.getEmail(), dados.getWhatsapp())
        );

        /* 2. Estilo ------------------------------------------- */
        Estilo estilo = new Estilo(dados.getEstilo(), dados.getEstilo(), cliente);
        estiloRepo.save(estilo);

        /* 3. Foto da pessoa (opcional) ------------------------ */
        if (dados.getFotoPessoa() != null) {
            FotoPessoa fp = fotoPessoaRepo.findById(dados.getFotoPessoa())
                    .orElseThrow();          // 404 se inexistente
            fp.setCliente(cliente);
            fotoPessoaRepo.save(fp);
        }

        /* 4. Fotos das roupas (5 UUIDs) ----------------------- */
        for (int i = 0; i < dados.getFotosRoupas().size(); i++) {
            String uuid = dados.getFotosRoupas().get(i);
            FotoItem fi = fotoItemRepo.findById(uuid).orElseThrow();
            fi.setCliente(cliente);
            fi.setPecaNome("Peça " + (i + 1));
            fotoItemRepo.save(fi);
        }

        return ResponseEntity.status(201).body("Inscrição recebida com sucesso!");
    }
}
