package com.sop.financeiro.service;

import com.sop.financeiro.dto.EmpenhoDTO;
import com.sop.financeiro.exception.BadRequestException;
import com.sop.financeiro.exception.ResourceNotFoundException;
import com.sop.financeiro.model.Despesa;
import com.sop.financeiro.model.Empenho;
import com.sop.financeiro.repository.DespesaRepository;
import com.sop.financeiro.repository.EmpenhoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmpenhoServiceTest {

    private EmpenhoRepository empenhoRepository;
    private DespesaRepository despesaRepository;
    private ModelMapper modelMapper;
    private EmpenhoService empenhoService;

    @BeforeEach
    void setUp() {
        empenhoRepository = mock(EmpenhoRepository.class);
        despesaRepository = mock(DespesaRepository.class);
        modelMapper = new ModelMapper();
        empenhoService = new EmpenhoService(empenhoRepository, despesaRepository, modelMapper);
    }

    @Test
    void deveSalvarEmpenhoComSucesso() {
        Despesa despesa = new Despesa();
        despesa.setProtocolo("123");
        despesa.setValor(new BigDecimal("1000.00"));
        despesa.setEmpenhos(new ArrayList<>());

        EmpenhoDTO dto = new EmpenhoDTO();
        dto.setDespesaId("123");
        dto.setValor(new BigDecimal("500.00"));

        when(despesaRepository.findById("123")).thenReturn(Optional.of(despesa));
        when(empenhoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        EmpenhoDTO resultado = empenhoService.salvar(dto);

        assertNotNull(resultado);
        assertEquals("123", resultado.getDespesaId());
        assertNotNull(resultado.getNumero());
        verify(despesaRepository).save(despesa);
    }

    @Test
    void deveLancarExcecaoQuandoDespesaNaoExiste() {
        when(despesaRepository.findById("999")).thenReturn(Optional.empty());

        EmpenhoDTO dto = new EmpenhoDTO();
        dto.setDespesaId("999");
        dto.setValor(new BigDecimal("100"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            empenhoService.salvar(dto);
        });

        assertEquals("Despesa não encontrada", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoValorEmpenhadoUltrapassaDespesa() {
        Despesa despesa = new Despesa();
        despesa.setProtocolo("123");
        despesa.setValor(new BigDecimal("1000.00"));

        Empenho empenhoExistente = new Empenho();
        empenhoExistente.setValor(new BigDecimal("900.00"));

        despesa.setEmpenhos(List.of(empenhoExistente));

        EmpenhoDTO dto = new EmpenhoDTO();
        dto.setDespesaId("123");
        dto.setValor(new BigDecimal("200.00"));

        when(despesaRepository.findById("123")).thenReturn(Optional.of(despesa));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            empenhoService.salvar(dto);
        });

        assertEquals("Valor empenhado excede o valor da despesa", exception.getMessage());
    }

    @Test
    void deveListarEmpenhosPorDespesa() {
        Despesa despesa = new Despesa();
        despesa.setProtocolo("123");

        Empenho empenho = new Empenho();
        empenho.setNumero("2025NE0001");
        empenho.setDespesa(despesa);
        empenho.setValor(new BigDecimal("100.00"));
        empenho.setData(java.time.LocalDate.now());

        when(empenhoRepository.findAll()).thenReturn(List.of(empenho));

        List<EmpenhoDTO> lista = empenhoService.listarPorDespesa("123");

        assertEquals(1, lista.size());
        assertEquals("123", lista.get(0).getDespesaId());
    }

    @Test
    void deveDeletarEmpenhoComSucesso() {
        Empenho empenho = new Empenho();
        empenho.setNumero("2025NE0001");
        empenho.setPagamentos(new ArrayList<>());

        Despesa despesa = new Despesa();
        despesa.setEmpenhos(new ArrayList<>(List.of(empenho)));

        empenho.setDespesa(despesa);

        when(empenhoRepository.findById("2025NE0001")).thenReturn(Optional.of(empenho));

        empenhoService.deletar("2025NE0001");

        verify(empenhoRepository).delete(empenho);
        verify(despesaRepository).save(despesa);
        assertFalse(despesa.getEmpenhos().contains(empenho));
    }

    @Test
    void deveLancarExcecaoAoDeletarEmpenhoComPagamentos() {
        Empenho empenho = new Empenho();
        empenho.setNumero("2025NE0001");
        empenho.setPagamentos(List.of(mock(com.sop.financeiro.model.Pagamento.class)));

        when(empenhoRepository.findById("2025NE0001")).thenReturn(Optional.of(empenho));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            empenhoService.deletar("2025NE0001");
        });

        assertEquals("Empenho possui pagamentos vinculados", exception.getMessage());
        verify(empenhoRepository, never()).delete(any());
    }
}

