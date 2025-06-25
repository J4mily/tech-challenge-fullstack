package br.com.senai.desafio.tech_challenge.controller;

import br.com.senai.desafio.tech_challenge.dto.*;
import br.com.senai.desafio.tech_challenge.exception.ResourceConflictException;
import br.com.senai.desafio.tech_challenge.exception.ResourceNotFoundException;
import br.com.senai.desafio.tech_challenge.exception.UnprocessableEntityException;
import br.com.senai.desafio.tech_challenge.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductRequestDTO productRequestDTO,
            HttpServletRequest request) {
        try {
            ProductResponseDTO createdProduct = productService.createProduct(productRequestDTO);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdProduct.getId())
                    .toUri();
            return ResponseEntity.created(location).body(createdProduct);
        } catch (ResourceConflictException ex) {
            return buildErrorResponse(HttpStatus.CONFLICT, "Conflito de Recurso", ex.getMessage(), request.getRequestURI());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id, HttpServletRequest request) {
        try {
            ProductResponseDTO product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (ResourceNotFoundException ex) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, "Recurso Não Encontrado", ex.getMessage(), request.getRequestURI());
        }
    }

    @GetMapping
    public ResponseEntity<?> listProducts(
            @PageableDefault(size = 10, sort = "name") Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean hasDiscount,
            @RequestParam(required = false) Boolean onlyOutOfStock,
            @RequestParam(required = false) Boolean withCouponApplied,
            HttpServletRequest request) {
        try {
            PaginatedResponseDTO<ProductResponseDTO> response = productService.listProducts(
                    pageable, search, minPrice, maxPrice, hasDiscount, onlyOutOfStock, withCouponApplied);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            String errorMessage = "Ocorreu um erro ao processar a listagem de produtos.";
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro Interno", errorMessage, request.getRequestURI());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, "Recurso Não Encontrado", ex.getMessage(), request.getRequestURI());
        }
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<?> restoreProduct(@PathVariable Long id, HttpServletRequest request) {
        try {
            ProductResponseDTO restoredProduct = productService.restoreProduct(id);
            return ResponseEntity.ok(restoredProduct);
        } catch (ResourceNotFoundException ex) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, "Recurso Não Encontrado", ex.getMessage(), request.getRequestURI());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateDTO productUpdateDTO,
            HttpServletRequest request) {
        try {
            ProductResponseDTO updatedProduct = productService.updateProduct(id, productUpdateDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (ResourceNotFoundException ex) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, "Recurso Não Encontrado", ex.getMessage(), request.getRequestURI());
        } catch (ResourceConflictException ex) {
            return buildErrorResponse(HttpStatus.CONFLICT, "Conflito de Recurso", ex.getMessage(), request.getRequestURI());
        }
    }

    @PostMapping("/{id}/discount/coupon")
    public ResponseEntity<?> applyCoupon(
            @PathVariable("id") Long productId,
            @Valid @RequestBody ApplyCouponDTO applyCouponDTO,
            HttpServletRequest request) {
        try {
            ProductResponseDTO productResponse = productService.applyCoupon(productId, applyCouponDTO);
            return ResponseEntity.ok(productResponse);
        } catch (ResourceNotFoundException ex) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, "Recurso Não Encontrado", ex.getMessage(), request.getRequestURI());
        } catch (ResourceConflictException ex) {
            return buildErrorResponse(HttpStatus.CONFLICT, "Conflito de Recurso", ex.getMessage(), request.getRequestURI());
        } catch (UnprocessableEntityException ex) {
            return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Entidade Não Processável", ex.getMessage(), request.getRequestURI());
        } catch (Exception ex) {
            String genericMessage = "Ocorreu um erro inesperado. Tente novamente mais tarde.";
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro Interno do Servidor", genericMessage, request.getRequestURI());
        }
    }

    @DeleteMapping("/{id}/discount")
    public ResponseEntity<?> removeDiscount(@PathVariable("id") Long productId, HttpServletRequest request) {
        try {
            productService.removeDiscount(productId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, "Recurso Não Encontrado", ex.getMessage(), request.getRequestURI());
        }
    }

    @PostMapping("/{id}/discount/percent")
    public ResponseEntity<?> applyPercentageDiscount(
            @PathVariable("id") Long productId,
            @Valid @RequestBody ApplyPercentageDiscountDTO dto,
            HttpServletRequest request) {
        try {
            ProductResponseDTO response = productService.applyPercentageDiscount(productId, dto);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException ex) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, "Recurso Não Encontrado", ex.getMessage(), request.getRequestURI());
        } catch (ResourceConflictException ex) {
            return buildErrorResponse(HttpStatus.CONFLICT, "Conflito de Recurso", ex.getMessage(), request.getRequestURI());
        } catch (UnprocessableEntityException ex) {
            return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Entidade Não Processável", ex.getMessage(), request.getRequestURI());
        }
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String error, String message, String path) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", System.currentTimeMillis());
        errorBody.put("status", status.value());
        errorBody.put("error", error);
        errorBody.put("message", message);
        errorBody.put("path", path);
        return new ResponseEntity<>(errorBody, status);
    }
}