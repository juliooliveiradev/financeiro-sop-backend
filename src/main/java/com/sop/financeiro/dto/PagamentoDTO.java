package com.sop.financeiro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoDTO {
    private String numero;
    private LocalDate data;
    private String dataPagamentoFormatado;
    private BigDecimal valor;
    private String valorPagoFormatado;
    private String observacao;
    private String empenhoId;
}

