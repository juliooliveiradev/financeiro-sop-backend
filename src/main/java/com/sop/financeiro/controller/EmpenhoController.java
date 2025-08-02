package com.sop.financeiro.controller;

import com.sop.financeiro.dto.EmpenhoDTO;
import com.sop.financeiro.service.EmpenhoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empenhos")
@RequiredArgsConstructor
public class EmpenhoController {

    private final EmpenhoService empenhoService;

    @PostMapping
    public ResponseEntity<EmpenhoDTO> criar(@RequestBody @Valid EmpenhoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(empenhoService.salvar(dto));
    }

    @GetMapping("/despesa")
    public List<EmpenhoDTO> listarPorDespesa(@RequestParam String protocolo) {
        return empenhoService.listarPorDespesa(protocolo);
    }

    @DeleteMapping("/{numero}")
    public ResponseEntity<Void> deletar(@PathVariable String numero) {
        empenhoService.deletar(numero);
        return ResponseEntity.noContent().build();
    }
}

