package br.com.welissontiago.service;

import br.com.welissontiago.exceptions.BadRequestException;
import br.com.welissontiago.exceptions.FileStorageException;
import br.com.welissontiago.file.exporter.contract.PersonExporter;
import br.com.welissontiago.file.exporter.factory.FileExporterFactory;
import br.com.welissontiago.file.importer.contract.FileImporter;
import br.com.welissontiago.file.importer.factory.FileImporterFactory;
import br.com.welissontiago.repository.PersonRepository;
import br.com.welissontiago.controller.PersonController;
import br.com.welissontiago.dto.v1.PersonDTO;
import br.com.welissontiago.exceptions.RequiredObjectisNullException;
import br.com.welissontiago.exceptions.ResourceNotFoundException;

import static br.com.welissontiago.mapper.ObjectMapper.parseObject;
import br.com.welissontiago.model.PersonModel;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private Logger logger = LoggerFactory.getLogger(PersonService.class.getName());

    @Autowired
    PersonRepository personRepository;

    @Autowired
    FileImporterFactory importer;

    @Autowired
    FileExporterFactory exporter;

    @Autowired
    PagedResourcesAssembler<PersonDTO> assembler;


    public PersonDTO findById(Long id) {
        logger.info("Find person by id: ");
        var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable){
        var people = personRepository.findAll(pageable);
        return buildPagedModel(pageable, people);
    }


    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Pageable pageable, Page<PersonModel> people) {
        var peopleWithLinks = people.map(person -> {
            var dto = parseObject(person, PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).findAll(pageable.getPageNumber(),
                pageable.getPageSize(), String.valueOf(pageable.getSort()))).withSelfRel();

        return assembler.toModel(peopleWithLinks, findAllLink);
    }

    public PagedModel<EntityModel<PersonDTO>> findByName(String firstName, Pageable pageable){

        var people = personRepository.findPeopleByName(firstName,pageable);

        return buildPagedModel(pageable, people);
    }

    public Resource exportPage(Pageable pageable, String acceptHeader){
        logger.info("Export people page");
        var people = personRepository.findAll(pageable).map(p -> parseObject(p, PersonDTO.class)).getContent();
        try {
            PersonExporter exporter = this.exporter.getExporter(acceptHeader);
            return exporter.exportPeople(people);
        } catch (Exception e) {
            throw new RuntimeException("Error export file",e);
        }
    }


    public PersonDTO create(PersonDTO person) {
        if(person == null) throw new RequiredObjectisNullException();
        logger.info("Create person: " + person);
        var entity = parseObject(person, PersonModel.class);
        var dto = parseObject(personRepository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public List<PersonDTO> massCreation(MultipartFile file){
        logger.info("Importing people from file: " + file);
        if(file.isEmpty()) throw new BadRequestException("Invalid File, please set a valid file");
        try(InputStream inputStream = file.getInputStream()) {
            String fileName = Optional.ofNullable(file.getOriginalFilename()).orElseThrow(() -> new BadRequestException("file name cannot be null"));
            FileImporter importer = this.importer.getImporter(fileName);
            List<PersonModel> entitys = importer.importFile(inputStream).stream()
                    .map(dto -> personRepository.save(parseObject(dto, PersonModel.class)))
                    .toList();

            return entitys.stream().map(entity -> {
                var dto = parseObject(entity, PersonDTO.class);
                addHateoasLinks(dto);
                return dto;
            }).toList();
        }catch(Exception e){
            throw new FileStorageException("Error importing file");
        }
    }

    public PersonDTO update(PersonDTO person) {
        if(person == null) throw new RequiredObjectisNullException();
        logger.info("update person: " + person);
        PersonModel entity = personRepository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        entity.setEnabled(person.getEnabled());
        entity.setPhotoUrl(person.getPhotoUrl());
        entity.setProfileUrl(person.getProfileUrl());
        var dto = parseObject(personRepository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        logger.info("delete one person: " );
        PersonModel entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        personRepository.delete(entity);
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("Disable one person: " );
        personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        personRepository.disablePerson(id);
        var entity = personRepository.findById(id).get();
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public Resource exportPerson(Long id, String acceptHeard) {
        logger.info("Exporting data of one person ");
        var person = personRepository.findById(id)
                .map(entity -> parseObject(entity, PersonDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        try {
            PersonExporter exporter = this.exporter.getExporter(acceptHeard);
            return exporter.exportPerson(person);
        } catch (Exception e) {
            throw new RuntimeException("Error export file", e);
        }

    }



    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(1,12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findByName("",1,12, "asc")).withRel("findByName").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class)).slash("massiveCreate").withRel("massiveCreate").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).exportPage(1,12, "asc", null)).withRel("exportPage").withType("GET")
                .withTitle("Export people"));
    }

}
