package br.com.senai.desafio.tech_challenge.controller;

import br.com.senai.desafio.tech_challenge.dto.*;
import br.com.senai.desafio.tech_challenge.exception.ResourceConflictException;
import br.com.senai.desafio.tech_challenge.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products") // URL base para todos os endpoints deste controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        try {
            ProductResponseDTO createdProduct = productService.createProduct(productRequestDTO);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdProduct.getId())
                    .toUri();

            return ResponseEntity.created(location).body(createdProduct);

        } catch (ResourceConflictException ex) {
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("timestamp", System.currentTimeMillis());
            errorBody.put("status", HttpStatus.CONFLICT.value());
            errorBody.put("error", "Conflito de Recurso");
            errorBody.put("message", ex.getMessage());

            return new ResponseEntity<>(errorBody, HttpStatus.CONFLICT);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<ProductResponseDTO>> listProducts(
            @PageableDefault(size = 10, sort = "name") Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean hasDiscount,
            @RequestParam(required = false) Boolean onlyOutOfStock,
            @RequestParam(required = false) Boolean withCouponApplied
    ) {
        PaginatedResponseDTO<ProductResponseDTO> response = productService.listProducts(
                pageable, search, minPrice, maxPrice, hasDiscount, onlyOutOfStock, withCouponApplied
        );
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<ProductResponseDTO> restoreProduct(@PathVariable Long id) {
        ProductResponseDTO restoredProduct = productService.restoreProduct(id);
        return ResponseEntity.ok(restoredProduct);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateDTO productUpdateDTO) {
        ProductResponseDTO updatedProduct = productService.updateProduct(id, productUpdateDTO);
        return ResponseEntity.ok(updatedProduct);
    }
    @PostMapping("/{id}/discount/coupon")
    public ResponseEntity<ProductResponseDTO> applyCoupon(
            @PathVariable("id") Long productId,
            @Valid @RequestBody ApplyCouponDTO applyCouponDTO) {
        return ResponseEntity.ok(productService.applyCoupon(productId, applyCouponDTO));
    }
    @DeleteMapping("/{id}/discount")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeDiscount(@PathVariable("id") Long productId) {
        productService.removeDiscount(productId);
    }
    @PostMapping("/{id}/discount/percent")
    public ResponseEntity<ProductResponseDTO> applyPercentageDiscount(
            @PathVariable("id") Long productId,
            @Valid @RequestBody ApplyPercentageDiscountDTO dto) {
        return ResponseEntity.ok(productService.applyPercentageDiscount(productId, dto));
    }
}