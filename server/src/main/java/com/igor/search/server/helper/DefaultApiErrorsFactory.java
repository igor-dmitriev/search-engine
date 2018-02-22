package com.igor.search.server.helper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DefaultApiErrorsFactory implements ApiErrorsFactory {

  private static final String INTERNAL_SERVER_ERROR_TITLE = "Server Error";
  private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Sorry, something went wrong";
  private static final String NOT_FOUND_MESSAGE = "Requested resource not found";

  @Override
  public ApiErrors createBadRequest(String message) {
    return createError(HttpStatus.BAD_REQUEST.getReasonPhrase(), message, HttpStatus.BAD_REQUEST);
  }

  @Override
  public ApiErrors createInternalServerError() {
    return createError(INTERNAL_SERVER_ERROR_TITLE, INTERNAL_SERVER_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ApiErrors createNotFound(String message) {
    return createError(NOT_FOUND_MESSAGE, message, HttpStatus.NOT_FOUND);
  }

  private ApiErrors createError(String title, String message, HttpStatus errorCode) {
    return new ApiErrors(Collections.singletonList(new ApiError(errorCode.value(), title, message)));
  }
}
