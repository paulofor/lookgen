// FotoPessoaRepository.java
package com.lookgen.repository;

import com.lookgen.model.FotoPessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FotoPessoaRepository extends JpaRepository<FotoPessoa, String> {
    List<FotoPessoa> findByClienteId(Long clienteId);
}