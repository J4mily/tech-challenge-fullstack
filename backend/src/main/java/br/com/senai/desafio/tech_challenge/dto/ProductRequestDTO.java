package br.com.senai.desafio.tech_challenge.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequestDTO {

    @NotBlank(message = "O nome não pode ser vazio.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-_,.]+$", message = "Nome contém caracteres inválidos.")
    private String name;

    @Size(max = 300, message = "A descrição não pode exceder 300 caracteres.")
    private String description;

    @NotNull(message = "O estoque não pode ser nulo.")
    @Min(value = 0, message = "O estoque não pode ser negativo.")
    @Max(value = 999999, message = "O estoque não pode exceder 999999.")
    private Integer stock;

    @NotNull(message = "O preço não pode ser nulo.")
    @DecimalMin(value = "0.01", message = "O preço deve ser no mínimo 0.01.")
    @Digits(integer = 8, fraction = 2, message = "Formato de preço inválido.")
    private BigDecimal price;
}