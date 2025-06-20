package br.com.senai.desafio.tech_challenge.repository;

import br.com.senai.desafio.tech_challenge.model.Coupon;

import java.util.Optional;

public interface CouponRepository {
    // Método para buscar um cupom pelo código.
    Optional<Coupon> findByCode(String code);
}
