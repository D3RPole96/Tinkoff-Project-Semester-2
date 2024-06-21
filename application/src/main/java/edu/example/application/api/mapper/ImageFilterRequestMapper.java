package edu.example.application.api.mapper;

import edu.example.application.api.dto.GetModifiedImageByRequestIdResponse;
import edu.example.application.domain.entity.ImageFilterRequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for image filter request entity and data transfer objects.
 */
@Mapper(componentModel = "spring")
public interface ImageFilterRequestMapper {
  @Mapping(target = "imageId", source = "imageFilterRequestEntity",
      qualifiedByName = "GetModifiedImageByRequestIdResponse")
  GetModifiedImageByRequestIdResponse imageFilterRequestEntityToGetModifiedImageByRequestIdResponse(
      ImageFilterRequestEntity imageFilterRequestEntity);

  /**
   * Mapper for image filter request entity and data transfer objects.
   *
   * @param imageFilterRequestEntity Image Filter Request Entity
   * @return String Image id, modified or not
   */
  @Named("GetModifiedImageByRequestIdResponse")
  default String getModifiedImageByRequestIdResponse(
      ImageFilterRequestEntity imageFilterRequestEntity) {
    if (imageFilterRequestEntity.getEditedImageId() == null) {
      return imageFilterRequestEntity.getUneditedImageId().toString();
    }

    return imageFilterRequestEntity.getEditedImageId().toString();
  }

}
