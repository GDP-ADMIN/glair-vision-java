package glair.vision.model.param;

import glair.vision.util.Json;

import java.util.HashMap;

/**
 * A record representing parameters for Active Liveness detection.
 */
public record ActiveLivenessParam(String imagePath, String gestureCode) {
  /**
   * Returns a JSON representation of the ActiveLivenessParam.
   *
   * @return A JSON string representing the image path and gesture code.
   */
  @Override
  public String toString() {
    HashMap<String, String> map = new HashMap<>();
    map.put("image", this.imagePath);
    map.put("gestureCode", this.gestureCode);

    return Json.toJsonString(map, 2);
  }
}
