package br.com.senai.desafio.tech_challenge.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductUpdateDTO {

    // Os campos não são @NotBlank ou @NotNull porque eles podem não ser enviados na requisição de PATCH.

    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    @Pattern(regexp = "^[\\p{L}0-9\\s\\-_,.]+$", message = "Nome contém caracteres inválidos.")
    private String name;

    @Size(max = 300, message = "A descrição não pode exceder 300 caracteres.")
    private String description;

    @Min(value = 0, message = "O estoque não pode ser negativo.")
    @Max(value = 999999, message = "O estoque não pode exceder 999999.")
    private Integer stock;

    @DecimalMin(value = "0.01", message = "O preço deve ser no mínimo 0.01.")
    @Digits(integer = 8, fraction = 2, message = "Formato de preço inválido.")
    private BigDecimal price;
}