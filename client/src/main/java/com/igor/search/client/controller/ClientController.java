package com.igor.search.client.controller;

import com.igor.search.client.client.ServerHttpClient;
import com.igor.search.dto.DocumentDto;
import com.igor.search.helper.Endpoints;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * Created by Igor Dmitriev on 2/21/18
 */
@RestController
@RequestMapping(Endpoints.DOCUMENTS)
@RequiredArgsConstructor
public class ClientController {

  // it could be a Feign client
  private final ServerHttpClient serverHttpClient;

  @PostMapping
  public ResponseEntity saveDocument(@RequestBody DocumentDto documentDto) {
    return serverHttpClient.saveDocument(documentDto);
  }

  @GetMapping(Endpoints.DOCUMENTS_KEY)
  public ResponseEntity getDocument(@PathVariable String key) {
    return serverHttpClient.getDocument(key);
  }

  @GetMapping(Endpoints.DOCUMENTS_SEARCH)
  public ResponseEntity searchDocumentKeys(@RequestParam("token") List<String> tokens) {
    return serverHttpClient.searchDocumentKeys(tokens);
  }

}
