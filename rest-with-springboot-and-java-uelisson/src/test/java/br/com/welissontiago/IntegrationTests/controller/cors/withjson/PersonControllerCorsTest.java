package br.com.welissontiago.IntegrationTests.controller.cors.withjson;

import br.com.welissontiago.IntegrationTests.dto.PersonDTO;
import br.com.welissontiago.IntegrationTests.dto.TokenDTO;
import br.com.welissontiago.IntegrationTests.dto.UserCredentialsDTO;
import br.com.welissontiago.IntegrationTests.testcontainers.AbstractIntegrationTest;
import br.com.welissontiago.configs.TestConfigs;
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

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerCorsTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonDTO person;
    private static TokenDTO tokenDto;


    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        person = new PersonDTO();
        tokenDto = new TokenDTO();
    }

    @Test
    @Order(1)
    void signin() {
        UserCredentialsDTO credentials =
                new UserCredentialsDTO("leandro", "admin123");

        tokenDto = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class);

        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());
    }

    @Test
    @Order(2)
    void create() throws JsonProcessingException {
        mockPerson();
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_3000)
                .setBaseUri("http://localhost")
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .setPort(TestConfigs.SERVER_PORT)
                .setBasePath("/api/person/v1")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(person)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();;

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getGender());
        assertNotNull(createdPerson.getAddress());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Samara",createdPerson.getFirstName());
        assertEquals("Parceroti",createdPerson.getLastName());
        assertEquals("Female",createdPerson.getGender());
        assertEquals("Salvador - BA",createdPerson.getAddress());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(3)
    void createWithWrongOrigin() throws JsonProcessingException {
        mockPerson();
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_UELISSON)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .setBaseUri("http://localhost")
                .setPort(TestConfigs.SERVER_PORT)
                .setBasePath("/api/person/v1")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(person)
                .when()
                    .post()
                .then()
                    .statusCode(403)
                .extract()
                    .body()
                        .asString();;

        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(4)
    void findById() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .setBaseUri("http://localhost")
                .setPort(TestConfigs.SERVER_PORT)
                .setBasePath("/api/person/v1")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();;

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getGender());
        assertNotNull(createdPerson.getAddress());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Samara",createdPerson.getFirstName());
        assertEquals("Parceroti",createdPerson.getLastName());
        assertEquals("Female",createdPerson.getGender());
        assertEquals("Salvador - BA",createdPerson.getAddress());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(5)
    void findByIdWithWrongOrigin() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_UELISSON)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .setBaseUri("http://localhost")
                .setPort(TestConfigs.SERVER_PORT)
                .setBasePath("/api/person/v1")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();;

        assertEquals("Invalid CORS request", content);
    }

    private void mockPerson() {
        person.setFirstName("Samara");
        person.setLastName("Parceroti");
        person.setAddress("Salvador - BA");
        person.setGender("Female");
        person.setEnabled(true);
    }
}