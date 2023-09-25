package glair.vision.model.param;

import glair.vision.util.Json;

import java.util.HashMap;

public class KtpParam {
  private final String imagePath;
  private final Boolean qualitiesDetector;

  public KtpParam(String imagePath) {
    this(imagePath, false);
  }

  public KtpParam(String imagePath, Boolean qualitiesDetector) {
    this.imagePath = imagePath;
    this.qualitiesDetector = qualitiesDetector;
  }

  public Boolean getQualitiesDetector() {
    return qualitiesDetector;
  }

  public String getImagePath() {
    return imagePath;
  }

  @Override
  public String toString() {
    HashMap<String, String> map = new HashMap<>();
    map.put("Image Path", this.imagePath);
    map.put("Qualities Detector", this.qualitiesDetector.toString());

    return Json.toJsonString(map, 2);
  }
}
