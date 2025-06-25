package br.com.senai.desafio.tech_challenge.validator;
import br.com.senai.desafio.tech_challenge.dto.CouponRequestDTO;
import br.com.senai.desafio.tech_challenge.model.CouponType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.time.Duration;

public class CouponRequestValidator implements ConstraintValidator<ValidCouponRequest, CouponRequestDTO> {

    @Override
    public boolean isValid(CouponRequestDTO dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        boolean isValid = true;

        // 1. Validação do valor com base no tipo
        if (dto.getType() == CouponType.PERCENT) {
            if (dto.getValue().compareTo(new BigDecimal("1")) < 0 || dto.getValue().compareTo(new BigDecimal("80")) > 0) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Para o tipo PERCENT, o valor deve estar entre 1 e 80.")
                        .addPropertyNode("value").addConstraintViolation();
                isValid = false;
            }
        }

        // 2. Validação das datas
        if (dto.getValidFrom() != null && dto.getValidUntil() != null) {
            if (dto.getValidUntil().isBefore(dto.getValidFrom())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("A data de validade final deve ser posterior à data inicial.")
                        .addPropertyNode("validUntil").addConstraintViolation();
                isValid = false;
            }

            if (Duration.between(dto.getValidFrom(), dto.getValidUntil()).toDays() > (365 * 5)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("A validade máxima do cupom não pode exceder 5 anos.")
                        .addPropertyNode("validUntil").addConstraintViolation();
                isValid = false;
            }
        }

        return isValid;
    }
}
