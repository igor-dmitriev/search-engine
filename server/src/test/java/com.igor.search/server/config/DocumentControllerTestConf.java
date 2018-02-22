package com.igor.search.server.config;

import com.igor.search.server.helper.ApiErrorsFactory;
import com.igor.search.server.helper.DefaultApiErrorsFactory;
import com.igor.search.server.mapper.DocumentMapper;
import com.igor.search.server.mapper.DocumentMapperImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Igor Dmitriev on 2/21/18
 */
@Configuration
public class DocumentControllerTestConf {
  @Bean
  public DocumentMapper documentMapper() {
    return new DocumentMapperImpl();
  }

  @Bean
  public ApiErrorsFactory apiErrorsFactory() {
    return new DefaultApiErrorsFactory();
  }
}
