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
        InputStream principalStream = new org.springframework.core.io.ClassPathResource("relatorios/relatoriounidades.jrxml").getInputStream();
        InputStream subReportStream = new org.springframework.core.io.ClassPathResource("relatorios/relatorioencomendas.jrxml").getInputStream();

        JasperReport principalReport = JasperCompileManager.compileReport(principalStream);
        JasperReport subReport = JasperCompileManager.compileReport(subReportStream);

        List<Unidade> unidades = unidadeRepository.findAll();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(unidades);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("subReport", subReport);

        JasperPrint jasperPrint = JasperFillManager.fillReport(principalReport, parametros, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
