package edu.example.application.api.mapper;

import edu.example.application.api.dto.GetImagesResponse;
import edu.example.application.api.dto.Image;
import edu.example.application.api.dto.UploadImageResponse;
import edu.example.application.domain.entity.ImageEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for image entities and data transfer objects.
 */
@Mapper(componentModel = "spring")
public interface ImageMapper {
  @Mapping(target = "imageId", source = "imageEntity.id")
  Image imageEntityToImage(ImageEntity imageEntity);

  @Mapping(target = "imageId", source = "imageEntity.id")
  UploadImageResponse imageEntityToUploadImageResponse(ImageEntity imageEntity);

  /**
   * Mapping method for lists of ImageEntities to GetImagesResponse.
   *
   * @param imageEntities List of ImageEntities
   * @return GetImagesResponse Data transfer object for getting user images
   */
  default GetImagesResponse imageEntitiesToGetImagesResponse(List<ImageEntity> imageEntities) {
    return new GetImagesResponse(imageEntities
        .stream()
        .map(this::imageEntityToImage)
        .toList()
        .toArray(new Image[0]));
  }
}
