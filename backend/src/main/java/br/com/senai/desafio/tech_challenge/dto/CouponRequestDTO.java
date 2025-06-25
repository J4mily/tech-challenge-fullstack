package br.com.senai.desafio.tech_challenge.dto;
import br.com.senai.desafio.tech_challenge.model.CouponType;
import br.com.senai.desafio.tech_challenge.validator.ValidCouponRequest;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@ValidCouponRequest
public class CouponRequestDTO {

    @NotBlank(message = "O código do cupom não pode ser vazio.")
    @Size(min = 4, max = 20, message = "O código deve ter entre 4 e 20 caracteres.")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "O código deve conter apenas letras maiúsculas e números, sem espaços.")
    private String code;

    @NotNull(message = "O tipo do cupom é obrigatório.")
    private CouponType type;

    @NotNull(message = "O valor do cupom é obrigatório.")
    @Positive(message = "O valor do cupom deve ser positivo.")
    private BigDecimal value;

    @NotNull(message = "O campo oneShot é obrigatório.")
    private Boolean oneShot;

    @NotNull(message = "A data de início da validade é obrigatória.")
    private Instant validFrom;

    @NotNull(message = "A data de fim da validade é obrigatória.")
    @Future(message = "A data de fim deve ser no futuro.")
    private Instant validUntil;
}