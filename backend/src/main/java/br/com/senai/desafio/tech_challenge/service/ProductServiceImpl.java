package br.com.senai.desafio.tech_challenge.service;

import br.com.senai.desafio.tech_challenge.dto.ProductRequestDTO;
import br.com.senai.desafio.tech_challenge.dto.ProductResponseDTO;
import br.com.senai.desafio.tech_challenge.exception.ResourceConflictException;
import br.com.senai.desafio.tech_challenge.model.Product;
import br.com.senai.desafio.tech_challenge.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Lombok: cria um construtor com os campos 'final'.
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        // Normaliza o nome para a verificação de duplicidade.
        String normalizedName = normalizeName(productRequestDTO.getName());

        // Verifica se um produto com o nome normalizado já existe.
        productRepository.findByName(normalizedName).ifPresent(product -> {
            throw new ResourceConflictException("Já existe um produto com o nome '" + productRequestDTO.getName() + "'.");
        });

        // Converte o DTO para a entidade Product
        Product newProduct = Product.builder()
                .name(normalizedName) // Salva o nome já normalizado para consistência
                .description(productRequestDTO.getDescription())
                .price(productRequestDTO.getPrice())
                .stock(productRequestDTO.getStock())
                .build();

        // Salva o novo produto no banco de dados
        Product savedProduct = productRepository.save(newProduct);

        // Converte a entidade salva para o DTO de resposta e retorna
        return mapToProductResponseDTO(savedProduct);
    }

    /**
     * Normaliza uma string de nome para um formato padrão.
     * Remove espaços extras e converte para minúsculas.
     */
    private String normalizeName(String name) {
        if (name == null) {
            return null;
        }
        return name.trim().replaceAll("\\s+", " ").toLowerCase();
    }

    /**
     * Mapeia uma entidade Product para um ProductResponseDTO.
     */
    private ProductResponseDTO mapToProductResponseDTO(Product product) {
        // Neste momento, finalPrice é igual ao preço original pois não há descontos.
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName()) // Retorna o nome normalizado
                .description(product.getDescription())
                .stock(product.getStock())
                .price(product.getPrice())
                .finalPrice(product.getPrice()) // Lógica de desconto virá no futuro
                .isOutOfStock(product.getStock() == 0)
                .hasCouponApplied(false) // Lógica de cupom virá no futuro
                .discount(null) // Lógica de desconto virá no futuro
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
