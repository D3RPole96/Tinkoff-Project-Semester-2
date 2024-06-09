package edu.example.application.repository;

import edu.example.application.domain.entity.ImageFilterRequestEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of ImageFilterRequestEntities.
 */
public interface ImageFilterRequestRepository
    extends JpaRepository<ImageFilterRequestEntity, UUID> {

}
