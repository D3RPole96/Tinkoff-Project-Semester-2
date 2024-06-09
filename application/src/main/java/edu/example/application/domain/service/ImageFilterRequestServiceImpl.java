package edu.example.application.domain.service;

import edu.example.application.api.exceptions.EntityNotFoundException;
import edu.example.application.domain.entity.ImageFilterRequestEntity;
import edu.example.application.domain.utils.Status;
import edu.example.application.kafka.KafkaProducer;
import edu.example.application.repository.ImageFilterRequestRepository;
import edu.example.common.components.kafka.models.Filter;
import edu.example.common.components.kafka.models.KafkaWipMessage;
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
  public UUID applyImageFilters(UUID imageId, List<Filter> filters, String authorUsername) {
    var imageMeta = imageService.getImageMeta(imageId, authorUsername);

    var imageFilterRequest = new ImageFilterRequestEntity()
        .setUneditedImageId(imageId)
        .setStatus(Status.WIP);

    var savedRequest = imageFilterRequestRepository.save(imageFilterRequest);

    var kafkaWipMessage = new KafkaWipMessage(imageId,
        savedRequest.getRequestId(),
        authorUsername,
        imageMeta.getLink(),
        imageMeta.getName(),
        filters);

    kafkaProducer.write(kafkaWipMessage);

    return savedRequest.getRequestId();
  }

  @Override
  public ImageFilterRequestEntity getModifiedImageByRequestId(UUID imageId,
                                                              UUID requestId,
                                                              String authorUsername) {
    imageService.getImageMeta(imageId, authorUsername);

    var a = imageFilterRequestRepository.findAll();

    var imageFilterRequest = imageFilterRequestRepository.findById(requestId).orElseThrow((()
        -> new EntityNotFoundException("Запрос на применение фильтров с указанным ID не найден"))
    );
    if (!imageFilterRequest.getUneditedImageId().equals(imageId)) {
      throw new EntityNotFoundException("К данному запросу не относится эта картинка");
    }

    return imageFilterRequest;
  }
}
