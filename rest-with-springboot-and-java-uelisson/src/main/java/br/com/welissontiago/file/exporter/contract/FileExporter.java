package br.com.welissontiago.file.exporter.contract;

import br.com.welissontiago.dto.v1.PersonDTO;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FileExporter {

    Resource exportFile(List<PersonDTO> people) throws IOException;
}
