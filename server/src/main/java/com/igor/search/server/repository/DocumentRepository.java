package com.igor.search.server.repository;

import com.igor.search.server.data.SearchCriteria;
import com.igor.search.server.entity.Document;

import java.util.Optional;
import java.util.Set;

/**
 * Created by Igor Dmitriev on 2/21/18
 */
public interface DocumentRepository {
  Document save(Document document);

  Optional<Document> findByKey(String key);

  Set<String> search(SearchCriteria searchCriteria);

  long count();
}
