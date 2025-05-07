package br.com.welissontiago.service;

import br.com.welissontiago.dto.v1.BooksDTO;
import br.com.welissontiago.exceptions.RequiredObjectisNullException;
import br.com.welissontiago.model.BooksModel;
import br.com.welissontiago.repository.BooksRepository;
import br.com.welissontiago.unitetests.mapper.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BooksRepository Repository;

    @InjectMocks
    private BookService service;

    MockBook input;

    @BeforeEach
    void setUp() {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        BooksModel person = input.mockEntity(1);
        person.setId(1L);
        when(Repository.findById(1L)).thenReturn(Optional.of(person));
        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self") && link.getHref().endsWith("/api/books/v1/1")
                && link.getType().equals("GET")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll") && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create") && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update") && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete") && link.getHref().endsWith("/api/books/v1/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Author Test1", result.getAuthor());
        assertEquals(50.0, result.getPrice());
        assertEquals("Title Test1", result.getTitle());
        assertNotNull(result.getLaunch_date());
    }



    @Test
    void findAll() {
    }

    @Test
    void create() {
        BooksDTO personDTO = input.mockDTO(1);
        BooksModel persisted = input.mockEntity(1);
        persisted.setId(1L);

        when(Repository.save(any(BooksModel.class))).thenReturn(persisted);

        var result = service.create(personDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self") && link.getHref().endsWith("/api/books/v1/1")
                        && link.getType().equals("GET"))
        );
        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create") && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("POST"))
        );
        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update") && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("PUT"))
        );
        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete") && link.getHref().endsWith("/api/books/v1/1")
                        && link.getType().equals("DELETE"))
        );

        assertEquals("Author Test1", result.getAuthor());
        assertEquals(50.0, result.getPrice());
        assertEquals("Title Test1", result.getTitle());
        assertNotNull(result.getLaunch_date());
    }

    @Test
    void testCreateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectisNullException.class, () -> {
            service.create(null);
        });

        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        BooksDTO existingBooks = input.mockDTO(1);
        BooksModel existing = input.mockEntity(1);
        existing.setId(1L);

        BooksModel persisted = input.mockEntity(1);
        persisted.setId(1L);

        when(Repository.findById(1L)).thenReturn(Optional.of(existing));
        when(Repository.save(any(BooksModel.class))).thenReturn(persisted);

        var result = service.update(existingBooks);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self") && link.getHref().endsWith("/api/books/v1/1")
                        && link.getType().equals("GET"))
        );
        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create") && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("POST"))
        );
        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update") && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("PUT"))
        );
        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete") && link.getHref().endsWith("/api/books/v1/1")
                        && link.getType().equals("DELETE"))
        );

        assertEquals("Author Test1", result.getAuthor());
        assertEquals(50.0, result.getPrice());
        assertEquals("Title Test1", result.getTitle());
        assertNotNull(result.getLaunch_date());
    }

    @Test
    void testUpdateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectisNullException.class, () -> {
            service.update(null);
        });

        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteById() {
        BooksModel books = input.mockEntity(1);
        books.setId(1L);
        when(Repository.findById(1L)).thenReturn(Optional.of(books));
        service.deleteById(1L);
        verify(Repository, times(1)).findById(anyLong());
        verify(Repository, times(1)).delete(any(BooksModel.class));
        verifyNoMoreInteractions(Repository);
    }
}