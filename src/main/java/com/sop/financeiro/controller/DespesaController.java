package com.sop.financeiro.controller;

import com.sop.financeiro.config.SecurityConfig;
import com.sop.financeiro.dto.DespesaDTO;
import com.sop.financeiro.service.DespesaService;
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
@RequestMapping("/api/despesas")
@RequiredArgsConstructor
@Tag(name = "Despesas", description = "Gerenciamento de despesas")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class DespesaController {

    private final DespesaService despesaService;

    @Operation(summary = "Listar todas as despesas")
    @GetMapping
    public List<DespesaDTO> listar() {
        return despesaService.listarTodas();
    }

    @Operation(summary = "Buscar despesa pelo protocolo")
    @GetMapping("/buscar")
    public DespesaDTO buscar(
            @Parameter(description = "Protocolo da despesa", required = true)
            @RequestParam String protocolo) {
        return despesaService.buscarPorId(protocolo);
    }

    @Operation(summary = "Criar nova despesa")
    @PostMapping
    public ResponseEntity<DespesaDTO> criar(@RequestBody @Valid DespesaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(despesaService.salvar(dto));
    }

    @Operation(summary = "Atualizar despesa existente pelo protocolo")
    @PutMapping
    public DespesaDTO atualizar(
            @Parameter(description = "Protocolo da despesa", required = true)
            @RequestParam String protocolo,
            @RequestBody @Valid DespesaDTO dto) {
        return despesaService.atualizar(protocolo, dto);
    }

    @Operation(summary = "Deletar despesa pelo protocolo")
    @DeleteMapping
    public ResponseEntity<Void> deletar(
            @Parameter(description = "Protocolo da despesa", required = true)
            @RequestParam String protocolo) {
        despesaService.deletar(protocolo);
        return ResponseEntity.noContent().build();
    }
}
