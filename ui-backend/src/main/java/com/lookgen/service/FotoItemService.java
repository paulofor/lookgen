// FotoItemService.java
package com.lookgen.service;

import com.lookgen.model.FotoItem;
import com.lookgen.model.CategoriaItem;
import com.lookgen.repository.FotoItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FotoItemService {

    private final FotoItemRepository fotoItemRepository;

    public FotoItemService(FotoItemRepository repo) {
        this.fotoItemRepository = repo;
    }

    /* ---------- CRUD ---------- */

    public FotoItem salvar(FotoItem fotoItem) {
        return fotoItemRepository.save(fotoItem);
    }

    public List<FotoItem> listarPorCliente(Long clienteId) {
        return fotoItemRepository.findByClienteId(clienteId);
    }

    public List<FotoItem> listarPorClienteECategoria(Long clienteId, CategoriaItem categoria) {
        return fotoItemRepository.findByClienteIdAndCategoria(clienteId, categoria);
    }

    public Optional<FotoItem> buscarPorId(String id) {      // ← String
        return fotoItemRepository.findById(id);
    }

    public void deletar(String id) {                        // ← String
        fotoItemRepository.deleteById(id);
    }


}
