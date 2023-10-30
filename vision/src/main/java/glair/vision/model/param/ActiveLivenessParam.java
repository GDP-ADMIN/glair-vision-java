package glair.vision.model.param;

import glair.vision.enums.GestureCode;
import glair.vision.util.Json;

import java.util.HashMap;

/**
 * A record representing parameters for Active Liveness detection.
 */
public class ActiveLivenessParam {
  private final String imagePath;
  private final GestureCode gestureCode;

  /**
   * Constructs an ActiveLivenessParam object with the specified image path and gesture code.
   *
   * @param imagePath    The file path of the image to be processed.
   * @param gestureCode  The gesture code associated with the image.
   */
  public ActiveLivenessParam(String imagePath, GestureCode gestureCode) {
    this.imagePath = imagePath;
    this.gestureCode = gestureCode;
  }

  /**
   * Gets the image path associated with the ActiveLivenessParam.
   *
   * @return The file path of the image.
   */
  public String getImagePath() {
    return imagePath;
  }

  /**
   * Gets the gesture code associated with the ActiveLivenessParam.
   *
   * @return The gesture code.
   */
  public GestureCode getGestureCode() {
    return gestureCode;
  }

  /**
   * Returns a JSON representation of the ActiveLivenessParam.
   *
   * @return A JSON string representing the image path and gesture code.
   */
  @Override
  public String toString() {
    HashMap<String, String> map = new HashMap<>();
    map.put("image", this.imagePath);
    map.put("gestureCode", this.gestureCode.label);

    return Json.toJsonString(map, 2);
  }
}
