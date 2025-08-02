package com.sop.financeiro.util;

import java.math.BigDecimal;
import java.util.Collection;

public class ValorUtils {
    public static BigDecimal somarValores(Collection<BigDecimal> valores) {
        return valores.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
