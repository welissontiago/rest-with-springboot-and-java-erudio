package br.com.welissontiago.file.exporter.implement;

import br.com.welissontiago.dto.v1.PersonDTO;
import br.com.welissontiago.file.exporter.contract.PersonExporter;
import br.com.welissontiago.service.QrCodeService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PdfExporter implements PersonExporter {

    @Autowired
    private QrCodeService service;

    @Override
    public Resource exportPeople(List<PersonDTO> people) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/templates/People.jrxml");
        if (inputStream == null) {
            throw new RuntimeException("Template not found");
        }

        JasperPrint jasperPrint;
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);
            Map<String, Object> parameters = new HashMap<>();
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource exportPerson(PersonDTO person) throws IOException {
        InputStream mainTemplateStream = getClass().getResourceAsStream("/templates/person.jrxml");
        if (mainTemplateStream == null) {
            throw new RuntimeException("Template not found");
        }

        InputStream subReportStream = getClass().getResourceAsStream("/templates/books.jrxml");
        if (subReportStream  == null) {
            throw new RuntimeException("Template not found");
        }

        JasperPrint jasperPrint;
        try {
            JasperReport mainReport = JasperCompileManager.compileReport(mainTemplateStream);
            JasperReport subReport = JasperCompileManager.compileReport(subReportStream);

            JRBeanCollectionDataSource subDataSource = new JRBeanCollectionDataSource(person.getBooks());

            InputStream qrCodeStream = service.generateQRCode(person.getProfileUrl(), 200, 200);

            String path = getClass().getResource("/templates/books.jasper").getPath();

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("SUB_REPORT_DATA_SOURCE", subDataSource);
            parameters.put("BOOK_SUB_REPORT", subReport);
            parameters.put("SUB_REPORT_DIR", path);
            parameters.put("QR_CODE_IMAGE", qrCodeStream);

            JRBeanCollectionDataSource mainDataSource = new JRBeanCollectionDataSource(Collections.singletonList(person));
            jasperPrint = JasperFillManager.fillReport(mainReport, parameters, mainDataSource);
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }


}
