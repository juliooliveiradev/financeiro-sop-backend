package com.sop.financeiro.repository;

import com.sop.financeiro.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DespesaRepository extends JpaRepository<Despesa, String> {}
