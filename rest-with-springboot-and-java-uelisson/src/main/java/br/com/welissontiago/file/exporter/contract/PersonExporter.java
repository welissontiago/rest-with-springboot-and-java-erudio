package br.com.welissontiago.file.exporter.contract;

import br.com.welissontiago.dto.v1.PersonDTO;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface PersonExporter {

    Resource exportPeople(List<PersonDTO> people) throws IOException;
    Resource exportPerson(PersonDTO person) throws IOException;

}
