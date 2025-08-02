package com.sop.financeiro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpenhoDTO {
    private String numero;
    private LocalDate data;
    private String dataEmpenhoFormatado;
    private BigDecimal valor;
    private String valorEmpenhadoFormatado;
    private String observacao;
    private String despesaId;
}