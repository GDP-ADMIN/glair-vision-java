package glair.vision.model.param;

import glair.vision.util.Json;

import java.util.HashMap;

/**
 * A class representing parameters for Face Matching.
 */
public class FaceMatchParam {
  private final String capturedPath;
  private final String storedPath;

  /**
   * Constructs a FaceMatchParam object with the specified paths for the captured and stored images.
   *
   * @param capturedPath The file path of the captured image.
   * @param storedPath   The file path of the stored image for comparison.
   */
  public FaceMatchParam(String capturedPath, String storedPath) {
    this.capturedPath = capturedPath;
    this.storedPath = storedPath;
  }

  /**
   * Gets the file path of the captured image.
   *
   * @return The file path of the captured image.
   */
  public String getCapturedPath() {
    return capturedPath;
  }

  /**
   * Gets the file path of the stored image for comparison.
   *
   * @return The file path of the stored image.
   */
  public String getStoredPath() {
    return storedPath;
  }

  /**
   * Returns a JSON representation of the FaceMatchParam.
   *
   * @return A JSON string representing the captured and stored image paths.
   */
  @Override
  public String toString() {
    HashMap<String, String> map = new HashMap<>();
    map.put("Captured Image Path", this.capturedPath);
    map.put("Stored Image Path", this.storedPath);

    return Json.toJsonString(map, 2);
  }
}
