package br.com.senai.desafio.tech_challenge.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApplyCouponDTO {
    @NotBlank(message = "O código do cupom é obrigatório.")
    private String code;
}