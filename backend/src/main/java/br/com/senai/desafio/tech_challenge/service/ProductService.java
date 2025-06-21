package br.com.senai.desafio.tech_challenge.service;

import br.com.senai.desafio.tech_challenge.dto.*;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);
    ProductResponseDTO getProductById(Long id);
    PaginatedResponseDTO<ProductResponseDTO> listProducts(
            Pageable pageable,
            String search,
            BigDecimal minPrice,
            BigDecimal maxPrice
    );
    void deleteProduct(Long id);
    ProductResponseDTO restoreProduct(Long id);
    ProductResponseDTO updateProduct(Long id, ProductUpdateDTO productUpdateDTO);
    ProductResponseDTO applyCoupon(Long productId, ApplyCouponDTO applyCouponDTO);
    void removeDiscount(Long productId);
    ProductResponseDTO applyPercentageDiscount(Long productId, ApplyPercentageDiscountDTO dto);


}