package br.com.welissontiago.unitetests.mapper;

import br.com.welissontiago.dto.v1.PersonDTO;
import br.com.welissontiago.model.PersonModel;

import java.util.ArrayList;
import java.util.List;

public class MockPerson {

    public PersonModel mockEntity() {return mockEntity(0);}

    public PersonDTO mockDTO() {return mockDTO(0);}

    public List<PersonModel> mockList() {
        List<PersonModel> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }
        return persons;
    }

    public List<PersonDTO> mockDTOList() {
        List<PersonDTO> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockDTO(i));
        }
        return persons;
    }

    public PersonModel mockEntity(Integer number) {
        PersonModel person = new PersonModel();
        person.setAddress("Address Test" + number);
        person.setFirstName("First Name Test" + number);
        person.setLastName("Last Name Test" + number);
        person.setGender(((number % 2 ) == 0) ? "Male" : "Female");
        person.setId(number.longValue());
        return person;
    }

    public PersonDTO mockDTO(Integer number) {
        PersonDTO person = new PersonDTO();
        person.setAddress("Address Test" + number);
        person.setFirstName("First Name Test" + number);
        person.setLastName("Last Name Test" + number);
        person.setGender(((number % 2 ) == 0) ? "Male" : "Female");
        person.setId(number.longValue());
        return person;
    }
}
