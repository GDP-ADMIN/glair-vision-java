package glair.vision.model.param;

import glair.vision.util.Json;

import java.util.HashMap;

/**
 * The `KtpParam` class represents parameters for processing a KTP.
 */
public class KtpParam {
  private final String imagePath;
  private final Boolean qualitiesDetector;

  /**
   * Constructs a `KtpParam` instance with the specified image path and default
   * qualities detector setting.
   *
   * @param imagePath The path to the KTP image.
   */

  public KtpParam(String imagePath) {
    this(imagePath, false);
  }

  /**
   * Constructs a `KtpParam` instance with the specified image path and qualities
   * detector setting.
   *
   * @param imagePath         The path to the KTP image.
   * @param qualitiesDetector A boolean flag indicating whether qualities detection
   *                          should be enabled.
   */
  public KtpParam(String imagePath, Boolean qualitiesDetector) {
    this.imagePath = imagePath;
    this.qualitiesDetector = qualitiesDetector;
  }

  /**
   * Gets the qualities detector setting.
   *
   * @return The qualities detector setting, `true` if enabled, `false` otherwise.
   */
  public Boolean getQualitiesDetector() {
    return qualitiesDetector;
  }

  /**
   * Gets the path to the KTP image.
   *
   * @return The path to the KTP image.
   */
  public String getImagePath() {
    return imagePath;
  }

  /**
   * Returns a string representation of the `KtpParam` object.
   *
   * @return A string containing information about the image path and qualities
   * detector setting.
   */
  @Override
  public String toString() {
    HashMap<String, String> map = new HashMap<>();
    map.put("Image Path", this.imagePath);
    map.put("Qualities Detector", this.qualitiesDetector.toString());

    return Json.toJsonString(map, 2);
  }
}
