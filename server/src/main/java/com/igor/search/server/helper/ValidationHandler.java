package com.igor.search.server.helper;

import com.igor.search.server.error.InvalidPayloadException;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import lombok.experimental.UtilityClass;

import static java.util.stream.Collectors.toList;

@UtilityClass
public class ValidationHandler {

  public void checkPayloadErrors(BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      List<FieldError> allErrors = bindingResult.getFieldErrors();

      List<String> allErrorMessages = allErrors.stream()
          .map(error -> error.getField() + " " +  error.getDefaultMessage())
          .collect(toList());

      throw new InvalidPayloadException("Payload validation failed", allErrorMessages);
    }
  }
}
