package com.igor.search.server.service;

import com.igor.search.server.entity.Document;
import com.igor.search.server.error.ResourceNotFoundException;
import com.igor.search.server.repository.DocumentRepository;
import com.igor.search.server.service.impl.DefaultDocumentService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Igor Dmitriev on 2/21/18
 */

/*
Our DocumentService layer code is dependent on our repository. However, to test the service layer,
we do not need to know or care about how the persistence layer is implemented.
We just test business logic.
 */

@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceTest {
  @Autowired
  private DocumentService documentService;

  @Mock
  private DocumentRepository documentRepository;

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Before
  public void setUp() {
    documentService = new DefaultDocumentService(documentRepository);
  }

  @Test
  public void shouldFindDocumentByKey() {
    // given
    Document expected = Document.builder().key("test key").data("test data").build();
    when(documentRepository.findByKey("test key")).thenReturn(Optional.of(expected));

    // when
    Document actual = documentService.getByKey("test key");

    // then
    assertThat(actual.getKey(), is("test key"));
  }

  @Test
  public void shouldNotFindDocumentByNotExistentKey() {
    // given
    when(documentRepository.findByKey("test key")).thenReturn(Optional.empty());
    exception.expect(ResourceNotFoundException.class);
    exception.expectMessage(containsString("test key"));

    // when
    documentService.getByKey("test key");
  }

  /*
   save and search methods are not covered cause they are dumb,
   there is no business logic, they are dumb
  */
}
