package edu.example.third_filter.repository;

import edu.example.third_filter.models.RequestEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Request repository.
 */
public interface RequestRepository extends JpaRepository<RequestEntity, UUID> {
}
