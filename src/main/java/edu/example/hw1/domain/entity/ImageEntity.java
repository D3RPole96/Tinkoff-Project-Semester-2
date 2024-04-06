package edu.example.hw1.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

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
