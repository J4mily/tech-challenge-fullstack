package br.com.senai.desafio.tech_challenge.service;
import br.com.senai.desafio.tech_challenge.dto.ProductRequestDTO;
import br.com.senai.desafio.tech_challenge.dto.ProductResponseDTO;
import br.com.senai.desafio.tech_challenge.exception.ResourceConflictException;
import br.com.senai.desafio.tech_challenge.exception.ResourceNotFoundException;
import br.com.senai.desafio.tech_challenge.model.Product;
import br.com.senai.desafio.tech_challenge.repository.ProductDiscountRepository;
import br.com.senai.desafio.tech_challenge.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

// Ativa a extensão do Mockito para JUnit 5, que nos permite usar as anotações @Mock e @InjectMocks.
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    // @Mock: Cria um "duplo" ou um "ator" para o nosso repositório.
    // Ele não vai ao banco de dados; nós vamos dizer a ele exatamente como se comportar em cada teste.
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductDiscountRepository productDiscountRepository; // Precisamos de mockar todas as dependências do serviço.

    // @InjectMocks: Cria uma instância real do nosso serviço (o "cérebro" que queremos testar)
    // e injeta os mocks (os "atores") dentro dele automaticamente.
    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequestDTO productRequestDTO;

    // @BeforeEach: Este método é executado ANTES de cada teste.
    // Usamo-lo para criar objetos de exemplo que serão reutilizados em vários testes.
    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("café especial")
                .description("Um café de alta qualidade")
                .price(new BigDecimal("59.90"))
                .stock(100)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setName("Café Especial");
        productRequestDTO.setDescription("Um café de alta qualidade");
        productRequestDTO.setPrice(new BigDecimal("59.90"));
        productRequestDTO.setStock(100);
    }

    @Test
    @DisplayName("Deve criar um produto com sucesso quando o nome é único")
    void createProduct_shouldReturnProduct_whenNameIsUnique() {
        // Arrange (Arrumar o cenário)
        // Dizemos ao nosso mock: "Quando o método findByName for chamado com qualquer string,
        // retorne um Optional vazio (ou seja, o nome não existe)."
        when(productRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Dizemos ao nosso mock: "Quando o método save for chamado com qualquer objeto Product,
        // retorne o nosso objeto 'product' de exemplo."
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Dizemos ao mock do desconto para retornar vazio, pois é um produto novo.
        when(productDiscountRepository.findByProductIdAndRemovedAtIsNull(any())).thenReturn(Optional.empty());

        // Act (Agir / Executar o método a ser testado)
        ProductResponseDTO result = productService.createProduct(productRequestDTO);

        // Assert (Verificar o resultado)
        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
        verify(productRepository).save(any(Product.class)); // Verifica se o método save foi realmente chamado.
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar criar um produto com nome que já existe")
    void createProduct_shouldThrowException_whenNameExists() {
        // Arrange (Arrumar o cenário)
        // Dizemos ao nosso mock: "Quando o método findByName for chamado, finja que
        // você encontrou um produto (o nosso 'product' de exemplo)."
        when(productRepository.findByName(anyString())).thenReturn(Optional.of(product));

        // Act & Assert (Agir e Verificar)
        // Verificamos se, ao chamar createProduct, uma ResourceConflictException é lançada.
        assertThrows(ResourceConflictException.class, () -> {
            productService.createProduct(productRequestDTO);
        });

        // Verificamos se o método save NUNCA foi chamado, pois a execução deveria parar antes.
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve retornar um produto quando o ID existe")
    void getProductById_shouldReturnProduct_whenIdExists() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productDiscountRepository.findByProductIdAndRemovedAtIsNull(1L)).thenReturn(Optional.empty());

        // Act
        ProductResponseDTO result = productService.getProductById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Deve lançar uma exceção quando o ID do produto não existe")
    void getProductById_shouldThrowException_whenIdDoesNotExist() {
        // Arrange
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(99L);
        });
    }
}