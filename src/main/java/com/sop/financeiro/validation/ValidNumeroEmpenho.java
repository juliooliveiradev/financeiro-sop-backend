package com.sop.financeiro.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NumeroEmpenhoValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNumeroEmpenho {
    String message() default "Número de empenho inválido. Use o formato AAAANE0000 (ex: 2025NE0003)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
