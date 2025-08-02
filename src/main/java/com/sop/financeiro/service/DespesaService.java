package com.sop.financeiro.service;

import com.sop.financeiro.dto.DespesaDTO;
import com.sop.financeiro.exception.BadRequestException;
import com.sop.financeiro.exception.ResourceNotFoundException;
import com.sop.financeiro.model.Despesa;
import com.sop.financeiro.repository.DespesaRepository;
import com.sop.financeiro.util.FormatadorUtils;
import com.sop.financeiro.util.NumeroGeradorUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DespesaService {

    private final DespesaRepository despesaRepository;
    private final ModelMapper modelMapper;

    public DespesaDTO salvar(DespesaDTO dto) {
        Despesa despesa = modelMapper.map(dto, Despesa.class);
        despesa.setProtocolo(NumeroGeradorUtils.gerarProtocolo());
        despesa.setDataProtocolo(LocalDateTime.now());
        despesa.setStatus("Aguardando Empenho");

        Despesa salva = despesaRepository.save(despesa);
        DespesaDTO resposta = modelMapper.map(salva, DespesaDTO.class);
        preencherCamposFormatados(resposta, salva);
        return resposta;
    }

    public List<DespesaDTO> listarTodas() {
        return despesaRepository.findAll().stream()
                .map(d -> {
                    DespesaDTO dto = modelMapper.map(d, DespesaDTO.class);
                    preencherCamposFormatados(dto, d);
                    return dto;
                }).toList();
    }

    public DespesaDTO buscarPorId(String protocolo) {
        Despesa d = despesaRepository.findById(protocolo)
                .orElseThrow(() -> new ResourceNotFoundException("Despesa não encontrada"));
        DespesaDTO dto = modelMapper.map(d, DespesaDTO.class);
        preencherCamposFormatados(dto, d);
        return dto;
    }

    public DespesaDTO atualizar(String protocolo, DespesaDTO dto) {
        buscarPorId(protocolo); // apenas para validar existência
        dto.setProtocolo(protocolo);
        Despesa atualizada = despesaRepository.save(modelMapper.map(dto, Despesa.class));
        return modelMapper.map(atualizada, DespesaDTO.class);
    }

    public void deletar(String protocolo) {
        Despesa d = despesaRepository.findById(protocolo)
                .orElseThrow(() -> new ResourceNotFoundException("Despesa não encontrada"));
        if (!d.getEmpenhos().isEmpty()) {
            throw new BadRequestException("Não pode excluir uma despesa com empenhos");
        }
        despesaRepository.delete(d);
    }

    private void preencherCamposFormatados(DespesaDTO dto, Despesa entidade) {
        dto.setDataProtocoloFormatado(FormatadorUtils.formatarDataHora(entidade.getDataProtocolo()));
        dto.setDataVencimentoFormatado(FormatadorUtils.formatarData(entidade.getDataVencimento()));
        dto.setValorFormatado(FormatadorUtils.formatarMoeda(entidade.getValor()));
    }
}


