// ClienteRepository.java
package com.lookgen.repository;

import com.lookgen.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Métodos adicionais, se necessário
    Cliente findByEmail(String email);
}
