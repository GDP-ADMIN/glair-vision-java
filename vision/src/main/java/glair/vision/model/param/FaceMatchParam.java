package glair.vision.model.param;

import glair.vision.util.Json;

import java.util.HashMap;

/**
 * A record representing parameters for Face Matching.
 */
public record FaceMatchParam(String capturedPath, String storedPath) {
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
