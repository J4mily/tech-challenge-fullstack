package br.com.senai.desafio.tech_challenge.service;

import br.com.senai.desafio.tech_challenge.dto.*;
import br.com.senai.desafio.tech_challenge.exception.ResourceConflictException;
import br.com.senai.desafio.tech_challenge.exception.ResourceNotFoundException;
import br.com.senai.desafio.tech_challenge.exception.UnprocessableEntityException;
import br.com.senai.desafio.tech_challenge.model.Coupon;
import br.com.senai.desafio.tech_challenge.model.CouponType;
import br.com.senai.desafio.tech_challenge.model.Product;
import br.com.senai.desafio.tech_challenge.model.ProductDiscount;
import br.com.senai.desafio.tech_challenge.repository.CouponRepository;
import br.com.senai.desafio.tech_challenge.repository.ProductDiscountRepository;
import br.com.senai.desafio.tech_challenge.repository.ProductRepository;
import br.com.senai.desafio.tech_challenge.repository.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final ProductDiscountRepository productDiscountRepository;

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

        Specification<Product> spec = Specification.where(null);
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

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto com ID " + id + " não encontrado para exclusão.");
        }
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

    @Override
    @Transactional
    public ProductResponseDTO applyCoupon(Long productId, ApplyCouponDTO applyCouponDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + productId + " não encontrado."));

        productDiscountRepository.findByProductIdAndRemovedAtIsNull(productId).ifPresent(discount -> {
            throw new ResourceConflictException("Este produto já possui um desconto ativo.");
        });

        String normalizedCode = applyCouponDTO.getCode().trim().toUpperCase();
        Coupon coupon = couponRepository.findByCode(normalizedCode)
                .orElseThrow(() -> new ResourceNotFoundException("Cupom com código '" + applyCouponDTO.getCode() + "' não encontrado."));

        Instant now = Instant.now();
        if (now.isBefore(coupon.getValidFrom()) || now.isAfter(coupon.getValidUntil())) {
            throw new UnprocessableEntityException("Este cupom não é válido na data de hoje.");
        }

        ProductDiscount newDiscount = ProductDiscount.builder()
                .product(product)
                .coupon(coupon)
                .type(coupon.getType())
                .value(coupon.getValue())
                .build();

        if (calculateFinalPrice(product.getPrice(), newDiscount).compareTo(new BigDecimal("0.01")) < 0) {
            throw new UnprocessableEntityException("A aplicação deste cupom resulta num preço final inválido (menor que R$ 0,01).");
        }

        productDiscountRepository.save(newDiscount);
        return mapToProductResponseDTO(product, newDiscount);
    }

    @Override
    @Transactional
    public ProductResponseDTO applyPercentageDiscount(Long productId, ApplyPercentageDiscountDTO dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + productId + " não encontrado."));

        productDiscountRepository.findByProductIdAndRemovedAtIsNull(productId).ifPresent(discount -> {
            throw new ResourceConflictException("Este produto já possui um desconto ativo.");
        });

        ProductDiscount newDiscount = ProductDiscount.builder()
                .product(product)
                .type(CouponType.PERCENT)
                .value(dto.getPercentage())
                .coupon(null)
                .build();

        if (calculateFinalPrice(product.getPrice(), newDiscount).compareTo(new BigDecimal("0.01")) < 0) {
            throw new UnprocessableEntityException("A aplicação deste desconto resulta num preço final inválido (menor que R$ 0,01).");
        }

        productDiscountRepository.save(newDiscount);
        return mapToProductResponseDTO(product, newDiscount);
    }

    @Override
    @Transactional
    public void removeDiscount(Long productId) {
        ProductDiscount activeDiscount = productDiscountRepository.findByProductIdAndRemovedAtIsNull(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + productId + " não possui um desconto ativo para ser removido."));

        activeDiscount.setRemovedAt(Instant.now());
        productDiscountRepository.save(activeDiscount);
    }

    private BigDecimal calculateFinalPrice(BigDecimal originalPrice, ProductDiscount discount) {
        if (discount.getType() == CouponType.PERCENT) {
            BigDecimal discountFactor = discount.getValue().divide(new BigDecimal("100"));
            BigDecimal discountAmount = originalPrice.multiply(discountFactor);
            return originalPrice.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
        } else {
            return originalPrice.subtract(discount.getValue());
        }
    }

    private ProductResponseDTO mapToProductResponseDTO(Product product, ProductDiscount activeDiscount) {
        BigDecimal finalPrice = product.getPrice();
        AppliedDiscountDTO discountDTO = null;

        if (activeDiscount != null) {
            finalPrice = calculateFinalPrice(product.getPrice(), activeDiscount);
            discountDTO = AppliedDiscountDTO.builder()
                    .type(activeDiscount.getType())
                    .value(activeDiscount.getValue())
                    .appliedAt(activeDiscount.getAppliedAt())
                    .build();
        }

        return ProductResponseDTO.builder()
                .id(product.getId()).name(product.getName()).description(product.getDescription())
                .stock(product.getStock()).price(product.getPrice()).finalPrice(finalPrice)
                .isOutOfStock(product.getStock() == 0)
                .hasCouponApplied(activeDiscount != null && activeDiscount.getCoupon() != null)
                .discount(discountDTO)
                .createdAt(product.getCreatedAt()).updatedAt(product.getUpdatedAt())
                .build();
    }

    private ProductResponseDTO mapToProductResponseDTO(Product product) {
        Optional<ProductDiscount> activeDiscount = productDiscountRepository.findByProductIdAndRemovedAtIsNull(product.getId());
        return mapToProductResponseDTO(product, activeDiscount.orElse(null));
    }

    private String normalizeName(String name) {
        if (name == null) {
            return null;
        }
        return name.trim().replaceAll("\\s+", " ").toLowerCase();
    }
}
