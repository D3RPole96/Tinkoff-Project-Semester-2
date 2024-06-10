package edu.example.first.filter.repository;

import edu.example.first.filter.models.RequestEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Request repository.
 */
public interface RequestRepository extends JpaRepository<RequestEntity, UUID> {
}
