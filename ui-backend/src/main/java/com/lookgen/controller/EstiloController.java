// EstiloController.java
package com.lookgen.controller;

import com.lookgen.model.Estilo;
import com.lookgen.service.EstiloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/estilos")
public class EstiloController {

    @Autowired
    private EstiloService estiloService;

    @PostMapping
    public ResponseEntity<Estilo> salvar(@RequestBody Estilo estilo) {
        Estilo salvo = estiloService.salvar(estilo);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public List<Estilo> listarTodos() {
        return estiloService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estilo> buscarPorId(@PathVariable Long id) {
        Optional<Estilo> estilo = estiloService.buscarPorId(id);
        return estilo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        estiloService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
