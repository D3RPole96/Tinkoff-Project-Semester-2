package edu.example.hw1.domain.entity;

import edu.example.hw1.domain.utils.Filter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Filter to image entity. Primary key type: UUID, varchar.
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "filters_to_image")
@Accessors(chain = true)
public class FilterToImageEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "filter_to_image_id")
  private UUID id;

  @Column(name = "filter")
  @Enumerated(EnumType.STRING)
  private Filter filter;

  @ManyToOne
  @JoinColumn(name = "request_id", referencedColumnName = "request_id")
  private ImageFilterRequestEntity imageFilterRequest;
}
