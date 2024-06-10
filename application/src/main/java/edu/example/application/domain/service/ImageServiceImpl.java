package edu.example.application.domain.service;

import edu.example.application.api.exceptions.EntityNotFoundException;
import edu.example.application.api.exceptions.FileAccessException;
import edu.example.application.config.MinioProperties;
import edu.example.application.domain.entity.ImageEntity;
import edu.example.application.repository.ImageRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementation of images service.
 */
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
  private final ImageRepository imageRepository;
  private final UserService userService;
  private final MinioService minioService;

  @Override
  public byte[] downloadImage(UUID imageId, String authorUsername) throws Exception {
    var image = getImageMeta(imageId, authorUsername);

    return minioService.downloadImage(image.getLink());
  }

  @Override
  public byte[] downloadImageWithoutAuthentication(UUID imageId) throws Exception {
    var image = getImageMetaWithoutAuthentication(imageId);

    return minioService.downloadImage(image.getLink());
  }

  @Override
  public ImageEntity uploadImageToUser(MultipartFile file, String authorUsername) throws Exception {
    var user = userService.getUserByUsername(authorUsername);
    var imageDto = minioService.uploadImage(file);
    var image = new ImageEntity()
        .setName(imageDto.getName())
        .setSize(imageDto.getSize())
        .setLink(imageDto.getLink());
    image.setUser(user);

    imageRepository.save(image);

    return image;
  }

  @Override
  public void deleteImage(UUID imageId, String authorUsername) throws Exception {
    var image = getImageMeta(imageId, authorUsername);
    imageRepository.deleteById(imageId);

    minioService.deleteImage(image.getLink());
  }

  @Override
  public List<ImageEntity> getUserImages(String username) {
    var user = userService.getUserByUsername(username);

    return imageRepository.findAllByUserId(user.getId());
  }

  @Override
  public ImageEntity getImageMeta(UUID imageId, String authorUsername) {
    var image = imageRepository
        .findImageEntityById(imageId)
        .orElseThrow(() -> new EntityNotFoundException("Картинка с указанным ID не найдена"));

    var user = userService.getUserByUsername(authorUsername);

    if (!image.getUser().getId().equals(user.getId())) {
      throw new FileAccessException("Нет доступа к этому файлу");
    }

    return image;
  }

  @Override
  public ImageEntity getImageMetaWithoutAuthentication(UUID imageId) {
    return imageRepository
        .findImageEntityById(imageId)
        .orElseThrow(() -> new EntityNotFoundException("Картинка с указанным ID не найдена"));
  }
}
