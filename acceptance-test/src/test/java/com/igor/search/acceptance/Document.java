package com.igor.search.acceptance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by Igor Dmitriev on 2/21/18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"key", "data"})
public class Document {
  private String key;
  private String data;
  private String createdAt; // todo map as LocalDateTime
}
