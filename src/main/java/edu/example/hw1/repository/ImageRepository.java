package edu.example.hw1.repository;

import edu.example.hw1.domain.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, UUID> {
    boolean existsImagesByIdIn(List<UUID> ids);
    boolean existsImagesByLink(String link);
    Optional<ImageEntity> findImageById(UUID id);
    List<ImageEntity> findAllByIdIn(List<UUID> ids);
    List<ImageEntity> findAllByUserId(UUID userId);
}
