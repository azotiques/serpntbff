package org.azotiques.serpnt_bff.dtos;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductDto(
  String name,
  String description,
  String picture,
  BigDecimal price,
  Long brandId,
  Long categoryId
) {}
