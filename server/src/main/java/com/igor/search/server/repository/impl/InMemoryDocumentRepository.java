package com.igor.search.server.repository.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import com.igor.search.server.data.SearchCriteria;
import com.igor.search.server.entity.Document;
import com.igor.search.server.repository.DocumentRepository;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Created by Igor Dmitriev on 2/21/18
 */
@Repository
public class InMemoryDocumentRepository implements DocumentRepository {

  private static final String WORD_SPLIT_PATTERN = "\\s+";

  private final Map<String, Document> storage = new HashMap<>();
  private final Multimap<String, Document> index = ArrayListMultimap.create();

  @Override
  public Document save(Document document) {
    Document alreadyExistingDocument = storage.get(document.getKey());
    if (alreadyExistingDocument != null) {
      return alreadyExistingDocument;
    }

    Set<String> words = new HashSet<>(Arrays.asList(document.getData().split(WORD_SPLIT_PATTERN)));
    document.setCreatedAt(LocalDateTime.now());
    words.forEach(word -> index.put(word, document));
    storage.put(document.getKey(), document);
    return document;
  }

  @Override
  public Optional<Document> findByKey(String key) {
    return Optional.ofNullable(storage.get(key));
  }

  @Override
  public Set<String> search(SearchCriteria searchCriteria) {
    return searchCriteria.getTokens().stream()
        .map(index::get)
        .flatMap(Collection::stream)
        .map(Document::getKey)
        .collect(toSet());
  }

  @Override
  public long count() {
    return storage.size();
  }
}
