package com.sop.financeiro.controller;

import com.sop.financeiro.dto.DespesaDTO;
import com.sop.financeiro.service.DespesaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/despesas")
@RequiredArgsConstructor
public class DespesaController {

    private final DespesaService despesaService;

    @GetMapping
    public List<DespesaDTO> listar() {
        return despesaService.listarTodas();
    }

    @GetMapping("/buscar")
    public DespesaDTO buscar(@RequestParam String protocolo) {
        return despesaService.buscarPorId(protocolo);
    }

    @PostMapping
    public ResponseEntity<DespesaDTO> criar(@RequestBody @Valid DespesaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(despesaService.salvar(dto));
    }

    @PutMapping
    public DespesaDTO atualizar(@RequestParam String protocolo, @RequestBody @Valid DespesaDTO dto) {
        return despesaService.atualizar(protocolo, dto);
    }


    @DeleteMapping
    public ResponseEntity<Void> deletar(@RequestParam String protocolo) {
        despesaService.deletar(protocolo);
        return ResponseEntity.noContent().build();
    }

}

