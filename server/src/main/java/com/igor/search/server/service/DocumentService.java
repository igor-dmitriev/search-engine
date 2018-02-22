package com.igor.search.server.service;

import com.igor.search.server.data.SearchCriteria;
import com.igor.search.server.entity.Document;

import java.util.Set;

/**
 * Created by Igor Dmitriev on 2/21/18
 */
public interface DocumentService {
  Document save(Document document);

  Document getByKey(String key);

  Set<String> search(SearchCriteria searchCriteria);
}
