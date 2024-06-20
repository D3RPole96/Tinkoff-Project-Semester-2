package edu.example.imagga.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import edu.example.imagga.models.RequestEntity;

/**
 * Request repository.
 */
public interface RequestRepository extends JpaRepository<RequestEntity, UUID> {
}
