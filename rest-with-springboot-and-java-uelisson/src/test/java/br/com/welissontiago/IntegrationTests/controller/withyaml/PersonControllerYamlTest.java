package br.com.welissontiago.IntegrationTests.controller.withyaml;

import br.com.welissontiago.IntegrationTests.controller.withyaml.mapper.YAMLMapper;
import br.com.welissontiago.IntegrationTests.dto.PersonDTO;
import br.com.welissontiago.IntegrationTests.dto.TokenDTO;
import br.com.welissontiago.IntegrationTests.dto.UserCredentialsDTO;
import br.com.welissontiago.IntegrationTests.dto.wrapper.xml.PagedModelPerson;
import br.com.welissontiago.IntegrationTests.testcontainers.AbstractIntegrationTest;
import br.com.welissontiago.configs.TestConfigs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YAMLMapper objectMapper;
    private static PersonDTO person;
    private static TokenDTO tokenDto;


    @BeforeAll
    static void setUp() {
        objectMapper = new YAMLMapper();
        person = new PersonDTO();
        tokenDto = new TokenDTO();
    }

    @Test
    @Order(0)
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
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockPerson();
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .setBaseUri("http://localhost")
                .setPort(TestConfigs.SERVER_PORT)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .setBasePath("/api/person/v1")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var createdPerson = given().config(RestAssuredConfig.config()
                .encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
        ).spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                    .body(person, objectMapper)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .as(PersonDTO.class, objectMapper);

        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Murilo",createdPerson.getFirstName());
        assertEquals("Moreninho",createdPerson.getLastName());
        assertEquals("Male",createdPerson.getGender());
        assertEquals("Salvador - BA",createdPerson.getAddress());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        person.setLastName("Moreno Marrom");


        var createdPerson = given().config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                ).spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                    .body(person, objectMapper)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                .as(PersonDTO.class, objectMapper);

        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Murilo",createdPerson.getFirstName());
        assertEquals("Moreno Marrom",createdPerson.getLastName());
        assertEquals("Male",createdPerson.getGender());
        assertEquals("Salvador - BA",createdPerson.getAddress());
        assertTrue(createdPerson.getEnabled());
    }


    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {
        var createdPerson = given().config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                ).spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(PersonDTO.class, objectMapper);

        person = createdPerson;

        assertNotNull(createdPerson.getId());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Murilo",createdPerson.getFirstName());
        assertEquals("Moreno Marrom",createdPerson.getLastName());
        assertEquals("Male",createdPerson.getGender());
        assertEquals("Salvador - BA",createdPerson.getAddress());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(4)
    void disableTest() throws JsonProcessingException {
        var createdPerson = given().config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                ).spec(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(PersonDTO.class, objectMapper);

        person = createdPerson;

        assertNotNull(createdPerson.getId());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Murilo",createdPerson.getFirstName());
        assertEquals("Moreno Marrom",createdPerson.getLastName());
        assertEquals("Male",createdPerson.getGender());
        assertEquals("Salvador - BA",createdPerson.getAddress());
        assertFalse(createdPerson.getEnabled());
    }

    @Test
    @Order(5)
    void deleteTest() throws JsonProcessingException {
        given(specification)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }


    @Test
    @Order(6)
    void findAllTest() throws JsonProcessingException {
        var content = given(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .queryParam("page", 3, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(PagedModelPerson.class, objectMapper);

        List<PersonDTO> persons = content.getContent();

        PersonDTO personOne = persons.get(0);

        assertNotNull(personOne.getId());

        assertTrue(personOne.getId() > 0);

        assertEquals("Allin",personOne.getFirstName());
        assertEquals("Emmot",personOne.getLastName());
        assertEquals("Male",personOne.getGender());
        assertEquals("7913 Lindbergh Way",personOne.getAddress());
        assertFalse(personOne.getEnabled());

        PersonDTO personTwo = persons.get(1);

        assertNotNull(personTwo.getId());

        assertTrue(personTwo.getId() > 0);

        assertEquals("Allin",personTwo.getFirstName());
        assertEquals("Otridge",personTwo.getLastName());
        assertEquals("Male",personTwo.getGender());
        assertEquals("09846 Independence Center",personTwo.getAddress());
        assertFalse(personTwo.getEnabled());
    }

    @Test
    @Order(7)
    void findByName() throws JsonProcessingException {
        var content = given(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("firstName", "and")
                .queryParam("page", 0, "size", 12, "direction", "asc")
                .when()
                .get("findPeopleName/{firstName}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(PagedModelPerson.class, objectMapper);

        List<PersonDTO> persons = content.getContent();

        PersonDTO personOne = persons.get(0);

        assertNotNull(personOne.getId());

        assertTrue(personOne.getId() > 0);

        assertEquals("Alessandro",personOne.getFirstName());
        assertEquals("McFaul",personOne.getLastName());
        assertEquals("Male",personOne.getGender());
        assertEquals("5 Lukken Plaza",personOne.getAddress());
        assertTrue(personOne.getEnabled());

        PersonDTO personTwo = persons.get(1);

        assertNotNull(personTwo.getId());

        assertTrue(personTwo.getId() > 0);

        assertEquals("Andrey",personTwo.getFirstName());
        assertEquals("Climar",personTwo.getLastName());
        assertEquals("Male",personTwo.getGender());
        assertEquals("77478 Northridge Point",personTwo.getAddress());
        assertTrue(personTwo.getEnabled());
    }


    private void mockPerson() {
        person.setFirstName("Murilo");
        person.setLastName("Moreninho");
        person.setAddress("Salvador - BA");
        person.setGender("Male");
        person.setEnabled(true);
    }
}