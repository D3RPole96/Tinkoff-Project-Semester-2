package edu.example.hw1.domain.entity;

import edu.example.hw1.domain.utils.Filter;
import edu.example.hw1.domain.utils.FilterToImageId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Table(name = "filters_to_image",
    indexes = @Index(columnList = "request_id", name = "request_id_index"))
@IdClass(FilterToImageId.class)
@Accessors(chain = true)
public class FilterToImageEntity {
  @Id
  @Column(name = "request_id")
  private UUID requestId;

  @Id
  @Column(name = "request_id")
  @Enumerated(EnumType.STRING)
  private Filter filter;

  @ManyToOne
  @JoinColumn(name = "request_id", referencedColumnName = "request_id")
  private ImageFilterRequestEntity imageFilterRequest;
}
