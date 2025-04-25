package br.com.welissontiago.unitetests.mapper;

import br.com.welissontiago.dto.v1.BooksDTO;
import br.com.welissontiago.model.BooksModel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBook {

    public BooksModel mockEntity() {return mockEntity(0);}

    public BooksDTO mockDTO() {return mockDTO(0);}

    public List<BooksModel> mockList() {
        List<BooksModel> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BooksDTO> mockDTOList() {
        List<BooksDTO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockDTO(i));
        }
        return books;
    }

    public BooksModel mockEntity(Integer number) {
        BooksModel book = new BooksModel();
        book.setAuthor("Author Test" + number);
        book.setLaunch_date(new Date());
        book.setPrice(50.0);
        book.setTitle("Title Test" + number);
        book.setId(number.longValue());
        return book;
    }

    public BooksDTO mockDTO(Integer number) {
        BooksDTO book = new BooksDTO();
        book.setAuthor("Author Test" + number);
        book.setLaunch_date(new Date());
        book.setPrice(50.0);
        book.setTitle("Title Test" + number);
        book.setId(number.longValue());
        return book;
    }
}
