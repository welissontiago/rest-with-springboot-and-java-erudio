package br.com.welissontiago.IntegrationTests.dto.wrapper.json;

import br.com.welissontiago.dto.v1.BooksDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class BookEmbeddedDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("Books")
    private List<BooksDTO> Books;

    public BookEmbeddedDTO() {}

    public List<BooksDTO> getBooks() {
        return Books;
    }

    public void setBooks(List<BooksDTO> books) {
        Books = books;
    }
}
