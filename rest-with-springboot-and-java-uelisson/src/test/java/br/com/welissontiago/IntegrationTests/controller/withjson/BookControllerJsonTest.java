package br.com.welissontiago.IntegrationTests.controller.withjson;

import br.com.welissontiago.IntegrationTests.dto.BookDTO;
import br.com.welissontiago.IntegrationTests.dto.wrapper.json.WrapperBookDTO;
import br.com.welissontiago.IntegrationTests.testcontainers.AbstractIntegrationTest;
import br.com.welissontiago.configs.TestConfigs;
import br.com.welissontiago.dto.v1.BooksDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static BookDTO book;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        book = new BookDTO();
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockBook();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .setBaseUri("http://localhost")
                .setPort(TestConfigs.SERVER_PORT)
                .setBasePath("/api/books/v1")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertNotNull(book.getId());
        assertEquals("Docker Deep Dive", book.getTitle());
        assertEquals("Nigel Poulton", book.getAuthor());
        assertEquals(55.99, book.getPrice());
    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {

        book.setTitle("Docker Deep Dive - Updated");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .when()
                .put()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);

        assertNotNull(createdBook.getId());
        assertNotNull(book.getId());
        assertEquals("Docker Deep Dive - Updated", book.getTitle());
        assertEquals("Nigel Poulton", book.getAuthor());
        assertEquals(55.99, book.getPrice());
    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", book.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);

        assertNotNull(createdBook.getId());
        assertNotNull(book.getId());
        assertEquals("Docker Deep Dive - Updated", book.getTitle());
        assertEquals("Nigel Poulton", book.getAuthor());
        assertEquals(55.99, book.getPrice());
    }

    @Test
    @Order(4)
    void deleteTest() throws JsonProcessingException {

        given(specification)
                .pathParam("id", book.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }


    @Test
    @Order(5)
    void findAllTest() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("page", 0 , "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();
        
        WrapperBookDTO wrapper = objectMapper.readValue(content, WrapperBookDTO.class);
        var books = wrapper.getEmbedded().getBooks();

        BooksDTO bookOne = books.get(0);

        assertNotNull(bookOne.getId());
        assertNotNull(bookOne.getTitle());
        assertNotNull(bookOne.getAuthor());
        assertNotNull(bookOne.getPrice());
        assertTrue(bookOne.getId() > 0);
        assertEquals("Implantando a governança de TI", bookOne.getTitle());
        assertEquals("Aguinaldo Aragon Fernandes e Vladimir Ferraz de Abreu", bookOne.getAuthor());
        assertEquals(54.00, bookOne.getPrice());

        BooksDTO foundBookFive = books.get(1);

        assertNotNull(foundBookFive.getId());
        assertNotNull(foundBookFive.getTitle());
        assertNotNull(foundBookFive.getAuthor());
        assertNotNull(foundBookFive.getPrice());
        assertTrue(foundBookFive.getId() > 0);
        assertEquals("The Pragmatic Programmer", foundBookFive.getTitle());
        assertEquals("Andrew Hunt e David Thomas", foundBookFive.getAuthor());
        assertEquals(115.58, foundBookFive.getPrice());
    }

    private void mockBook() {
        book.setTitle("Docker Deep Dive");
        book.setAuthor("Nigel Poulton");
        book.setPrice(Double.valueOf(55.99));
        book.setLaunch_date(new Date());
    }
}
