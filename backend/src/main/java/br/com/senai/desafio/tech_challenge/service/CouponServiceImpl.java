package br.com.senai.desafio.tech_challenge.service;
import br.com.senai.desafio.tech_challenge.dto.CouponRequestDTO;
import br.com.senai.desafio.tech_challenge.dto.CouponResponseDTO;
import br.com.senai.desafio.tech_challenge.dto.CouponUpdateDTO;
import br.com.senai.desafio.tech_challenge.exception.ResourceConflictException;
import br.com.senai.desafio.tech_challenge.exception.ResourceNotFoundException;
import br.com.senai.desafio.tech_challenge.model.Coupon;
import br.com.senai.desafio.tech_challenge.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private static final List<String> RESERVED_CODES = Arrays.asList("ADMIN", "AUTH", "NULL", "UNDEFINED");

    @Override
    public CouponResponseDTO createCoupon(CouponRequestDTO couponRequestDTO) {
        String normalizedCode = normalizeCode(couponRequestDTO.getCode());

        // Validação de código reservado
        if (RESERVED_CODES.contains(normalizedCode)) {
            throw new ResourceConflictException("O código '" + couponRequestDTO.getCode() + "' é reservado e não pode ser usado.");
        }

        // Validação de unicidade
        couponRepository.findByCode(normalizedCode).ifPresent(c -> {
            throw new ResourceConflictException("O código de cupom '" + couponRequestDTO.getCode() + "' já existe.");
        });

        Coupon coupon = Coupon.builder()
                .code(normalizedCode)
                .type(couponRequestDTO.getType())
                .value(couponRequestDTO.getValue())
                .oneShot(couponRequestDTO.getOneShot())
                .validFrom(couponRequestDTO.getValidFrom())
                .validUntil(couponRequestDTO.getValidUntil())
                .build();

        Coupon savedCoupon = couponRepository.save(coupon);

        return mapToCouponResponseDTO(savedCoupon);
    }

    @Override
    public CouponResponseDTO getCouponByCode(String code) {
        String normalizedCode = normalizeCode(code);
        Coupon coupon = couponRepository.findByCode(normalizedCode)
                .orElseThrow(() -> new ResourceNotFoundException("Cupom com o código '" + code + "' não encontrado."));
        return mapToCouponResponseDTO(coupon);
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase();
    }

    private CouponResponseDTO mapToCouponResponseDTO(Coupon coupon) {
        return CouponResponseDTO.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .type(coupon.getType())
                .value(coupon.getValue())
                .oneShot(coupon.isOneShot())
                .validFrom(coupon.getValidFrom())
                .validUntil(coupon.getValidUntil())
                .createdAt(coupon.getCreatedAt())
                .build();
    }
    @Override
    public List<CouponResponseDTO> listAllCoupons() {
        return couponRepository.findAll().stream()
                .map(this::mapToCouponResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CouponResponseDTO updateCoupon(String code, CouponUpdateDTO couponUpdateDTO) {
        String normalizedCode = normalizeCode(code);
        Coupon couponToUpdate = couponRepository.findByCode(normalizedCode)
                .orElseThrow(() -> new ResourceNotFoundException("Cupom com o código '" + code + "' não encontrado."));

        // Lógica de atualização parcial
        if (couponUpdateDTO.getType() != null) {
            couponToUpdate.setType(couponUpdateDTO.getType());
        }
        if (couponUpdateDTO.getValue() != null) {
            couponToUpdate.setValue(couponUpdateDTO.getValue());
        }
        if (couponUpdateDTO.getOneShot() != null) {
            couponToUpdate.setOneShot(couponUpdateDTO.getOneShot());
        }
        if (couponUpdateDTO.getValidFrom() != null) {
            couponToUpdate.setValidFrom(couponUpdateDTO.getValidFrom());
        }
        if (couponUpdateDTO.getValidUntil() != null) {
            couponToUpdate.setValidUntil(couponUpdateDTO.getValidUntil());
        }

        Coupon updatedCoupon = couponRepository.save(couponToUpdate);
        return mapToCouponResponseDTO(updatedCoupon);
    }
    @Override
    @Transactional
    public void deleteCoupon(String code) {
        String normalizedCode = normalizeCode(code);
        Coupon coupon = couponRepository.findByCode(normalizedCode)
                .orElseThrow(() -> new ResourceNotFoundException("Cupom com o código '" + code + "' não encontrado."));

        couponRepository.delete(coupon);
    }
}