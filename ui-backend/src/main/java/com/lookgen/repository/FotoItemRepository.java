// FotoItemRepository.java
package com.lookgen.repository;
// src/main/java/com/lookgen/repository/FotoItemRepository.java


import com.lookgen.model.FotoItem;
import com.lookgen.model.CategoriaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FotoItemRepository extends JpaRepository<FotoItem, String> {
    List<FotoItem> findByClienteId(Long clienteId);
    List<FotoItem> findByClienteIdAndCategoria(Long clienteId, CategoriaItem categoria);
}
