package com.lookgen.controller;

import com.lookgen.model.FotoItem;
import com.lookgen.service.FotoItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// FotoItemController.java
@RestController
@RequestMapping("/api/foto-item")
public class FotoItemController {

    private final FotoItemService fotoItemService;

    public FotoItemController(FotoItemService service) {
        this.fotoItemService = service;
    }

    /* ----------------------------------------------------------------
     * Buscar item por ID (String)
     * ---------------------------------------------------------------- */
    @GetMapping("/{id}")
    public ResponseEntity<FotoItem> buscarPorId(@PathVariable String id) {
        return fotoItemService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /* ----------------------------------------------------------------
     * Deletar item por ID (String)
     * ---------------------------------------------------------------- */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        fotoItemService.deletar(id);          // <-- agora bate com o service
        return ResponseEntity.noContent().build();
    }
}
