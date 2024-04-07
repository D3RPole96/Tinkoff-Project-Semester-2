package edu.example.hw1.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Image entity. Primary key type: UUID.
 */
@Data
@Entity
@Table(name = "images")
@Accessors(chain = true)
public class ImageEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "name", length = 100)
  private String name;

  @Column(name = "size")
  private Long size;

  @Column(name = "link", length = 300)
  private String link;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity user;
}
