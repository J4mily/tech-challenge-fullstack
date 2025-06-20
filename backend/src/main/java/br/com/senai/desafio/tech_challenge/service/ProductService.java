package br.com.senai.desafio.tech_challenge.service;

import br.com.senai.desafio.tech_challenge.dto.PaginatedResponseDTO;
import br.com.senai.desafio.tech_challenge.dto.ProductRequestDTO;
import br.com.senai.desafio.tech_challenge.dto.ProductResponseDTO;

import br.com.senai.desafio.tech_challenge.dto.ProductUpdateDTO;
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

}