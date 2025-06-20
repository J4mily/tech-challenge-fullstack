package br.com.senai.desafio.tech_challenge.service;
import br.com.senai.desafio.tech_challenge.dto.CouponRequestDTO;
import br.com.senai.desafio.tech_challenge.dto.CouponResponseDTO;
import br.com.senai.desafio.tech_challenge.exception.ResourceConflictException;
import br.com.senai.desafio.tech_challenge.exception.ResourceNotFoundException;
import br.com.senai.desafio.tech_challenge.model.Coupon;
import br.com.senai.desafio.tech_challenge.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

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
}