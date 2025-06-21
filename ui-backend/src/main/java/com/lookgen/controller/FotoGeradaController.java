// FotoGeradaController.java
package com.lookgen.controller;

import com.lookgen.model.FotoGerada;
import com.lookgen.service.FotoGeradaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fotos-geradas")
public class FotoGeradaController {

    @Autowired
    private FotoGeradaService fotoGeradaService;

    @PostMapping
    public ResponseEntity<FotoGerada> salvar(@RequestBody FotoGerada fotoGerada) {
        FotoGerada salva = fotoGeradaService.salvar(fotoGerada);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<FotoGerada> listarPorCliente(@PathVariable Long clienteId) {
        return fotoGeradaService.listarPorCliente(clienteId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FotoGerada> buscarPorId(@PathVariable Long id) {
        Optional<FotoGerada> foto = fotoGeradaService.buscarPorId(id);
        return foto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        fotoGeradaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
