package com.igor.search.client.config;

import com.igor.search.client.client.ServerHttpClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Created by Igor Dmitriev on 2/21/18
 */
@Configuration
public class ClientConfig {

  @Value("${search.engine.server.endpoint}")
  private String serverEndpoint;

  @Value("${search.engine.server.http.client.read_timeout_millis:10000}")
  private int readTimeout;

  @Bean
  public ServerHttpClient serverHttpClient() {
    return new ServerHttpClient(restTemplate(), serverEndpoint);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplateBuilder()
        .setReadTimeout(readTimeout)
        .errorHandler(new NoOpResponseErrorHandler())
        .build();
  }

  private static class NoOpResponseErrorHandler extends DefaultResponseErrorHandler {
    private NoOpResponseErrorHandler() {
    }

    public void handleError(ClientHttpResponse response) throws IOException {
    }
  }
}
