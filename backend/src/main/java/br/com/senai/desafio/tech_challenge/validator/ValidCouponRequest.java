package br.com.senai.desafio.tech_challenge.validator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CouponRequestValidator.class) // Aponta para a classe que contém a lógica de validação
@Target({ ElementType.TYPE }) // Define que esta anotação pode ser usada em uma classe
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCouponRequest {
    String message() default "Validação do cupom falhou";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
