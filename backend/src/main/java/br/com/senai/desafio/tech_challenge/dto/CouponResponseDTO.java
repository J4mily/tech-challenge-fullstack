package br.com.senai.desafio.tech_challenge.dto;
import br.com.senai.desafio.tech_challenge.model.CouponType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class CouponResponseDTO {
    private Long id;
    private String code;
    private CouponType type;
    private BigDecimal value;
    @JsonProperty("one_shot")
    private boolean oneShot;
    @JsonProperty("valid_from")
    private Instant validFrom;
    @JsonProperty("valid_until")
    private Instant validUntil;
    @JsonProperty("created_at")
    private Instant createdAt;
}