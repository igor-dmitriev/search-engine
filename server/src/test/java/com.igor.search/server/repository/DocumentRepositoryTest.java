package com.igor.search.server.repository;

import com.igor.search.server.entity.Document;
import com.igor.search.server.repository.impl.InMemoryDocumentRepository;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Igor Dmitriev on 2/21/18
 */

/*
  1. this is a regular unit test, cause storage is in memory, otherwise it should has been an integration one
  2. Spring is not needed here
 */
public class DocumentRepositoryTest {

  private DocumentRepository documentRepository;

  @Before
  public void setUp() {
    documentRepository = new InMemoryDocumentRepository();
  }

  @Test
  public void shouldSaveDocumentSuccessfully() {
    // given
    Document expected = Document.builder().key("test key").data("test data").build();

    // when
    documentRepository.save(expected);

    // then
    assertThat(documentRepository.count(), is(1L));
    Optional<Document> actual = documentRepository.findByKey("test key");
    assertThat(actual.isPresent(), is(true));
    assertThat(actual.get(), is(expected));
  }

  @Test
  public void shouldNotOverrideIfDocumentAlreadyExists() {
    // given
    String key = "test key";
    Document firstDocument = Document.builder().key(key).data("first data").build();
    Document secondDocument = Document.builder().key(key).data("second data").build();
    documentRepository.save(firstDocument);

    // when
    Document actual = documentRepository.save(secondDocument);

    // then
    assertThat(actual.getKey(), is(key));
    assertThat(actual.getData(), is("first data"));
  }

  // the other tests are omitted

}
