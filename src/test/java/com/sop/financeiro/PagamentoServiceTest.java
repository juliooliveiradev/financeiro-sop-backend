package com.sop.financeiro;

import com.sop.financeiro.dto.PagamentoDTO;
import com.sop.financeiro.exception.BadRequestException;
import com.sop.financeiro.exception.ResourceNotFoundException;
import com.sop.financeiro.model.Despesa;
import com.sop.financeiro.model.Empenho;
import com.sop.financeiro.model.Pagamento;
import com.sop.financeiro.repository.DespesaRepository;
import com.sop.financeiro.repository.EmpenhoRepository;
import com.sop.financeiro.repository.PagamentoRepository;
import com.sop.financeiro.service.PagamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PagamentoServiceTest {

    private PagamentoRepository pagamentoRepository;
    private EmpenhoRepository empenhoRepository;
    private DespesaRepository despesaRepository;
    private ModelMapper modelMapper;
    private PagamentoService pagamentoService;

    @BeforeEach
    void setUp() {
        pagamentoRepository = mock(PagamentoRepository.class);
        empenhoRepository = mock(EmpenhoRepository.class);
        despesaRepository = mock(DespesaRepository.class);
        modelMapper = new ModelMapper();
        pagamentoService = new PagamentoService(pagamentoRepository, empenhoRepository, despesaRepository, modelMapper);
    }

    @Test
    void deveSalvarPagamentoComSucesso() {
        // Arrange
        PagamentoDTO dto = new PagamentoDTO();
        dto.setEmpenhoId("EMP001");
        dto.setValor(new BigDecimal("100.00"));
        dto.setData(LocalDate.of(2025, 8, 1));
        dto.setObservacao("Pagamento parcial");

        Empenho empenho = new Empenho();
        empenho.setNumero("EMP001");
        empenho.setValor(new BigDecimal("500.00"));
        empenho.setPagamentos(new ArrayList<>());

        Despesa despesa = new Despesa();
        empenho.setDespesa(despesa);

        when(empenhoRepository.findById("EMP001")).thenReturn(Optional.of(empenho));
        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        PagamentoDTO resultado = pagamentoService.salvar(dto);

        // Assert
        assertNotNull(resultado.getNumero());
        assertEquals("EMP001", resultado.getEmpenhoId());
        assertEquals("Pagamento parcial", dto.getObservacao());
        assertNotNull(resultado.getValorPagoFormatado());
        assertNotNull(resultado.getDataPagamentoFormatado());
        verify(despesaRepository).save(despesa);
    }

    @Test
    void deveLancarExcecaoQuandoEmpenhoNaoExiste() {
        when(empenhoRepository.findById("EMP999")).thenReturn(Optional.empty());

        PagamentoDTO dto = new PagamentoDTO();
        dto.setEmpenhoId("EMP999");
        dto.setValor(BigDecimal.TEN);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            pagamentoService.salvar(dto);
        });

        assertEquals("Empenho não encontrado", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoValorExcedeEmpenho() {
        Pagamento pagamentoExistente = new Pagamento();
        pagamentoExistente.setValor(new BigDecimal("450.00"));

        PagamentoDTO dto = new PagamentoDTO();
        dto.setEmpenhoId("EMP123");
        dto.setValor(new BigDecimal("100.00"));

        Empenho empenho = new Empenho();
        empenho.setNumero("EMP123");
        empenho.setValor(new BigDecimal("500.00"));
        empenho.setPagamentos(List.of(pagamentoExistente));

        when(empenhoRepository.findById("EMP123")).thenReturn(Optional.of(empenho));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            pagamentoService.salvar(dto);
        });

        assertEquals("Valor pago excede valor do empenho", ex.getMessage());
    }

    @Test
    void deveListarPagamentosPorEmpenho() {
        Empenho empenho = new Empenho();
        empenho.setNumero("EMP001");

        Pagamento p1 = new Pagamento();
        p1.setNumero("PAG001");
        p1.setData(LocalDate.of(2025, 8, 3));
        p1.setValor(new BigDecimal("200.00"));
        p1.setEmpenho(empenho);

        when(pagamentoRepository.findAll()).thenReturn(List.of(p1));

        List<PagamentoDTO> lista = pagamentoService.listarPorEmpenho("EMP001");

        assertEquals(1, lista.size());
        PagamentoDTO dto = lista.get(0);
        assertEquals("EMP001", dto.getEmpenhoId());
        assertEquals("R$ 200,00", dto.getValorPagoFormatado());
        assertEquals("03/08/2025", dto.getDataPagamentoFormatado());
    }
}
