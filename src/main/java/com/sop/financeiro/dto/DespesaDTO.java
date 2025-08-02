package com.sop.financeiro.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DespesaDTO {


    private String protocolo;

    private String tipo;
    //@JsonIgnore
    private LocalDateTime dataProtocolo;

    private String dataProtocoloFormatado;

    //@JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataVencimento;

    private String dataVencimentoFormatado;

    private String credor;
    private String descricao;

    @JsonFormat(pattern = "#,##0.00")
    private BigDecimal valor;

    private String valorFormatado;

    private String status;
}
