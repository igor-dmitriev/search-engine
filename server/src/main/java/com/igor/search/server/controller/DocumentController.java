package com.igor.search.server.controller;

import com.igor.search.dto.DocumentDto;
import com.igor.search.server.entity.Document;
import com.igor.search.server.helper.ValidationHandler;
import com.igor.search.server.mapper.DocumentMapper;
import com.igor.search.server.helper.Endpoints;
import com.igor.search.server.service.DocumentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

/**
 * Created by Igor Dmitriev on 2/21/18
 */
@RestController
@RequestMapping(Endpoints.DOCUMENTS)
@RequiredArgsConstructor
public class DocumentController {

  private final DocumentService documentService;
  private final DocumentMapper documentMapper;

  /**
   * Create a document
   * @param documentDto a document that is going to be saved
   * @return Returns successfully created document
   */
  @PostMapping
  public ResponseEntity<DocumentDto> saveDocument(@Valid @RequestBody DocumentDto documentDto, BindingResult bindingResult) {
    ValidationHandler.checkPayloadErrors(bindingResult);
    Document document = documentService.save(documentMapper.map(documentDto));
    return ResponseEntity.status(HttpStatus.CREATED).body(documentMapper.map(document));
  }

  /**
   * Get a document by key
   * @param key key of the document
   * @return Returns the document to which the specified key is mapped
   */
  @GetMapping(Endpoints.DOCUMENTS_KEY)
  public ResponseEntity<DocumentDto> getDocument(@PathVariable String key) {
    Document document = documentService.getByKey(key);
    return ResponseEntity.status(HttpStatus.OK).body(documentMapper.map(document));
  }

  /**
   * Search documents keys by tokens, where token is a word inside a document
   * @param tokens list of words
   * @return Returns key set of all documents which contain corresponding tokens
   */
  @GetMapping(Endpoints.DOCUMENTS_SEARCH)
  public ResponseEntity<Set> searchDocumentKeys(@RequestParam("token") List<String> tokens) {
    Set<String> keys = documentService.search(documentMapper.map(tokens));
    return ResponseEntity.status(HttpStatus.OK).body(keys);
  }

}
