package br.com.senai.desafio.tech_challenge.service;

import br.com.senai.desafio.tech_challenge.dto.MetaDTO;
import br.com.senai.desafio.tech_challenge.dto.PaginatedResponseDTO;
import br.com.senai.desafio.tech_challenge.dto.ProductRequestDTO;
import br.com.senai.desafio.tech_challenge.dto.ProductResponseDTO;
import br.com.senai.desafio.tech_challenge.exception.ResourceConflictException;
import br.com.senai.desafio.tech_challenge.exception.ResourceNotFoundException;
import br.com.senai.desafio.tech_challenge.model.Product;
import br.com.senai.desafio.tech_challenge.repository.ProductRepository;
import br.com.senai.desafio.tech_challenge.repository.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        String normalizedName = normalizeName(productRequestDTO.getName());
        productRepository.findByName(normalizedName).ifPresent(product -> {
            throw new ResourceConflictException("Já existe um produto com o nome '" + productRequestDTO.getName() + "'.");
        });
        Product newProduct = Product.builder()
                .name(normalizedName)
                .description(productRequestDTO.getDescription())
                .price(productRequestDTO.getPrice())
                .stock(productRequestDTO.getStock())
                .build();
        Product savedProduct = productRepository.save(newProduct);
        return mapToProductResponseDTO(savedProduct);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + id + " não encontrado."));
        return mapToProductResponseDTO(product);
    }

    @Override
    public PaginatedResponseDTO<ProductResponseDTO> listProducts(
            Pageable pageable, String search, BigDecimal minPrice, BigDecimal maxPrice) {

        Specification<Product> spec = Specification.where(null); // Começa com uma especificação "vazia".

        // Adiciona cada filtro condicionalmente.
        if (search != null && !search.isEmpty()) {
            spec = spec.and(ProductSpecification.hasText(search));
        }
        if (minPrice != null) {
            spec = spec.and(ProductSpecification.hasMinPrice(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(ProductSpecification.hasMaxPrice(maxPrice));
        }

        Page<Product> productPage = productRepository.findAll(spec, pageable);

        var productDTOs = productPage.getContent().stream()
                .map(this::mapToProductResponseDTO)
                .collect(Collectors.toList());

        MetaDTO meta = MetaDTO.builder()
                .page(productPage.getNumber())
                .limit(productPage.getSize())
                .totalItems(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .build();

        return new PaginatedResponseDTO<>(productDTOs, meta);
    }

    private String normalizeName(String name) {
        if (name == null) {
            return null;
        }
        return name.trim().replaceAll("\\s+", " ").toLowerCase();
    }

    private ProductResponseDTO mapToProductResponseDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .stock(product.getStock())
                .price(product.getPrice())
                .finalPrice(product.getPrice())
                .isOutOfStock(product.getStock() == 0)
                .hasCouponApplied(false)
                .discount(null)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
