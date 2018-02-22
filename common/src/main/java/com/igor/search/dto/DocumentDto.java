package com.igor.search.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Igor Dmitriev on 2/21/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDto {
  @NotNull
  @Size(min = 1)
  private String key;

  @NotNull
  @Size(min = 1)
  private String data;

  private LocalDateTime createdAt;
}
