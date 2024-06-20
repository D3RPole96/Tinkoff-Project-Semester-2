package edu.example.imagga.service;

import edu.example.imagga.client.ImaggaClient;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Implementation of second filter service.
 */
@Service
public class ImaggaFilterServiceImpl implements ImaggaFilterService {
  private final int topTagsCount;
  private final ImaggaClient imaggaClient;

  public ImaggaFilterServiceImpl(ImaggaClient imaggaClient,
                                 @Value("${imagga.top-tags}") String topTags) {
    this.imaggaClient = imaggaClient;
    topTagsCount = Integer.parseInt(topTags);
  }

  @Override
  public byte[] apply(byte[] imageBytes, String contentType) throws IOException {
    var imageUrl = imaggaClient.uploadImage(imageBytes);
    var topTags = imaggaClient.getTopTags(imageUrl);

    var byteArrayInputStream = new ByteArrayInputStream(imageBytes);
    var bufferedImage = ImageIO.read(byteArrayInputStream);

    var width = bufferedImage.getWidth();
    var height = bufferedImage.getHeight();
    var taggedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    var graphics = taggedImage.createGraphics();
    graphics.drawImage(bufferedImage, 0, 0, null);
    graphics.setFont(new Font("Calibri", Font.BOLD, 36));
    graphics.setColor(Color.CYAN);

    var period = 46;
    var positionY = period;

    graphics.drawString("Tags:", 10, positionY);
    positionY += period;
    for (var tag : topTags) {
      graphics.drawString(tag, 10, positionY);
      positionY += period;
    }
    graphics.dispose();

    var outputStream = new ByteArrayOutputStream();
    var contentTypeSplit = contentType.split("/");
    ImageIO.write(taggedImage, contentTypeSplit[contentTypeSplit.length - 1], outputStream);
    return outputStream.toByteArray();
  }
}
