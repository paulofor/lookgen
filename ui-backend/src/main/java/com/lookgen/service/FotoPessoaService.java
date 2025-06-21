// FotoPessoaService.java
package com.lookgen.service;

import com.lookgen.model.FotoPessoa;
import com.lookgen.repository.FotoPessoaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FotoPessoaService {

    private final FotoPessoaRepository repo;

    public FotoPessoaService(FotoPessoaRepository repo) {
        this.repo = repo;
    }

    /* ----- CRUD --------------------------------------------------- */

    public FotoPessoa salvar(FotoPessoa foto) {
        return repo.save(foto);
    }

    public List<FotoPessoa> listarPorCliente(Long clienteId) {
        return repo.findByClienteId(clienteId);
    }

    public Optional<FotoPessoa> buscarPorId(String id) {     // ← String
        return repo.findById(id);
    }

    public void deletarPorId(String id) {                    // ← String
        repo.deleteById(id);
    }
}
