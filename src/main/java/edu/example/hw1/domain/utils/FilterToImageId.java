package edu.example.hw1.domain.utils;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterToImageId implements Serializable {
  private UUID requestId;
  private Filter filter;
}
