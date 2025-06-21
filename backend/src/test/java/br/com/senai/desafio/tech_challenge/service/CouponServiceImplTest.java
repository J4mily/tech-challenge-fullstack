package br.com.senai.desafio.tech_challenge.service;
import br.com.senai.desafio.tech_challenge.dto.CouponRequestDTO;
import br.com.senai.desafio.tech_challenge.dto.CouponResponseDTO;
import br.com.senai.desafio.tech_challenge.exception.ResourceConflictException;
import br.com.senai.desafio.tech_challenge.model.Coupon;
import br.com.senai.desafio.tech_challenge.model.CouponType;
import br.com.senai.desafio.tech_challenge.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    private Coupon coupon;
    private CouponRequestDTO couponRequestDTO;

    @BeforeEach
    void setUp() {
        coupon = Coupon.builder()
                .id(1L)
                .code("NATAL25")
                .type(CouponType.PERCENT)
                .value(new BigDecimal("25.00"))
                .oneShot(false)
                .validFrom(Instant.now().minus(1, ChronoUnit.DAYS))
                .validUntil(Instant.now().plus(30, ChronoUnit.DAYS))
                .createdAt(Instant.now())
                .build();

        couponRequestDTO = new CouponRequestDTO();
        couponRequestDTO.setCode("NATAL25");
        couponRequestDTO.setType(CouponType.PERCENT);
        couponRequestDTO.setValue(new BigDecimal("25"));
        couponRequestDTO.setOneShot(false);
        couponRequestDTO.setValidFrom(Instant.now());
        couponRequestDTO.setValidUntil(Instant.now().plus(60, ChronoUnit.DAYS));
    }

    @Test
    @DisplayName("Deve criar um cupom com sucesso quando o código é válido e único")
    void createCoupon_shouldSucceed_whenCodeIsValidAndUnique() {
        // Arrange
        when(couponRepository.findByCode("NATAL25")).thenReturn(Optional.empty());
        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

        // Act
        CouponResponseDTO result = couponService.createCoupon(couponRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("NATAL25", result.getCode());
        verify(couponRepository).save(any(Coupon.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar cupom com código duplicado")
    void createCoupon_shouldThrowException_whenCodeIsDuplicate() {
        // Arrange
        when(couponRepository.findByCode("NATAL25")).thenReturn(Optional.of(coupon));

        // Act & Assert
        ResourceConflictException exception = assertThrows(ResourceConflictException.class, () -> {
            couponService.createCoupon(couponRequestDTO);
        });

        assertEquals("O código de cupom 'NATAL25' já existe.", exception.getMessage());
        verify(couponRepository, never()).save(any(Coupon.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar cupom com código reservado")
    void createCoupon_shouldThrowException_whenCodeIsReserved() {
        // Arrange
        couponRequestDTO.setCode("ADMIN");

        // Act & Assert
        ResourceConflictException exception = assertThrows(ResourceConflictException.class, () -> {
            couponService.createCoupon(couponRequestDTO);
        });

        assertEquals("O código 'ADMIN' é reservado e não pode ser usado.", exception.getMessage());
        verify(couponRepository, never()).save(any(Coupon.class));
    }
}
