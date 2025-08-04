package com.sop.financeiro;


import com.sop.financeiro.dto.DespesaDTO;
import com.sop.financeiro.exception.BadRequestException;
import com.sop.financeiro.exception.ResourceNotFoundException;
import com.sop.financeiro.model.Despesa;
import com.sop.financeiro.model.Empenho;
import com.sop.financeiro.repository.DespesaRepository;
import com.sop.financeiro.service.DespesaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DespesaServiceTest {

    private DespesaRepository despesaRepository;
    private ModelMapper modelMapper;
    private DespesaService despesaService;

    @BeforeEach
    void setUp() {
        despesaRepository = mock(DespesaRepository.class);
        modelMapper = new ModelMapper();
        despesaService = new DespesaService(despesaRepository, modelMapper);
    }

    @Test
    void deveSalvarDespesa() {
        DespesaDTO dto = new DespesaDTO();
        dto.setDescricao("Teste");
        dto.setValor(BigDecimal.valueOf(100));
        dto.setDataVencimento(LocalDate.now());

        Despesa despesa = modelMapper.map(dto, Despesa.class);
        despesa.setProtocolo("12345");
        despesa.setDataProtocolo(LocalDateTime.now());
        despesa.setStatus("Aguardando Empenho");

        when(despesaRepository.save(any(Despesa.class))).thenReturn(despesa);

        DespesaDTO resposta = despesaService.salvar(dto);

        assertNotNull(resposta);
        assertEquals("Aguardando Empenho", resposta.getStatus());
        verify(despesaRepository, times(1)).save(any(Despesa.class));
    }

    @Test
    void deveListarTodasDespesas() {
        Despesa d = new Despesa();
        d.setDescricao("Teste");
        d.setValor(BigDecimal.valueOf(150));
        d.setDataProtocolo(LocalDateTime.now());
        d.setDataVencimento(LocalDate.now());
        d.setProtocolo("123");
        d.setStatus("Aguardando");

        when(despesaRepository.findAll()).thenReturn(List.of(d));

        List<DespesaDTO> lista = despesaService.listarTodas();

        assertEquals(1, lista.size());
        assertEquals("Teste", lista.get(0).getDescricao());
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        Despesa d = new Despesa();
        d.setProtocolo("abc123");
        d.setDescricao("Teste");
        d.setValor(BigDecimal.valueOf(250));
        d.setDataVencimento(LocalDate.now());
        d.setDataProtocolo(LocalDateTime.now());

        when(despesaRepository.findById("abc123")).thenReturn(Optional.of(d));

        DespesaDTO dto = despesaService.buscarPorId("abc123");

        assertNotNull(dto);
        assertEquals("abc123", dto.getProtocolo());
        assertEquals("Teste", dto.getDescricao());
    }

    @Test
    void deveLancarExcecaoAoBuscarPorIdInexistente() {
        when(despesaRepository.findById("naoExiste")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> despesaService.buscarPorId("naoExiste"));
    }

    @Test
    void deveAtualizarDespesaComSucesso() {
        Despesa existente = new Despesa();
        existente.setProtocolo("abc123");
        existente.setDescricao("Original");
        existente.setValor(BigDecimal.valueOf(200));

        DespesaDTO dtoAtualizado = new DespesaDTO();
        dtoAtualizado.setDescricao("Atualizada");
        dtoAtualizado.setValor(BigDecimal.valueOf(300));

        when(despesaRepository.findById("abc123")).thenReturn(Optional.of(existente));
        when(despesaRepository.save(any(Despesa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DespesaDTO resultado = despesaService.atualizar("abc123", dtoAtualizado);

        assertEquals("Atualizada", resultado.getDescricao());
        assertEquals(BigDecimal.valueOf(300), resultado.getValor());
    }

    @Test
    void deveDeletarDespesaSemEmpenhos() {
        Despesa d = new Despesa();
        d.setProtocolo("abc123");
        d.setEmpenhos(Collections.emptyList());

        when(despesaRepository.findById("abc123")).thenReturn(Optional.of(d));

        despesaService.deletar("abc123");

        verify(despesaRepository).delete(d);
    }

    @Test
    void deveLancarExcecaoAoDeletarDespesaComEmpenhos() {
        Despesa despesaComEmpenhos = new Despesa();
        despesaComEmpenhos.setProtocolo("123");

        // Cria um mock explícito de Empenho
        Empenho empenhoMock = mock(Empenho.class);
        despesaComEmpenhos.setEmpenhos(Collections.singletonList(empenhoMock));

        when(despesaRepository.findById("123")).thenReturn(Optional.of(despesaComEmpenhos));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            despesaService.deletar("123");
        });

        assertEquals("Não pode excluir uma despesa com empenhos", exception.getMessage());
        verify(despesaRepository, never()).delete(any());
    }
}

