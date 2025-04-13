package br.com.welissontiago.service;

import br.com.welissontiago.model.PersonModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonService {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    public PersonModel findById(Long id) {
        logger.info("Find person by id: " + id);
        PersonModel person = new PersonModel();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Uelisson");
        person.setLastName("Santos");
        person.setAddress("BA");
        person.setGender("M");
        return person;
    }

    public List<PersonModel> findAll(){
        logger.info("Find all peaple: ");
        List<PersonModel> persons = new ArrayList<PersonModel>();
        for (int i = 0; i < 8; i++) {
            PersonModel person = mockPerson(i);
            persons.add(person);
        }
        return persons;
    }

    private PersonModel mockPerson(int i) {
        PersonModel person = new PersonModel();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Pato " + i);
        person.setLastName("Donald" + i);
        person.setAddress("BA");
        person.setGender("M");
        return person;
    }

    public PersonModel create(PersonModel person) {
        logger.info("Create person: " + person);
        return person;
    }

    public PersonModel update(PersonModel person) {
        logger.info("update person: " + person);
        return person;
    }

    public void delete(Long id) {
        logger.info("delete one person: " );
    }
}
