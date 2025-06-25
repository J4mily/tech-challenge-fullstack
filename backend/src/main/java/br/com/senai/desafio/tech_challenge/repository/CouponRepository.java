package br.com.senai.desafio.tech_challenge.repository;

import br.com.senai.desafio.tech_challenge.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode(String code);
    Optional<Coupon> findByCodeAndDeletedAtIsNull(String code);
    List<Coupon> findByDeletedAtIsNull();
}
