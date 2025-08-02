package com.sop.financeiro.service;

import com.sop.financeiro.dto.EmpenhoDTO;
import com.sop.financeiro.exception.BadRequestException;
import com.sop.financeiro.exception.ResourceNotFoundException;
import com.sop.financeiro.model.Despesa;
import com.sop.financeiro.model.Empenho;
import com.sop.financeiro.repository.DespesaRepository;
import com.sop.financeiro.repository.EmpenhoRepository;
import com.sop.financeiro.repository.PagamentoRepository;
import com.sop.financeiro.util.DespesaStatusUtils;
import com.sop.financeiro.util.FormatadorUtils;
import com.sop.financeiro.util.NumeroGeradorUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpenhoService {

    private final EmpenhoRepository empenhoRepository;
    private final DespesaRepository despesaRepository;
    private final ModelMapper modelMapper;

    public EmpenhoDTO salvar(EmpenhoDTO dto) {
        Despesa despesa = despesaRepository.findById(dto.getDespesaId())
                .orElseThrow(() -> new ResourceNotFoundException("Despesa não encontrada"));

        BigDecimal somaAtual = despesa.getEmpenhos().stream()
                .map(Empenho::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(dto.getValor());

        if (somaAtual.compareTo(despesa.getValor()) > 0) {
            throw new BadRequestException("Valor empenhado excede o valor da despesa");
        }

        Empenho empenho = modelMapper.map(dto, Empenho.class);
        empenho.setDespesa(despesa);
        empenho.setNumero(NumeroGeradorUtils.gerarNumeroEmpenho());
        empenho.setData(LocalDate.now());

        Empenho salvo = empenhoRepository.save(empenho);

        // Atualiza status da despesa
        despesa.setStatus(DespesaStatusUtils.calcularStatus(despesa));
        despesaRepository.save(despesa);

        // Monta DTO de resposta com campos formatados
        EmpenhoDTO resposta = modelMapper.map(salvo, EmpenhoDTO.class);
        resposta.setDataEmpenhoFormatado(FormatadorUtils.formatarData(salvo.getData()));
        resposta.setValorEmpenhadoFormatado(FormatadorUtils.formatarMoeda(salvo.getValor()));
        resposta.setDespesaId(salvo.getDespesa().getProtocolo());

        return resposta;
    }

    public List<EmpenhoDTO> listarPorDespesa(String protocoloDespesa) {
        return empenhoRepository.findAll().stream()
                .filter(e -> e.getDespesa().getProtocolo().equals(protocoloDespesa))
                .map(e -> {
                    EmpenhoDTO dto = modelMapper.map(e, EmpenhoDTO.class);
                    dto.setDespesaId(e.getDespesa().getProtocolo());

                    // Adiciona os campos formatados manualmente
                    dto.setDataEmpenhoFormatado(FormatadorUtils.formatarData(e.getData()));
                    dto.setValorEmpenhadoFormatado(FormatadorUtils.formatarMoeda(e.getValor()));

                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public void deletar(String numero) {
        Empenho empenho = empenhoRepository.findById(numero)
                .orElseThrow(() -> new ResourceNotFoundException("Empenho não encontrado"));

        // Regra de negócio: impedir exclusão se houver pagamentos vinculados
        if (!empenho.getPagamentos().isEmpty()) {
            throw new BadRequestException("Empenho possui pagamentos vinculados");
        }

        // Pega a despesa associada
        Despesa despesa = empenho.getDespesa();

        // Remove o vínculo entre a despesa e o empenho
        if (despesa != null) {
            despesa.getEmpenhos().remove(empenho);
        }

        // Remove o empenho do banco
        empenhoRepository.delete(empenho);

        // Atualiza o status da despesa após a remoção do empenho
        if (despesa != null) {
            despesa.setStatus(DespesaStatusUtils.calcularStatus(despesa));
            despesaRepository.save(despesa);
        }
    }
}


