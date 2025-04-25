package br.com.welissontiago.service;

import br.com.welissontiago.controller.BooksController;
import br.com.welissontiago.dto.v1.BooksDTO;
import br.com.welissontiago.exceptions.RequiredObjectisNullException;
import br.com.welissontiago.exceptions.ResourceNotFoundException;
import br.com.welissontiago.model.BooksModel;
import br.com.welissontiago.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public BooksDTO findById(Long id) {
        var book = booksRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        var dto = parseObject(book, BooksDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public List<BooksDTO> findAll(){
        var books = parseListObject(booksRepository.findAll(), BooksDTO.class) ;
        books.forEach(this::addHateoasLinks);
        return books;
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
        dto.add(linkTo(methodOn(BooksController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BooksController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BooksController.class).update(dto)).withRel("update").withType("PUT"));
    }
}
