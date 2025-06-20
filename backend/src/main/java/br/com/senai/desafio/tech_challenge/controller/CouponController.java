package br.com.senai.desafio.tech_challenge.controller;
import br.com.senai.desafio.tech_challenge.dto.CouponRequestDTO;
import br.com.senai.desafio.tech_challenge.dto.CouponResponseDTO;
import br.com.senai.desafio.tech_challenge.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CouponResponseDTO> createCoupon(@Valid @RequestBody CouponRequestDTO couponRequestDTO) {
        CouponResponseDTO createdCoupon = couponService.createCoupon(couponRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(createdCoupon.getCode())
                .toUri();
        return ResponseEntity.created(location).body(createdCoupon);
    }

    @GetMapping("/{code}")
    public ResponseEntity<CouponResponseDTO> getCouponByCode(@PathVariable String code) {
        return ResponseEntity.ok(couponService.getCouponByCode(code));
    }
}