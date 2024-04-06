package edu.example.hw1.domain.service;

import edu.example.hw1.api.exceptions.EntityNotFoundException;
import edu.example.hw1.api.exceptions.FileAccessException;
import edu.example.hw1.domain.entity.ImageEntity;
import edu.example.hw1.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final MinioService minioService;
    private final UserService userService;

    @Override
    public byte[] downloadImage(UUID imageId, String authorUsername) throws Exception {
        var image = getImageMeta(imageId);
        var user = userService.getUserByUsername(authorUsername);

        var a = image.getUser().getId();
        var b = user.getId();

        if (!image.getUser().getId().equals(user.getId())) {
            throw new FileAccessException("Нет доступа к этому файлу");
        }

        return minioService.downloadImage(image.getLink());
    }

    @Override
    public ImageEntity uploadImageToUser(MultipartFile file, String authorUsername) throws Exception {
        var user = userService.getUserByUsername(authorUsername);
        var image = minioService.uploadImage(file);
        image.setUser(user);

        imageRepository.save(image);

        return image;
    }

    @Override
    public void deleteImage(UUID imageId, String authorUsername) throws Exception {
        var image = getImageMeta(imageId);
        imageRepository.deleteById(imageId);

        var user = userService.getUserByUsername(authorUsername);
        if (!image.getUser().getId().equals(user.getId())) {
            throw new FileAccessException("Нет доступа к этому файлу");
        }

        minioService.deleteImage(image.getLink());
    }

    @Override
    public List<ImageEntity> getUserImages(String username) {
        var user = userService.getUserByUsername(username);

        return imageRepository.findAllByUserId(user.getId());
    }

    private ImageEntity getImageMeta(UUID imageId) {
        return imageRepository
                .findImageEntityById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Картинка с указанным ID не найдена"));
    }
}
