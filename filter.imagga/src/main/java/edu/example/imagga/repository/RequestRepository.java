package edu.example.imagga.repository;

import edu.example.imagga.models.RequestEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Request repository.
 */
public interface RequestRepository extends JpaRepository<RequestEntity, UUID> {
}
