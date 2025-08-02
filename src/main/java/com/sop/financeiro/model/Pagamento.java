package com.sop.financeiro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {
    @Id
    private String numero;

    private LocalDate data;

    private BigDecimal valor;

    private String observacao;

    @ManyToOne
    @JoinColumn(name = "empenho_id")
    private Empenho empenho;
}
