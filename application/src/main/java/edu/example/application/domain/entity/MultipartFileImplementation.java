package edu.example.application.domain.entity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementation of MultipartFile interface.
 */
@Data
public class MultipartFileImplementation implements MultipartFile {
  private final byte[] bytes;
  private final String name;
  private final String contentType;

  /**
   * MultipartFileImplementation constructor.
   */
  public MultipartFileImplementation(byte[] imageBytes, String name, String contentType) {
    bytes = imageBytes;
    this.name = name;
    this.contentType = contentType;
  }

  @Override
  public String getOriginalFilename() {
    return null;
  }

  @Override
  public boolean isEmpty() {
    return bytes == null || bytes.length == 0;
  }

  @Override
  public long getSize() {
    return bytes.length;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return new ByteArrayInputStream(bytes);
  }

  @Override
  public void transferTo(File dest) throws IOException, IllegalStateException {
    new FileOutputStream(dest).write(bytes);
  }
}
