package com.sop.financeiro.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Despesa {

    @Id
    private String protocolo;

    private String tipo;

    @Column(name = "data_protocolo")
    private LocalDateTime dataProtocolo;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    private String credor;

    private String descricao;

    private BigDecimal valor;

    private String status;

    @OneToMany(mappedBy = "despesa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Empenho> empenhos = new ArrayList<>();

}
