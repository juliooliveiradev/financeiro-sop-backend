package com.sop.financeiro.service;

import com.sop.financeiro.dto.PagamentoDTO;
import com.sop.financeiro.exception.BadRequestException;
import com.sop.financeiro.exception.ResourceNotFoundException;
import com.sop.financeiro.model.Despesa;
import com.sop.financeiro.model.Empenho;
import com.sop.financeiro.model.Pagamento;
import com.sop.financeiro.repository.DespesaRepository;
import com.sop.financeiro.repository.EmpenhoRepository;
import com.sop.financeiro.repository.PagamentoRepository;
import com.sop.financeiro.util.DespesaStatusUtils;
import com.sop.financeiro.util.NumeroGeradorUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final EmpenhoRepository empenhoRepository;
    private final DespesaRepository despesaRepository;
    private final ModelMapper modelMapper;

    public PagamentoDTO salvar(PagamentoDTO dto) {
        Empenho empenho = empenhoRepository.findById(dto.getEmpenhoId())
                .orElseThrow(() -> new ResourceNotFoundException("Empenho não encontrado"));

        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setNumero(NumeroGeradorUtils.gerarNumeroPagamento());
        pagamento.setEmpenho(empenho);

        BigDecimal somaAtual = empenho.getPagamentos().stream()
                .map(Pagamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(dto.getValor());

        if (somaAtual.compareTo(empenho.getValor()) > 0) {
            throw new BadRequestException("Valor pago excede valor do empenho");
        }

        Pagamento salvo = pagamentoRepository.save(pagamento);

        // Atualiza status da despesa relacionada
        Despesa despesa = empenho.getDespesa();
        despesa.setStatus(DespesaStatusUtils.calcularStatus(despesa));
        despesaRepository.save(despesa);

        // Monta resposta com campos formatados
        PagamentoDTO resposta = modelMapper.map(salvo, PagamentoDTO.class);
        resposta.setEmpenhoId(salvo.getEmpenho().getNumero());

        // Formata data
        if (salvo.getData() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            resposta.setDataPagamentoFormatado(salvo.getData().format(formatter));
        }

        // Formata valor
        if (salvo.getValor() != null) {
            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            resposta.setValorPagoFormatado(nf.format(salvo.getValor()));
        }

        return resposta;
    }



    public List<PagamentoDTO> listarPorEmpenho(String numeroEmpenho) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        return pagamentoRepository.findAll().stream()
                .filter(p -> p.getEmpenho().getNumero().equals(numeroEmpenho))
                .map(p -> {
                    PagamentoDTO dto = modelMapper.map(p, PagamentoDTO.class);
                    dto.setEmpenhoId(p.getEmpenho().getNumero());

                    if (p.getData() != null) {
                        dto.setDataPagamentoFormatado(p.getData().format(dateFormatter));
                    }

                    if (p.getValor() != null) {
                        dto.setValorPagoFormatado(currencyFormatter.format(p.getValor()));
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

}


