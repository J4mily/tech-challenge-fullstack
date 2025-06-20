package br.com.senai.desafio.tech_challenge.service;

import br.com.senai.desafio.tech_challenge.dto.*;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
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
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        // Primeiro, verificamos se o produto existe e está ativo.
        // Se não existir, findById já lançará uma exceção ou podemos tratar aqui.
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto com ID " + id + " não encontrado para exclusão.");
        }
        // O JpaRepository vai usar a nossa anotação @SQLDelete para fazer o soft-delete.
        productRepository.deleteById(id);
    }
@Override
@Transactional
public ProductResponseDTO restoreProduct(Long id) {
    productRepository.findInactiveById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + id + " não está inativo ou não existe."));

    Instant currentTime = Instant.now();
    int rowsAffected = productRepository.restoreById(id, currentTime);

    if (rowsAffected == 0) {
        throw new ResourceNotFoundException("Falha ao restaurar o produto com ID " + id + ". Nenhuma linha foi afetada.");
    }
    return getProductById(id);
}

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductUpdateDTO productUpdateDTO) {
        Product productToUpdate = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + id + " não encontrado para atualização."));

        if (productUpdateDTO.getName() != null) {
            String normalizedName = normalizeName(productUpdateDTO.getName());
            productRepository.findByName(normalizedName).ifPresent(existingProduct -> {
                if (!existingProduct.getId().equals(id)) {
                    throw new ResourceConflictException("O nome '" + productUpdateDTO.getName() + "' já está em uso por outro produto.");
                }
            });
            productToUpdate.setName(normalizedName);
        }

        if (productUpdateDTO.getDescription() != null) {
            productToUpdate.setDescription(productUpdateDTO.getDescription());
        }

        if (productUpdateDTO.getStock() != null) {
            productToUpdate.setStock(productUpdateDTO.getStock());
        }

        if (productUpdateDTO.getPrice() != null) {
            productToUpdate.setPrice(productUpdateDTO.getPrice());
        }

        Product updatedProduct = productRepository.save(productToUpdate);

        return mapToProductResponseDTO(updatedProduct);
    }
}
