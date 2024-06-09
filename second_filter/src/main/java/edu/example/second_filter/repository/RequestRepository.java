package edu.example.second_filter.repository;

import edu.example.second_filter.models.RequestEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Request repository.
 */
public interface RequestRepository extends JpaRepository<RequestEntity, UUID> {
}
