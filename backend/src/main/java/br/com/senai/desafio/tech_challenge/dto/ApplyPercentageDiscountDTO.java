package br.com.senai.desafio.tech_challenge.dto;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ApplyPercentageDiscountDTO {
    @NotNull(message = "O valor percentual é obrigatório.")
    @DecimalMin(value = "1.0", message = "O desconto deve ser de no mínimo 1%.")
    @DecimalMax(value = "80.0", message = "O desconto não pode exceder 80%.")
    private BigDecimal percentage;
}
