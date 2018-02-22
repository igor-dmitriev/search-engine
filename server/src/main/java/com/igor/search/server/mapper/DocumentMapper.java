package com.igor.search.server.mapper;

import com.igor.search.dto.DocumentDto;
import com.igor.search.server.data.SearchCriteria;
import com.igor.search.server.entity.Document;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Created by Igor Dmitriev on 2/21/18
 */

@Mapper(config = MappingConfig.class)
public interface DocumentMapper {
  @Mapping(target = "createdAt", ignore = true)
  Document map(DocumentDto documentDto);

  DocumentDto map(Document document);

  default SearchCriteria map(List<String> tokens) {
    return new SearchCriteria(tokens);
  }
}
