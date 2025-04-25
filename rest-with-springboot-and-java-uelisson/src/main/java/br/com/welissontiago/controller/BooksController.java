package br.com.welissontiago.controller;

import br.com.welissontiago.controller.docs.BooksControllerDocs;
import br.com.welissontiago.dto.v1.BooksDTO;
import br.com.welissontiago.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books/v1")
@Tag(name = "books", description = "Endpoins for manage books")

public class BooksController implements BooksControllerDocs {
    @Autowired
    private BookService bookService;

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE})
    @Override
    public BooksDTO findById(@PathVariable("id") Long id) {
        return bookService.findById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE})
    @Override
    public List<BooksDTO> findAll() {
        return bookService.findAll();
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE})
    @Override
    public BooksDTO create(@RequestBody BooksDTO booksDTO) {
        return bookService.create(booksDTO);
    }

    @PutMapping (produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE})
    @Override
    public BooksDTO update(@RequestBody BooksDTO booksDTO) {
        return bookService.update(booksDTO);
    }

    @DeleteMapping (value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
