// FotoGeradaService.java
package com.lookgen.service;

import com.lookgen.model.FotoGerada;
import com.lookgen.repository.FotoGeradaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FotoGeradaService {

    @Autowired
    private FotoGeradaRepository fotoGeradaRepository;

    public FotoGerada salvar(FotoGerada fotoGerada) {
        return fotoGeradaRepository.save(fotoGerada);
    }

    public List<FotoGerada> listarPorCliente(Long clienteId) {
        return fotoGeradaRepository.findByClienteId(clienteId);
    }

    public Optional<FotoGerada> buscarPorId(Long id) {
        return fotoGeradaRepository.findById(id);
    }

    public void deletar(Long id) {
        fotoGeradaRepository.deleteById(id);
    }
}