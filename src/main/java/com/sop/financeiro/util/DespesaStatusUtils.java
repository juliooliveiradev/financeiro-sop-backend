package com.sop.financeiro.util;

import com.sop.financeiro.model.Despesa;
import com.sop.financeiro.model.Empenho;
import com.sop.financeiro.model.Pagamento;

import java.math.BigDecimal;

public class DespesaStatusUtils {

    public static String calcularStatus(Despesa despesa) {
        BigDecimal valorDespesa = despesa.getValor();

        BigDecimal totalEmpenhado = despesa.getEmpenhos().stream()
                .map(Empenho::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPago = despesa.getEmpenhos().stream()
                .flatMap(e -> e.getPagamentos().stream())
                .map(Pagamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (despesa.getEmpenhos().isEmpty()) {
            return "Aguardando Empenho";
        }

        if (totalEmpenhado.compareTo(valorDespesa) < 0) {
            return "Parcialmente Empenhada";
        }

        if (totalPago.compareTo(BigDecimal.ZERO) == 0) {
            return "Aguardando Pagamento";
        }

        if (totalPago.compareTo(valorDespesa) < 0) {
            return "Parcialmente Paga";
        }

        return "Paga";
    }
}

