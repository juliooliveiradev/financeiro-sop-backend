package com.sop.financeiro.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class NumeroEmpenhoValidator implements ConstraintValidator<ValidNumeroEmpenho, String> {

    private static final Pattern PADRAO = Pattern.compile("^\\d{4}NE\\d{4}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && PADRAO.matcher(value).matches();
    }
}
