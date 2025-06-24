package com.lookgen.styler.repo;

import com.lookgen.styler.domain.FotoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FotoItemRepo extends JpaRepository<FotoItem, String> {
    List<FotoItem> findByClienteIdOrderByDataUpload(Long clienteId);
}
