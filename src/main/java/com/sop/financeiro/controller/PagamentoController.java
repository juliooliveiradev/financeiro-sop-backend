package com.sop.financeiro.controller;

import com.sop.financeiro.dto.PagamentoDTO;
import com.sop.financeiro.service.PagamentoService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@RequiredArgsConstructor
@Tag(name = "Pagamentos", description = "Gerenciamento de pagamentos")
@SecurityRequirement(name = "bearerAuth")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @Operation(summary = "Criar novo pagamento")
    @PostMapping
    public ResponseEntity<PagamentoDTO> criar(@RequestBody @Valid PagamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoService.salvar(dto));
    }

    @Operation(summary = "Listar pagamentos por número do empenho")
    @GetMapping("/empenho/{numero}")
    public List<PagamentoDTO> listarPorEmpenho(
            @Parameter(description = "Número do empenho", required = true)
            @PathVariable String numero) {
        return pagamentoService.listarPorEmpenho(numero);
    }
}
