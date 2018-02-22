package com.igor.search.acceptance;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

/**
 * Created by Igor Dmitriev on 2/21/18
 */

@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = AcceptanceTest.Initializer.class)
public class AcceptanceTest {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final String CLIENT_SERVICE_NAME = "client_1";
  private static final String DOCUMENTS_ENDPOINT = "/documents";

  private static String clientHost = "localhost";
  private static final int CLIENT_SERVICE_PORT = 8000;
  private static int clientPort = CLIENT_SERVICE_PORT;

  @ClassRule
  public static DockerComposeContainer CONTAINERS =
      new DockerComposeContainer(new File("../docker-compose.yml"))
          .withLocalCompose(true)
          .withPull(false)
          .withExposedService(CLIENT_SERVICE_NAME, CLIENT_SERVICE_PORT);

  @Before
  @SneakyThrows
  public void setUp() {
    // TODO wait until all services is up, implement health check wait - https://github.com/testcontainers/testcontainers-java/issues/174
    Thread.sleep(10_000);
    RestAssured.baseURI = createUrl(clientHost, clientPort);
  }

  @Test
  public void happyPath() throws InterruptedException {
    Document firstDocumentToCreate = Document.builder().key("1").data("blue green").build();
    Document secondDocumentToCreate = Document.builder().key("2").data("blue red").build();
    Document thirdDocumentToCreate = Document.builder().key("3").data("blue green black").build();

    Document createdFirstDocument = createDocument(firstDocumentToCreate);
    Document createdSecondDocument = createDocument(secondDocumentToCreate);
    Document createdThirdDocument = createDocument(thirdDocumentToCreate);

    assertThat(firstDocumentToCreate, is(createdFirstDocument));
    assertThat(createdFirstDocument.getCreatedAt(), is(notNullValue()));

    assertThat(secondDocumentToCreate, is(createdSecondDocument));
    assertThat(createdSecondDocument.getCreatedAt(), is(notNullValue()));

    assertThat(thirdDocumentToCreate, is(createdThirdDocument));
    assertThat(createdThirdDocument.getCreatedAt(), is(notNullValue()));

    Document documentByKey = getDocumentByKey(firstDocumentToCreate.getKey());
    assertThat(documentByKey, is(createdFirstDocument));

    List<String> keysWithBlueTokens = searchDocumentsKeysByTokens(ImmutableMap.of("token", "blue"));
    List<String> keysWithGreenTokens = searchDocumentsKeysByTokens(ImmutableMap.of("token", "green"));
    List<String> keysWithBlackTokens = searchDocumentsKeysByTokens(ImmutableMap.of("token", "black"));
    List<String> keysWithBlackAndRedTokens = searchDocumentsKeysByTokens(ImmutableMap.of("token", ImmutableList.of("black", "red")));

    assertThat(keysWithBlueTokens, contains("1", "2", "3"));
    assertThat(keysWithGreenTokens, contains("1", "3"));
    assertThat(keysWithBlackTokens, contains("3"));
    assertThat(keysWithBlackAndRedTokens, contains("2", "3"));
  }

  @SneakyThrows
  private List<String> searchDocumentsKeysByTokens(Map<String, ?> tokens) {
    return RestAssured.given()
        .queryParams(tokens)
        .when()
        .get(DOCUMENTS_ENDPOINT + "/search")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract()
        .response()
        .jsonPath()
        .get();
  }

  @SneakyThrows
  private Document getDocumentByKey(String key) {
    String json = RestAssured.given()
        .when()
        .get(DOCUMENTS_ENDPOINT + "/" + key)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract()
        .response()
        .asString();
    return OBJECT_MAPPER.readValue(json, Document.class);
  }

  @SneakyThrows
  private Document createDocument(Document payload) {
    String json = RestAssured.given()
        .contentType(ContentType.JSON)
        .when()
        .body(OBJECT_MAPPER.writeValueAsString(payload))
        .post(DOCUMENTS_ENDPOINT)
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .extract()
        .response()
        .asString();
    return OBJECT_MAPPER.readValue(json, Document.class);
  }

  private static String createUrl(String host, Integer port) {
    return String.format("http://%s:%d", host, port);
  }

  static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
      AcceptanceTest.clientHost = CONTAINERS.getServiceHost(CLIENT_SERVICE_NAME, CLIENT_SERVICE_PORT);
      AcceptanceTest.clientPort = CONTAINERS.getServicePort(CLIENT_SERVICE_NAME, CLIENT_SERVICE_PORT);
    }

  }
}
