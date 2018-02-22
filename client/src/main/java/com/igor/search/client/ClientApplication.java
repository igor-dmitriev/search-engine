package com.igor.search.client;

import com.igor.search.client.config.ClientConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Created by Igor Dmitriev on 2/21/18
 */
@SpringBootApplication
@Import(ClientConfig.class)
public class ClientApplication {
  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }
}
