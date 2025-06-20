package br.com.senai.desafio.tech_challenge.controller;

import br.com.senai.desafio.tech_challenge.dto.PaginatedResponseDTO;
import br.com.senai.desafio.tech_challenge.dto.ProductRequestDTO;
import br.com.senai.desafio.tech_challenge.dto.ProductResponseDTO;
import br.com.senai.desafio.tech_challenge.dto.ProductUpdateDTO;
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

@RestController
@RequestMapping("/api/v1/products") // URL base para todos os endpoints deste controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO createdProduct = productService.createProduct(productRequestDTO);

        // Cria a URL do recurso recém-criado para retornar no cabeçalho 'Location'.
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.getId())
                .toUri();

        // Retorna 201 Created com a URL e o corpo do objeto criado.
        return ResponseEntity.created(location).body(createdProduct);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<ProductResponseDTO>> listProducts(
            // A anotação @PageableDefault define os valores padrão para paginação e ordenação.
            @PageableDefault(size = 10, sort = "name") Pageable pageable,
            // @RequestParam captura os parâmetros da URL. 'required = false' torna-os opcionais.
            @RequestParam(required = false) String search,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice
    ) {
        PaginatedResponseDTO<ProductResponseDTO> response = productService.listProducts(pageable, search, minPrice, maxPrice);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna 204 No Content em caso de sucesso
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
}