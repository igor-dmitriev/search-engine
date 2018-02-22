package com.igor.search.server.data;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Igor Dmitriev on 2/21/18
 */
@RequiredArgsConstructor
@Getter
public class SearchCriteria {
  private final List<String> tokens;
}
