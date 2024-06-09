package edu.example.second_filter.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of second filter service.
 */
@Service
@RequiredArgsConstructor
public class SecondFilterServiceImpl implements SecondFilterService {
  @Override
  public byte[] apply(byte[] imageBytes) throws IOException {
    var byteArrayInputStream = new ByteArrayInputStream(imageBytes);
    var originalImage = ImageIO.read(byteArrayInputStream);

    if (originalImage == null) {
      throw new IOException("Не удалось прочитать изображение из массива байтов.");
    }

    var invertedImage = new BufferedImage(
        originalImage.getWidth(),
        originalImage.getHeight(),
        originalImage.getType()
    );

    var g = invertedImage.createGraphics();
    g.drawImage(originalImage, 0, 0, null);
    g.dispose();

    for (int y = 0; y < invertedImage.getHeight(); y++) {
      for (int x = 0; x < invertedImage.getWidth(); x++) {
        int rgba = invertedImage.getRGB(x, y);
        int alpha = (rgba >> 24) & 0xff;
        int red = (rgba >> 16) & 0xff;
        int green = (rgba >> 8) & 0xff;
        int blue = rgba & 0xff;

        red = 255 - red;
        green = 255 - green;
        blue = 255 - blue;

        int invertedRGBA = (alpha << 24) | (red << 16) | (green << 8) | blue;
        invertedImage.setRGB(x, y, invertedRGBA);
      }
    }

    var byteArrayOutputStream = new ByteArrayOutputStream();
    ImageIO.write(invertedImage, "png", byteArrayOutputStream);
    return byteArrayOutputStream.toByteArray();
  }
}
