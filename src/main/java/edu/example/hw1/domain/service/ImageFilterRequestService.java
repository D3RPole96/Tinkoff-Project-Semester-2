package edu.example.hw1.domain.service;

import edu.example.hw1.domain.entity.ImageFilterRequestEntity;
import edu.example.hw1.domain.utils.Filter;
import java.util.List;
import java.util.UUID;

/**
 * Interface of image filter requests service.
 */
public interface ImageFilterRequestService {
  UUID applyImageFilters(UUID imageId, List<Filter> filters, String authorUsername);

  ImageFilterRequestEntity getModifiedImageByRequestId(UUID imageId,
                                                       UUID requestId,
                                                       String authorUsername);
}
