package br.com.welissontiago.service;

import br.com.welissontiago.PersonRepository;
import br.com.welissontiago.dto.v1.PersonDTO;
import br.com.welissontiago.exceptions.ResourceNotFoundException;
import static br.com.welissontiago.mapper.ObjectMapper.parseListObject;
import static br.com.welissontiago.mapper.ObjectMapper.parseObject;
import br.com.welissontiago.model.PersonModel;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PersonService {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = LoggerFactory.getLogger(PersonService.class.getName());
    @Autowired
    PersonRepository personRepository;


    public PersonDTO findById(Long id) {
        logger.info("Find person by id: ");
        var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        return parseObject(entity, PersonDTO.class);
    }

    public List<PersonDTO> findAll(){
        logger.info("Find all peaple: ");
        return parseListObject(personRepository.findAll(), PersonDTO.class) ;
    }


    public PersonDTO create(PersonDTO person) {
        logger.info("Create person: " + person);
        var entity = parseObject(person, PersonModel.class);
        return parseObject(personRepository.save(entity), PersonDTO.class);
    }

    public PersonDTO update(PersonDTO person) {
        logger.info("update person: " + person);
        PersonModel entity = personRepository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        return parseObject(personRepository.save(entity), PersonDTO.class);
    }

    public void delete(Long id) {
        logger.info("delete one person: " );
        PersonModel entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        personRepository.delete(entity);
    }


}
