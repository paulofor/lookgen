package com.lookgen.service;

import com.lookgen.model.Estilo;
import com.lookgen.repository.EstiloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstiloService {

    @Autowired
    private EstiloRepository estiloRepository;

    public Estilo salvar(Estilo estilo) {
        return estiloRepository.save(estilo);
    }

    public List<Estilo> listarTodos() {
        return estiloRepository.findAll();
    }

    public Optional<Estilo> buscarPorId(Long id) {
        return estiloRepository.findById(id);
    }

    public void deletar(Long id) {
        estiloRepository.deleteById(id);
    }
}