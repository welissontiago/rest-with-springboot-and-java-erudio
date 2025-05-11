package br.com.welissontiago.file.exporter.implement;

import br.com.welissontiago.dto.v1.PersonDTO;
import br.com.welissontiago.file.exporter.contract.FileExporter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class PdfExporter implements FileExporter {

    @Override
    public Resource exportFile(List<PersonDTO> people) throws IOException {
        return null;
    }


}
