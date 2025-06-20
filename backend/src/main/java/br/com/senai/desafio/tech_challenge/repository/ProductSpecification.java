package br.com.senai.desafio.tech_challenge.repository;
import br.com.senai.desafio.tech_challenge.model.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductSpecification {

    // Filtro para buscar texto no nome ou na descrição do produto.
    public static Specification<Product> hasText(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        String lowerCaseText = "%" + text.toLowerCase() + "%";
        // Retorna uma especificação que verifica se o nome OU a descrição contêm o texto.
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), lowerCaseText),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), lowerCaseText)
                );
    }

    // Filtro para preço mínimo.
    public static Specification<Product> hasMinPrice(BigDecimal minPrice) {
        if (minPrice == null) {
            return null;
        }
        // Retorna uma especificação que verifica se o preço é maior ou igual ao mínimo.
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    // Filtro para preço máximo.
    public static Specification<Product> hasMaxPrice(BigDecimal maxPrice) {
        if (maxPrice == null) {
            return null;
        }
        // Retorna uma especificação que verifica se o preço é menor ou igual ao máximo.
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}