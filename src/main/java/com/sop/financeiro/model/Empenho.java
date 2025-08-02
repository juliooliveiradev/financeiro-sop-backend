package com.sop.financeiro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empenho {
    @Id
    private String numero;

    private LocalDate data;

    private BigDecimal valor;

    private String observacao;

    @ManyToOne
    @JoinColumn(name = "despesa_id")
    private Despesa despesa;

    @OneToMany(mappedBy = "empenho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pagamento> pagamentos = new ArrayList<>();
}