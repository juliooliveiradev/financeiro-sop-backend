package com.sop.financeiro.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatadorUtils {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final Locale LOCALE_BR = new Locale("pt", "BR");
    private static final NumberFormat FORMATADOR_MOEDA = NumberFormat.getCurrencyInstance(LOCALE_BR);

    public static String formatarData(LocalDate data) {
        return data != null ? data.format(FORMATO_DATA) : "";
    }

    public static String formatarDataHora(LocalDateTime dataHora) {
        return dataHora != null ? dataHora.format(FORMATO_DATA_HORA) : "";
    }

    public static String formatarMoeda(BigDecimal valor) {
        return valor != null ? FORMATADOR_MOEDA.format(valor) : "R$ 0,00";
    }
}
