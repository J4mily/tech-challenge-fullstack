package br.com.senai.desafio.tech_challenge.repository;
import br.com.senai.desafio.tech_challenge.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    // Método para buscar um produto pelo nome normalizado.
    // A lógica de normalização ficará no serviço.
    Optional<Product> findByName(String name);
}