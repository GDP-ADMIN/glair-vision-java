package glair.vision.model.param.sessions;

import glair.vision.util.Json;

import java.util.HashMap;

/**
 * Represents the parameters for a KTP (Kartu Tanda Penduduk) session.
 * This class extends the {@link BaseSessionsParam} class to include additional options.
 */
public class KtpSessionsParam extends BaseSessionsParam {
  private boolean qualitiesDetector = false;

  /**
   * Constructs a KtpSessionsParam instance with the specified success URL.
   *
   * @param successUrl The URL to redirect to upon successful session completion.
   */
  public KtpSessionsParam(String successUrl) {
    super(successUrl);
  }

  /**
   * Gets whether the image should be checked for quality during the KTP session.
   *
   * @return True if the image quality check is enabled, false otherwise.
   */
  public boolean getQualitiesDetector() {
    return qualitiesDetector;
  }

  /**
   * Sets whether the image should be checked for quality during the KTP session.
   *
   * @param qualitiesDetector If set to true, the image will be checked for quality.
   *                          The default value is false.
   */
  public void setQualitiesDetector(boolean qualitiesDetector) {
    this.qualitiesDetector = qualitiesDetector;
  }

  /**
   * Returns a JSON representation of the KTP session parameters.
   *
   * @return A JSON string representing the session parameters.
   */
  @Override
  public String toString() {
    HashMap<String, String> map = new HashMap<>();
    map.put("Success Url", super.getSuccessUrl());
    map.put("Cancel Url", super.getCancelUrl());
    map.put("Qualities Detector", String.valueOf(qualitiesDetector));

    return Json.toJsonString(map, 2);
  }
}
