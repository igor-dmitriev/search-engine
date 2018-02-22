package com.igor.search.server.controller;

import com.google.common.collect.ImmutableSet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igor.search.dto.DocumentDto;
import com.igor.search.server.config.DocumentControllerTestConf;
import com.igor.search.server.data.SearchCriteria;
import com.igor.search.server.entity.Document;
import com.igor.search.server.error.ResourceNotFoundException;
import com.igor.search.server.helper.Endpoints;
import com.igor.search.server.service.DocumentService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.SneakyThrows;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * Created by Igor Dmitriev on 2/21/18
 */
@RunWith(SpringRunner.class)
@WebMvcTest(DocumentController.class)
@Import(DocumentControllerTestConf.class)
public class DocumentControllerTest {
  private static final String DOCUMENT_SCHEMA = "schemas/document.json";
  private static final String ERROR_SCHEMA = "schemas/error.json";

  private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Autowired
  private MockMvc mvc;

  @MockBean
  private DocumentService documentService;

  @Before
  public void setUp() {
    RestAssuredMockMvc.mockMvc(mvc);
    reset(documentService);
  }

  @Test
  @SneakyThrows
  public void shouldSaveDocument() {
    DocumentDto document = DocumentDto.builder().key("test key").data("test data").build();

    when(documentService.save(Mockito.any(Document.class))).thenReturn(Document.builder().key("test key").data("test data").createdAt(LocalDateTime.now()).build());

    given()
        .contentType(ContentType.JSON)
        .body(OBJECT_MAPPER.writeValueAsString(document))
        .when()
        .post(Endpoints.DOCUMENTS)
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .body(matchesJsonSchemaInClasspath(DOCUMENT_SCHEMA))
        .body("key", is("test key"))
        .body("data", is("test data"));
  }

  @Test
  public void shouldGetDocumentById() {
    String key = "1";
    Document document = Document.builder().data("test data").key(key).createdAt(LocalDateTime.now()).build();
    when(documentService.getByKey(key)).thenReturn(document);

    given()
        .when()
        .get(Endpoints.DOCUMENTS + "/" + key)
        .then()
        .statusCode(HttpStatus.OK.value())
        .body(matchesJsonSchemaInClasspath(DOCUMENT_SCHEMA))
        .body("key", is(key))
        .body("data", is("test data"));
  }

  @Test
  public void shouldReturnKeysWhenSearchByTokens() {
    when(documentService.search(Mockito.any(SearchCriteria.class))).thenReturn(ImmutableSet.of("key1", "key2"));

    given()
        .queryParam("token", "test token")
        .when()
        .get(Endpoints.DOCUMENTS + Endpoints.DOCUMENTS_SEARCH)
        .then()
        .log()
        .all()
        .statusCode(HttpStatus.OK.value())
        .body("$", hasItems("key1", "key2"));
  }

  @Test
  @SneakyThrows
  public void shouldReturnBadRequestWhenKeyIsNull() {
    DocumentDto document = DocumentDto.builder().data("test data").build();
    given()
        .contentType(ContentType.JSON)
        .body(OBJECT_MAPPER.writeValueAsString(document))
        .when()
        .post(Endpoints.DOCUMENTS)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .body(matchesJsonSchemaInClasspath(ERROR_SCHEMA));
  }

  @Test
  @SneakyThrows
  public void shouldReturnBadRequestWhenDataIsNull() {
    DocumentDto documentDto = DocumentDto.builder().key("test_key").build();
    given()
        .contentType(ContentType.JSON)
        .body(OBJECT_MAPPER.writeValueAsString(documentDto))
        .when()
        .post(Endpoints.DOCUMENTS)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .body(matchesJsonSchemaInClasspath(ERROR_SCHEMA));
  }

  @Test
  @SneakyThrows
  public void shouldReturnBadRequestWhenDataAndKeyAreNull() {
    DocumentDto documentDto = DocumentDto.builder().build();
    given()
        .contentType(ContentType.JSON)
        .body(OBJECT_MAPPER.writeValueAsString(documentDto))
        .when()
        .post(Endpoints.DOCUMENTS)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .body(matchesJsonSchemaInClasspath(ERROR_SCHEMA));
  }

  @Test
  public void shouldReturnNotFoundWhenThereIsNoSuchDocument() {
    String key = "1";
    when(documentService.getByKey(key)).thenThrow(ResourceNotFoundException.class);

    given()
        .standaloneSetup(DocumentController.class)
        .when()
        .get(Endpoints.DOCUMENTS + "/" + key)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }
}
