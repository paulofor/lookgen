package com.lookgen.styler.repo;

import com.lookgen.styler.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // MySQL requires the FOR UPDATE clause to appear before LIMIT
    // otherwise the database will return a syntax error. See issue #n/a
    // on some installations the previous query failed with:
    // "You have an error in your SQL syntax; ... near 'SKIP LOCKED'"
    // Reordering the clauses fixes the problem.
    @Query(value = "SELECT * FROM cliente WHERE precisa_processamento = 1 FOR UPDATE SKIP LOCKED LIMIT :limit", nativeQuery = true)
    List<Cliente> findPending(@Param("limit") int limit);
}
