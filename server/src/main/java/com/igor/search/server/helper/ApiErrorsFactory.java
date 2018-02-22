package com.igor.search.server.helper;

public interface ApiErrorsFactory {
  ApiErrors createBadRequest(String message);

  ApiErrors createInternalServerError();

  ApiErrors createNotFound(String message);
}
