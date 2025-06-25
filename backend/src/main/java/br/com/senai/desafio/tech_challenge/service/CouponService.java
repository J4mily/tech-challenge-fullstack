package br.com.senai.desafio.tech_challenge.service;
import br.com.senai.desafio.tech_challenge.dto.CouponRequestDTO;
import br.com.senai.desafio.tech_challenge.dto.CouponResponseDTO;
import br.com.senai.desafio.tech_challenge.dto.CouponUpdateDTO;

import java.util.List;

public interface CouponService {
    CouponResponseDTO createCoupon(CouponRequestDTO couponRequestDTO);
    CouponResponseDTO getCouponByCode(String code);
    List<CouponResponseDTO> listAllCoupons();
    CouponResponseDTO updateCoupon(String code, CouponUpdateDTO couponUpdateDTO);
    void deleteCoupon(String code);
}