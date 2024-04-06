package edu.example.hw1.domain.service;

import edu.example.hw1.config.MinioProperties;
import edu.example.hw1.domain.entity.ImageEntity;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private final MinioClient client;
    private final MinioProperties properties;

    @Override
    public ImageEntity uploadImage(MultipartFile file) throws Exception {
        var fileId = UUID.randomUUID().toString();

        var inputStream = new ByteArrayInputStream(file.getBytes());
        client.putObject(
                PutObjectArgs
                        .builder()
                        .bucket(properties.getBucket())
                        .object(fileId)
                        .stream(inputStream, file.getSize(), properties.getImageSize())
                        .contentType(file.getContentType())
                        .build()
        );

        return new ImageEntity().setName(file.getOriginalFilename()).setSize(file.getSize()).setLink(fileId);
    }

    @Override
    public byte[] downloadImage(String link) throws Exception {
        return IOUtils.toByteArray(client.getObject(
                GetObjectArgs.builder()
                        .bucket(properties.getBucket())
                        .object(link)
                        .build()));
    }

    @Override
    public void deleteImage(String link) throws Exception {
        client.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(properties.getBucket())
                        .object(link)
                        .build());
    }
}
