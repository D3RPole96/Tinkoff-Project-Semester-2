package edu.example.imagga.client;

/**
 * Interface of Imagga client.
 */
public interface ImaggaClient {
  String uploadImage(byte[] image);

  String[] getTopTags(String uploadId);
}
