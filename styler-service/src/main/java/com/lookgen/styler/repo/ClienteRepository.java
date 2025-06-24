package com.lookgen.styler.repo;

import com.lookgen.styler.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Algumas instalações do MySQL geravam erro de sintaxe ao utilizar
    // o `FOR UPDATE SKIP LOCKED` antes do `LIMIT`. Conforme a sintaxe
    // oficial do MySQL 8, o `LIMIT` deve vir primeiro, seguido pelo
    // `FOR UPDATE SKIP LOCKED` no final da consulta.
    @Query(value = "SELECT * FROM cliente WHERE precisa_processamento = 1 LIMIT :limit FOR UPDATE SKIP LOCKED", nativeQuery = true)
    List<Cliente> findPending(@Param("limit") int limit);
}
