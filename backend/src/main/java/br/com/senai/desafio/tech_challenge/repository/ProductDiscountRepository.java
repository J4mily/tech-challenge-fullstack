package br.com.senai.desafio.tech_challenge.repository;
import br.com.senai.desafio.tech_challenge.model.ProductDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Long> {
    // Método para encontrar um desconto ativo para um produto específico.
    // Um desconto é ativo se 'removedAt' for nulo.
    Optional<ProductDiscount> findByProductIdAndRemovedAtIsNull(Long productId);
    @Query("SELECT pd FROM ProductDiscount pd LEFT JOIN FETCH pd.coupon WHERE pd.product.id = :productId AND pd.removedAt IS NULL")
    Optional<ProductDiscount> findActiveDiscountWithCouponByProductId(@Param("productId") Long productId);

}
