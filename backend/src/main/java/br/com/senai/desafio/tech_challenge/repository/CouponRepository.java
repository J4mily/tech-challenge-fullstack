package br.com.senai.desafio.tech_challenge.repository;

import br.com.senai.desafio.tech_challenge.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    // Método para buscar um cupom pelo código.
    Optional<Coupon> findByCode(String code);
}
