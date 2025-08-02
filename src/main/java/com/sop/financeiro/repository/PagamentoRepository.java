package com.sop.financeiro.repository;

import com.sop.financeiro.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, String> {}
