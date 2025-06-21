// FotoGeradaRepository.java
package com.lookgen.repository;

import com.lookgen.model.FotoGerada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FotoGeradaRepository extends JpaRepository<FotoGerada, Long> {
    List<FotoGerada> findByClienteId(Long clienteId);
}
