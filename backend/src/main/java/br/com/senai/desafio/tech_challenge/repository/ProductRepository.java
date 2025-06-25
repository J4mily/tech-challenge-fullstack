package br.com.senai.desafio.tech_challenge.repository;
import br.com.senai.desafio.tech_challenge.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    // Método para buscar um produto pelo nome normalizado.
    Optional<Product> findByName(String name);

    // Este método usa uma query SQL nativa para encontrar um produto pelo ID,
    // especificamente entre os que foram inativados (deleted_at IS NOT NULL).
    @Query(value = "SELECT * FROM products WHERE id = :id AND deleted_at IS NOT NULL", nativeQuery = true)
    Optional<Product> findInactiveById(Long id);
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE products SET deleted_at = NULL, updated_at = :now WHERE id = :id", nativeQuery = true)
    int restoreById(@Param("id") Long id, @Param("now") Instant now);
}