package com.sop.financeiro.util;

import java.time.Year;
import java.util.concurrent.atomic.AtomicInteger;

public class NumeroGeradorUtils {

    private static final AtomicInteger contadorEmpenho = new AtomicInteger(1);
    private static final AtomicInteger contadorPagamento = new AtomicInteger(1);
    private static final AtomicInteger contadorProtocolo = new AtomicInteger(100000); // começa em 100000

    public static String gerarProtocolo() {
        int cod1 = (int) (Math.random() * 90000) + 10000; // 5 dígitos
        int cod2 = contadorProtocolo.getAndIncrement();   // 6 dígitos sequenciais
        int ano = Year.now().getValue();
        int mes = java.time.LocalDate.now().getMonthValue();
        return String.format("%05d.%06d/%04d-%02d", cod1, cod2, ano, mes);
    }

    public static String gerarNumeroEmpenho() {
        int ano = Year.now().getValue();
        int seq = contadorEmpenho.getAndIncrement();
        return String.format("%dNE%04d", ano, seq);
    }

    public static String gerarNumeroPagamento() {
        int ano = Year.now().getValue();
        int seq = contadorPagamento.getAndIncrement();
        return String.format("%dNP%04d", ano, seq);
    }
}