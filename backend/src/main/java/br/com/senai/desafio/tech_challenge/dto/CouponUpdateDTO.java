package br.com.senai.desafio.tech_challenge.dto;

import br.com.senai.desafio.tech_challenge.model.CouponType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CouponUpdateDTO {
    private CouponType type;

    @Positive(message = "O valor do cupom deve ser positivo.")
    private BigDecimal value;

    private Boolean oneShot;

    @FutureOrPresent(message = "A data de início não pode ser no passado.")
    private Instant validFrom;

    @Future(message = "A data de fim deve ser no futuro.")
    private Instant validUntil;
}
