package edu.example.hw1.repository;

import edu.example.hw1.domain.entity.FilterToImageEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of FilterToImageEntities.
 */
public interface FilterToImageRepository extends JpaRepository<FilterToImageEntity, UUID> {
}
