package com.igor.search.server.service.impl;

import com.igor.search.server.data.SearchCriteria;
import com.igor.search.server.entity.Document;
import com.igor.search.server.error.ResourceNotFoundException;
import com.igor.search.server.repository.DocumentRepository;
import com.igor.search.server.service.DocumentService;

import org.springframework.stereotype.Service;

import java.util.Set;

import lombok.RequiredArgsConstructor;

/**
 * Created by Igor Dmitriev on 2/21/18
 */
@Service
@RequiredArgsConstructor
public class DefaultDocumentService implements DocumentService {
  private final DocumentRepository documentRepository;

  @Override
  public Document save(Document document) {
    return documentRepository.save(document);
  }

  @Override
  public Document getByKey(String key) {
    return documentRepository.findByKey(key)
        .orElseThrow(() -> new ResourceNotFoundException("Document not found with key: " + key));
  }

  @Override
  public Set<String> search(SearchCriteria searchCriteria) {
    return documentRepository.search(searchCriteria);
  }
}
