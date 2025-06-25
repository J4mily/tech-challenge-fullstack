package br.com.senai.desafio.tech_challenge.dto;
import br.com.senai.desafio.tech_challenge.model.CouponType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class AppliedDiscountDTO {
    private CouponType type;
    private BigDecimal value;
    @JsonProperty("applied_at")
    private Instant appliedAt;
}
