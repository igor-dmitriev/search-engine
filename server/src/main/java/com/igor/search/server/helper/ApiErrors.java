package com.igor.search.server.helper;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ApiErrors {
  private final List<ApiError> errors;
}
