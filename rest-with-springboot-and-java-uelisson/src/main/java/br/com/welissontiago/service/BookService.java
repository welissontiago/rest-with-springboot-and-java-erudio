package br.com.welissontiago.service;

import br.com.welissontiago.controller.BooksController;
import br.com.welissontiago.controller.PersonController;
import br.com.welissontiago.dto.v1.BooksDTO;
import br.com.welissontiago.dto.v1.PersonDTO;
import br.com.welissontiago.exceptions.RequiredObjectisNullException;
import br.com.welissontiago.exceptions.ResourceNotFoundException;
import br.com.welissontiago.model.BooksModel;
import br.com.welissontiago.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.welissontiago.mapper.ObjectMapper.parseListObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import static br.com.welissontiago.mapper.ObjectMapper.parseObject;

@Service
public class BookService {

    @Autowired
    BooksRepository booksRepository;

    @Autowired
    PagedResourcesAssembler<BooksDTO> assembler;

    public BooksDTO findById(Long id) {
        var book = booksRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        var dto = parseObject(book, BooksDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PagedModel<EntityModel<BooksDTO>> findAll(Pageable pageable){
        var books = booksRepository.findAll(pageable);

        var booksWithLinks = books.map(book -> {
            var dto = parseObject(book, BooksDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BooksController.class).findAll(pageable.getPageNumber(),
                pageable.getPageSize(), String.valueOf(pageable.getSort()))).withSelfRel();

        return assembler.toModel(booksWithLinks, findAllLink);
    }

    public BooksDTO create(BooksDTO book) {
        if(book == null) throw new RequiredObjectisNullException();
        var theBook = parseObject(book, BooksModel.class);
        var dto = parseObject(booksRepository.save(theBook), BooksDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BooksDTO update(BooksDTO book) {
        if(book == null) throw new RequiredObjectisNullException();
        BooksModel theBook = booksRepository.findById(book.getId()).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        theBook.setAuthor(book.getAuthor());
        theBook.setLaunch_date(book.getLaunch_date());
        theBook.setPrice(book.getPrice());
        theBook.setTitle(book.getTitle());
        var dto = parseObject(booksRepository.save(theBook), BooksDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void deleteById(Long id) {
        BooksModel book = booksRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        booksRepository.delete(book);
    }


    private void addHateoasLinks(BooksDTO dto) {
        dto.add(linkTo(methodOn(BooksController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BooksController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(BooksController.class).findAll(1,12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BooksController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BooksController.class).update(dto)).withRel("update").withType("PUT"));
    }
}
