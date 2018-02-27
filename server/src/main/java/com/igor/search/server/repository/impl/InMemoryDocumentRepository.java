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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.util.stream.Collectors.toSet;

/**
 * Created by Igor Dmitriev on 2/21/18
 */
@Repository
public class InMemoryDocumentRepository implements DocumentRepository {

  private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
  private final Lock readLock = readWriteLock.readLock();
  private final Lock writeLock = readWriteLock.writeLock();

  private static final String WORD_SPLIT_PATTERN = "\\s+";

  private final Map<String, Document> storage = new HashMap<>();
  private final Multimap<String, Document> index = ArrayListMultimap.create();

  @Override
  public Document save(Document document) {
    writeLock.lock();
    try {
      Document alreadyExistingDocument = storage.get(document.getKey());
      if (alreadyExistingDocument != null) {
        return alreadyExistingDocument;
      }

      Set<String> words = new HashSet<>(Arrays.asList(document.getData().split(WORD_SPLIT_PATTERN)));
      document.setCreatedAt(LocalDateTime.now());
      words.forEach(word -> index.put(word, document));
      storage.put(document.getKey(), document);
      return document;
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public Optional<Document> findByKey(String key) {
    readLock.lock();
    try {
      return Optional.ofNullable(storage.get(key));
    } finally {
      readLock.unlock();
    }
  }

  @Override
  public Set<String> search(SearchCriteria searchCriteria) {
    readLock.lock();
    try {
      return searchCriteria.getTokens().stream()
          .map(index::get)
          .flatMap(Collection::stream)
          .map(Document::getKey)
          .collect(toSet());
    } finally {
      readLock.unlock();
    }
  }

  @Override
  public long count() {
    readLock.lock();
    try {
      return storage.size();
    } finally {
      readLock.unlock();
    }
  }
}
