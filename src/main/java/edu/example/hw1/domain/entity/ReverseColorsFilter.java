package edu.example.hw1.domain.entity;

public class ReverseColorsFilter implements Filter {
  @Override
  public byte[] apply(byte[] image) {
    var newImage = new byte[image.length];
    for (var i = 0; i < image.length; i++) {
      newImage[i] = (byte)(image[i] ^ 0xff);
    }

    return newImage;
  }
}
