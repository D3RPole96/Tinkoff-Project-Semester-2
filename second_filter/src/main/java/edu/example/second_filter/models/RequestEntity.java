package edu.example.second_filter.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Request entity. Primary key type: UUID.
 */
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "requests",
    uniqueConstraints = { @UniqueConstraint(columnNames = { "imageId", "requestId" }) })
@Accessors(chain = true)
public class RequestEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "image_id")
  private UUID imageId;

  @Column(name = "request_id")
  private UUID requestId;
}
