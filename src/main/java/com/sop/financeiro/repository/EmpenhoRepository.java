package com.sop.financeiro.repository;

import com.sop.financeiro.model.Empenho;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpenhoRepository extends JpaRepository<Empenho, String> {}
