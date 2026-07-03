package web.condominiodigital.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.condominiodigital.model.Unidade;
import web.condominiodigital.repository.UnidadeRepository;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

@Service
public class RelatorioService {

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Transactional(readOnly = true)
    public byte[] gerarRelatorioEncomendasPdf() throws Exception {
        // Carrega os arquivos JRXML
        InputStream principalStream = new org.springframework.core.io.ClassPathResource("relatorios/relatoriounidades.jrxml").getInputStream();
        InputStream subReportStream = new org.springframework.core.io.ClassPathResource("relatorios/relatorioencomendas.jrxml").getInputStream();

        // Compila os JRXML em tempo real (pois não estamos usando plugin Maven para compilar offline)
        JasperReport principalReport = JasperCompileManager.compileReport(principalStream);
        JasperReport subReport = JasperCompileManager.compileReport(subReportStream);

        // Busca todas as unidades (e suas encomendas devido ao mapeamento JPA)
        List<Unidade> unidades = unidadeRepository.findAll();

        // Cria o DataSource a partir da lista
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(unidades);

        // Parâmetros (passando o sub-relatório compilado)
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("subReport", subReport);

        // Preenche o relatório
        JasperPrint jasperPrint = JasperFillManager.fillReport(principalReport, parametros, dataSource);

        // Exporta para byte[] (PDF)
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
