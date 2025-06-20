package br.com.senai.desafio.tech_challenge.controller;
import br.com.senai.desafio.tech_challenge.dto.CouponRequestDTO;
import br.com.senai.desafio.tech_challenge.dto.CouponResponseDTO;
import br.com.senai.desafio.tech_challenge.dto.CouponUpdateDTO;
import br.com.senai.desafio.tech_challenge.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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
    @GetMapping
    public ResponseEntity<List<CouponResponseDTO>> listAllCoupons() {
        return ResponseEntity.ok(couponService.listAllCoupons());
    }

    @PatchMapping("/{code}")
    public ResponseEntity<CouponResponseDTO> updateCoupon(@PathVariable String code, @Valid @RequestBody CouponUpdateDTO couponUpdateDTO) {
        return ResponseEntity.ok(couponService.updateCoupon(code, couponUpdateDTO));
    }

    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCoupon(@PathVariable String code) {
        couponService.deleteCoupon(code);
    }
}