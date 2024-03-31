package edu.example.hw1.api.mapper;

import edu.example.hw1.api.dto.GetImagesResponse;
import edu.example.hw1.api.dto.Image;
import edu.example.hw1.api.dto.UploadImageResponse;
import edu.example.hw1.domain.entity.ImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    @Mapping(target = "imageId", source = "imageEntity.id")
    Image imageEntityToImage(ImageEntity imageEntity);

    @Mapping(target = "imageId", source = "imageEntity.id")
    UploadImageResponse imageEntityToUploadImageResponse(ImageEntity imageEntity);

    default GetImagesResponse imageEntitiesToGetImagesResponse(List<ImageEntity> imageEntities) {
        return new GetImagesResponse(imageEntities
                .stream()
                .map(this::imageEntityToImage)
                .toList()
                .toArray(new Image[0]));
    }

    default List<UploadImageResponse> imageEntitiesToUploadImageResponses(List<ImageEntity> imageEntities) {
        return imageEntities
                .stream()
                .map(this::imageEntityToUploadImageResponse)
                .toList();
    }
}
