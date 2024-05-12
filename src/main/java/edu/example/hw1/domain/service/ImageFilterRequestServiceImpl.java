package edu.example.hw1.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.example.hw1.api.exceptions.EntityNotFoundException;
import edu.example.hw1.domain.entity.FilterToImageEntity;
import edu.example.hw1.domain.entity.ImageFilterRequestEntity;
import edu.example.hw1.domain.utils.Filter;
import edu.example.hw1.domain.utils.Status;
import edu.example.hw1.kafka.KafkaProducer;
import edu.example.hw1.kafka.models.KafkaWipMessage;
import edu.example.hw1.repository.ImageFilterRequestRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of image filter requests service.
 */
@Service
@RequiredArgsConstructor
public class ImageFilterRequestServiceImpl implements ImageFilterRequestService {
  private final ImageService imageService;
  private final ImageFilterRequestRepository imageFilterRequestRepository;
  private final KafkaProducer kafkaProducer;

  @Override
  public UUID applyImageFilters(UUID imageId, List<Filter> filters, String authorUsername)
      throws JsonProcessingException {
    imageService.getImageMeta(imageId, authorUsername);

    var requestId = UUID.randomUUID();
    var filtersToImage = new ArrayList<FilterToImageEntity>();
    for (var filter : filters) {
      var filterToImage = new FilterToImageEntity().setRequestId(requestId).setFilter(filter);
      filtersToImage.add(filterToImage);
    }

    var imageFilterRequest = new ImageFilterRequestEntity()
        .setRequestId(requestId)
        .setUneditedImageId(imageId)
        .setStatus(Status.WIP)
        .setFiltersToImage(filtersToImage);

    var kafkaWipMessage = new KafkaWipMessage(imageId, requestId, filters);

    kafkaProducer.write(kafkaWipMessage);
    imageFilterRequestRepository.save(imageFilterRequest);

    return requestId;
  }

  @Override
  public ImageFilterRequestEntity getModifiedImageByRequestId(UUID imageId,
                                                              UUID requestId,
                                                              String authorUsername) {
    imageService.getImageMeta(imageId, authorUsername);

    var imageFilterRequest = imageFilterRequestRepository.findById(requestId).orElseThrow((()
        -> new EntityNotFoundException("Запрос на применение фильтров с указанным ID не найден"))
    );
    if (!imageFilterRequest.getUneditedImageId().equals(imageId)) {
      throw new EntityNotFoundException("К данному запросу не относится эта картинка");
    }

    return imageFilterRequest;
  }
}
