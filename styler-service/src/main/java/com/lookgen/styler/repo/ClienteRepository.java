package com.lookgen.styler.repo;

import com.lookgen.styler.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Simpler SQL compliant with most databases. The locking clause
    // appears after LIMIT as required by MySQL.
    @Query(value = "SELECT * FROM cliente WHERE precisa_processamento = 1 LIMIT :limit FOR UPDATE", nativeQuery = true)
    List<Cliente> findPending(@Param("limit") int limit);
}
