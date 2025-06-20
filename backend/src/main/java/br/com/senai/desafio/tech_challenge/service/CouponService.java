package br.com.senai.desafio.tech_challenge.service;
import br.com.senai.desafio.tech_challenge.dto.CouponRequestDTO;
import br.com.senai.desafio.tech_challenge.dto.CouponResponseDTO;
public interface CouponService {
    CouponResponseDTO createCoupon(CouponRequestDTO couponRequestDTO);
    CouponResponseDTO getCouponByCode(String code);
}