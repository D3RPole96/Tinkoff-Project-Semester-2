package edu.example.third_filter.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of third filter service.
 */
@Service
@RequiredArgsConstructor
public class ThirdFilterServiceImpl implements ThirdFilterService {
  @Override
  public byte[] apply(byte[] imageBytes) throws IOException {
    var byteArrayInputStream = new ByteArrayInputStream(imageBytes);
    var coloredImage = ImageIO.read(byteArrayInputStream);

    if (coloredImage == null) {
      throw new IOException("Failed to read image from input byte array.");
    }

    var grayscaleImage = new BufferedImage(
        coloredImage.getWidth(),
        coloredImage.getHeight(),
        BufferedImage.TYPE_BYTE_GRAY
    );

    var g = grayscaleImage.createGraphics();
    g.drawImage(coloredImage, 0, 0, null);
    g.dispose();

    // Преобразование черно-белого изображения обратно в массив байтов
    var byteArrayOutputStream = new ByteArrayOutputStream();
    ImageIO.write(grayscaleImage, "png", byteArrayOutputStream);
    return byteArrayOutputStream.toByteArray();
  }
}
