package edu.example.first.filter.service;

import java.awt.image.BufferedImage;
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
  public byte[] apply(byte[] imageBytes) throws IOException {
    BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

    float[] highPassKernel = {
        0, -1, 0,
        -1,  4, -1,
        0, -1, 0
    };

    var kernel = new Kernel(3, 3, highPassKernel);
    var op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

    var filteredImage = op.filter(image, null);

    var byteArrayOutputStream = new ByteArrayOutputStream();
    ImageIO.write(filteredImage, "png", byteArrayOutputStream);
    return byteArrayOutputStream.toByteArray();
  }
}
