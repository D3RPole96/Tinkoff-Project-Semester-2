package edu.example.hw1.repository;

import edu.example.hw1.domain.entity.FilterToImageEntity;
import edu.example.hw1.domain.utils.FilterToImageId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of FilterToImageEntities.
 */
public interface FilterToImageRepository
    extends JpaRepository<FilterToImageEntity, FilterToImageId> {

}
