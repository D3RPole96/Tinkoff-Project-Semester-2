package edu.example.first.filter.service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
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
public class FirstFilterServiceImpl implements FirstFilterService {
  @Override
  public byte[] apply(byte[] imageBytes, String contentType) throws IOException {
    var byteArrayInputStream = new ByteArrayInputStream(imageBytes);
    var image = ImageIO.read(byteArrayInputStream);

    var grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
    var g = grayImage.createGraphics();
    g.drawImage(image, 0, 0, null);
    g.dispose();

    float[] contourKernel = {
        -1, -1, -1,
        -1,  8, -1,
        -1, -1, -1
    };
    var contourOp = new ConvolveOp(new Kernel(3, 3, contourKernel));
    var contourImage = contourOp.filter(grayImage, null);

    for (var y = 0; y < contourImage.getHeight(); y++) {
      for (var x = 0; x < contourImage.getWidth(); x++) {
        var rgb = contourImage.getRGB(x, y);
        var alpha = (rgb >> 24) & 0xFF;
        var red = (rgb >> 16) & 0xFF;
        var green = (rgb >> 8) & 0xFF;
        var blue = rgb & 0xFF;
        var luminance = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
        var newRgb = (alpha << 24) | (luminance << 16) | (luminance << 8) | luminance;
        contourImage.setRGB(x, y, newRgb);
      }
    }

    var outputStream = new ByteArrayOutputStream();
    var contentTypeSplit = contentType.split("/");
    ImageIO.write(contourImage, contentTypeSplit[contentTypeSplit.length - 1], outputStream);

    return outputStream.toByteArray();
  }
}
