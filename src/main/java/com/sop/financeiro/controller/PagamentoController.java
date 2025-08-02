package com.sop.financeiro.controller;

import com.sop.financeiro.dto.PagamentoDTO;
import com.sop.financeiro.service.PagamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<PagamentoDTO> criar(@RequestBody @Valid PagamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoService.salvar(dto));
    }

    @GetMapping("/empenho/{numero}")
    public List<PagamentoDTO> listarPorEmpenho(@PathVariable String numero) {
        return pagamentoService.listarPorEmpenho(numero);
    }
}

