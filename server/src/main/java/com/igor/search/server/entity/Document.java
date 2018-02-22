package com.igor.search.server.entity;

import org.hibernate.validator.constraints.NotEmpty;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Igor Dmitriev on 2/21/18
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "key")
public class Document {
  @NotEmpty
  private String key;
  @NotEmpty
  private String data;
  private LocalDateTime createdAt;
}
