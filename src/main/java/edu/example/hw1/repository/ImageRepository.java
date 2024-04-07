package edu.example.hw1.repository;

import edu.example.hw1.domain.entity.ImageEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository of ImageEntities.
 */
@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, UUID> {
  Optional<ImageEntity> findImageEntityById(UUID id);

  List<ImageEntity> findAllByUserId(UUID userId);

  Optional<ImageEntity> findImageEntityByLink(String link);
}
