package com.lookgen.styler.repo;

import com.lookgen.styler.domain.Estilo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstiloRepo extends JpaRepository<Estilo, Long> {
    Estilo findFirstByClienteId(Long clienteId);
}
