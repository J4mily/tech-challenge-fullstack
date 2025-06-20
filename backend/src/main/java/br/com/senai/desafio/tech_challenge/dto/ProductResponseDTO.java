package br.com.senai.desafio.tech_challenge.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Não inclui campos nulos no JSON de resposta
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Integer stock;
    @JsonProperty("is_out_of_stock") // Define o nome do campo no JSON
    private boolean isOutOfStock;
    private BigDecimal price;
    private BigDecimal finalPrice; // Será calculado dinamicamente
    private Object discount; // Será um objeto com detalhes do desconto
    @JsonProperty("has_coupon_applied")
    private boolean hasCouponApplied;
    @JsonProperty("created_at")
    private Instant createdAt;
    @JsonProperty("updated_at")
    private Instant updatedAt;
}