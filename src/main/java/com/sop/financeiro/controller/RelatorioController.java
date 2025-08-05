package com.sop.financeiro.controller;

import com.sop.financeiro.dto.EmpenhoDTO;
import com.sop.financeiro.dto.PagamentoDTO;
import com.sop.financeiro.model.Despesa;
import com.sop.financeiro.repository.DespesaRepository;
import com.sop.financeiro.service.EmpenhoService;
import com.sop.financeiro.service.PagamentoService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@Tag(name = "Relatórios", description = "Geração de relatórios em PDF")
@SecurityRequirement(name = "bearerAuth")
public class RelatorioController {

    private final DespesaRepository despesaRepository;
    private final EmpenhoService empenhoService;
    private final PagamentoService pagamentoService;

    @Operation(summary = "Gerar relatório PDF de despesas")
    @GetMapping("/despesas")
    public void gerarRelatorio(HttpServletResponse response) throws Exception {
        List<Despesa> despesas = despesaRepository.findAll();

        InputStream jasperStream = getClass().getResourceAsStream("/reports/despesas.jrxml");
        JasperReport report = JasperCompileManager.compileReport(jasperStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(despesas);

        JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(), dataSource);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=relatorio-despesas.pdf");

        JasperExportManager.exportReportToPdfStream(print, response.getOutputStream());
    }

    @Operation(summary = "Gerar relatório PDF de empenhos por protocolo de despesa")
    @GetMapping("/empenhos")
    public void gerarRelatorioEmpenhos(HttpServletResponse response, @RequestParam String protocolo) throws Exception {
        List<EmpenhoDTO> empenhos = empenhoService.listarPorDespesa(protocolo);

        InputStream jasperStream = getClass().getResourceAsStream("/reports/empenhos.jrxml");
        JasperReport report = JasperCompileManager.compileReport(jasperStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(empenhos);
        JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(), dataSource);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=relatorio-empenhos.pdf");

        JasperExportManager.exportReportToPdfStream(print, response.getOutputStream());
    }

    @Operation(summary = "Gerar relatório PDF de pagamentos por número do empenho")
    @GetMapping("/pagamentos")
    public void gerarRelatorioPagamentos(HttpServletResponse response, @RequestParam String numero) throws Exception {
        List<PagamentoDTO> pagamentos = pagamentoService.listarPorEmpenho(numero);

        InputStream jasperStream = getClass().getResourceAsStream("/reports/pagamentos.jrxml");
        JasperReport report = JasperCompileManager.compileReport(jasperStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(pagamentos);
        JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(), dataSource);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=relatorio-pagamentos.pdf");

        JasperExportManager.exportReportToPdfStream(print, response.getOutputStream());
    }


}

