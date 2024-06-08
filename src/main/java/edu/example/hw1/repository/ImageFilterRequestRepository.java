package edu.example.hw1.repository;

import edu.example.hw1.domain.entity.ImageFilterRequestEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of ImageFilterRequestEntities.
 */
public interface ImageFilterRequestRepository
    extends JpaRepository<ImageFilterRequestEntity, UUID> {

}
