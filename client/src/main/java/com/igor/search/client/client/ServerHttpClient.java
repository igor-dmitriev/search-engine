package com.igor.search.client.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igor.search.dto.DocumentDto;
import com.igor.search.helper.Endpoints;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * Created by Igor Dmitriev on 2/21/18
 */

@RequiredArgsConstructor
public class ServerHttpClient {
  private final RestTemplate restTemplate;
  private final String serverEndpoint;

  public ResponseEntity saveDocument(DocumentDto documentDto) {
    ResponseEntity<String> responseFromServer = restTemplate.postForEntity(serverEndpoint + Endpoints.DOCUMENTS, documentDto, String.class);
    return ResponseEntity.status(responseFromServer.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(responseFromServer.getBody());
  }

  public ResponseEntity getDocument(String key) {
    ResponseEntity<String> responseFromServer = restTemplate.getForEntity(serverEndpoint + Endpoints.DOCUMENTS + "/" + key, String.class);
    return ResponseEntity.status(responseFromServer.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(responseFromServer.getBody());
  }

  public ResponseEntity searchDocumentKeys(List<String> tokens) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serverEndpoint + Endpoints.DOCUMENTS + Endpoints.DOCUMENTS_SEARCH);
    tokens.forEach(token -> builder.queryParam("token", token));
    ResponseEntity<List> responseFromServer = restTemplate.getForEntity(builder.build().encode().toUri(), List.class);
    return ResponseEntity.status(responseFromServer.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(responseFromServer.getBody());
  }
}
