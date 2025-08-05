package com.sop.financeiro.controller;

import com.sop.financeiro.dto.EmpenhoDTO;
import com.sop.financeiro.service.EmpenhoService;
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
@RequestMapping("/api/empenhos")
@RequiredArgsConstructor
@Tag(name = "Empenhos", description = "Gerenciamento de empenhos")
@SecurityRequirement(name = "bearerAuth")
public class EmpenhoController {

    private final EmpenhoService empenhoService;

    @Operation(summary = "Criar novo empenho")
    @PostMapping
    public ResponseEntity<EmpenhoDTO> criar(@RequestBody @Valid EmpenhoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(empenhoService.salvar(dto));
    }

    @Operation(summary = "Listar empenhos por protocolo da despesa")
    @GetMapping("/despesa")
    public List<EmpenhoDTO> listarPorDespesa(
            @Parameter(description = "Protocolo da despesa", required = true)
            @RequestParam String protocolo) {
        return empenhoService.listarPorDespesa(protocolo);
    }

    @Operation(summary = "Deletar empenho pelo número")
    @DeleteMapping("/{numero}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "Número do empenho", required = true)
            @PathVariable String numero) {
        empenhoService.deletar(numero);
        return ResponseEntity.noContent().build();
    }
}
