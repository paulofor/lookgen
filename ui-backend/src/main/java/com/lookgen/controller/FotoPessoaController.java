package com.lookgen.controller;

import com.lookgen.model.FotoPessoa;
import com.lookgen.service.FotoPessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fotos-pessoa")
public class FotoPessoaController {

    @Autowired
    private FotoPessoaService fotoPessoaService;

    @PostMapping
    public ResponseEntity<FotoPessoa> salvar(@RequestBody FotoPessoa fotoPessoa) {
        FotoPessoa salvo = fotoPessoaService.salvar(fotoPessoa);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<FotoPessoa> listarPorCliente(@PathVariable Long clienteId) {
        return fotoPessoaService.listarPorCliente(clienteId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FotoPessoa> buscarPorId(@PathVariable String id) {
        Optional<FotoPessoa> foto = fotoPessoaService.buscarPorId(id);
        return foto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        fotoPessoaService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
