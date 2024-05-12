package edu.example.hw1.domain.entity;

import edu.example.hw1.domain.utils.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Table(name = "image_filter_requests")
@Accessors(chain = true)
public class ImageFilterRequestEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "request_id")
  private UUID requestId;

  @Column(name = "unedited_image_id")
  private UUID uneditedImageId;

  @Column(name = "edited_image_id")
  private UUID editedImageId;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  @OneToMany(mappedBy = "imageFilterRequest", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<FilterToImageEntity> filtersToImage = new ArrayList<>();
}
