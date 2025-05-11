package br.com.welissontiago.file.importer.contract;

import br.com.welissontiago.dto.v1.PersonDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws IOException;
}
