package edu.example.hw1.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.example.hw1.domain.entity.ImageFilterRequestEntity;
import edu.example.hw1.domain.utils.Filter;
import java.util.List;
import java.util.UUID;

/**
 * Interface of image filter requests service.
 */
public interface ImageFilterRequestService {
  UUID applyImageFilters(UUID imageId, List<Filter> filters, String authorUsername)
      throws JsonProcessingException;

  ImageFilterRequestEntity getModifiedImageByRequestId(UUID imageId,
                                                       UUID requestId,
                                                       String authorUsername);
}
