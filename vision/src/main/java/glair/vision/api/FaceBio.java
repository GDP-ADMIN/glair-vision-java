package glair.vision.api;

import glair.vision.api.sessions.ActiveLivenessSessions;
import glair.vision.api.sessions.PassiveLivenessSessions;
import glair.vision.logger.Logger;
import glair.vision.model.Request;
import glair.vision.model.VisionSettings;
import glair.vision.model.param.ActiveLivenessParam;
import glair.vision.model.param.FaceMatchParam;
import glair.vision.util.Json;
import glair.vision.util.Util;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.util.HashMap;

/**
 * The FaceBio class provides methods for performing Face Biometric operations.
 */
public class FaceBio {
  private static final Logger logger = Logger.getInstance();

  private final Config config;
  private final ActiveLivenessSessions activeLivenessSessions;
  private final PassiveLivenessSessions passiveLivenessSessions;

  /**
   * Constructs an FaceBio instance with the provided configuration.
   *
   * @param config The configuration settings to use for Face Biometric operations.
   */
  public FaceBio(Config config) {
    this.config = config;
    this.activeLivenessSessions = new ActiveLivenessSessions(config);
    this.passiveLivenessSessions = new PassiveLivenessSessions(config);
  }

  /**
   * Get access to Active Liveness Sessions related operations.
   *
   * @return An instance of ActiveLivenessSessions for Active Liveness operations.
   */
  public ActiveLivenessSessions activeLivenessSessions() {
    return this.activeLivenessSessions;
  }

  /**
   * Get access to Passive Liveness Sessions related operations.
   *
   * @return An instance of PassiveLivenessSessions for Passive Liveness operations.
   */
  public PassiveLivenessSessions passiveLivenessSessions() {
    return this.passiveLivenessSessions;
  }

  /**
   * Performs Face Match using default configuration settings.
   *
   * @param param The FaceMatchParam object representing the candidate and stored faces.
   * @return The Face Match result.
   * @throws Exception If an error occurs during the detection process.
   */
  public String match(FaceMatchParam param) throws Exception {
    return match(param, null);
  }

  /**
   * Performs Face Match using custom configuration settings.
   *
   * @param param             The FaceMatchParam object representing the candidate and
   *                          stored faces.
   * @param newVisionSettings The custom vision settings to use.
   * @return The Face Match result.
   * @throws Exception If an error occurs during the detection process.
   */
  public String match(
      FaceMatchParam param, VisionSettings newVisionSettings
  ) throws Exception {
    log("Match", param);
    Util.checkFileExist(param.getCapturedPath());
    Util.checkFileExist(param.getStoredPath());

    HashMap<String, String> map = new HashMap<>();
    map.put("captured_image", Util.fileToBase64(param.getCapturedPath()));
    map.put("stored_image", Util.fileToBase64(param.getStoredPath()));

    RequestBody body = RequestBody.create(Json.toJsonString(map),
        MediaType.get("application/json; charset=utf-8")
    );

    return fetch(body, "match", newVisionSettings);
  }

  /**
   * Perform Passive Liveness detection using default configuration settings.
   *
   * @param imagePath The path to the image file.
   * @return The result of Passive Liveness detection.
   * @throws Exception If an error occurs during the detection process.
   */
  public String passiveLiveness(String imagePath) throws Exception {
    return passiveLiveness(imagePath, null);
  }

  /**
   * Perform Passive Liveness detection using custom configuration settings.
   *
   * @param imagePath         The path to the image file.
   * @param newVisionSettings The custom vision settings to use.
   * @return The result of Passive Liveness detection.
   * @throws Exception If an error occurs during the detection process.
   */
  public String passiveLiveness(
      String imagePath, VisionSettings newVisionSettings
  ) throws Exception {
    log("Passive Liveness", Json.toJsonString("image", imagePath));
    Util.checkFileExist(imagePath);

    MultipartBody.Builder builder = Util.createFormData();
    Util.addFileToFormData(builder, "image", imagePath);

    return fetch(builder.build(), "passive-liveness", newVisionSettings);
  }

  /**
   * Perform Active Liveness detection using default configuration settings.
   *
   * @param param The ActiveLivenessParam object representing the image and gesture code.
   * @return The result of Active Liveness detection.
   * @throws Exception If an error occurs during the detection process.
   */
  public String activeLiveness(ActiveLivenessParam param) throws Exception {
    return activeLiveness(param, null);
  }

  /**
   * Perform Active Liveness detection using custom configuration settings.
   *
   * @param param             The ActiveLivenessParam object representing the image and
   *                          gesture code.
   * @param newVisionSettings The custom vision settings to use.
   * @return The result of Active Liveness detection.
   * @throws Exception If an error occurs during the detection process.
   */
  public String activeLiveness(
      ActiveLivenessParam param, VisionSettings newVisionSettings
  ) throws Exception {
    log("Active Liveness", param);
    Util.checkFileExist(param.getImagePath());

    MultipartBody.Builder builder = Util.createFormData();
    Util.addFileToFormData(builder, "image", param.getImagePath());
    Util.addTextToFormData(builder, "gesture-code", param.getGestureCode().label);

    return fetch(builder.build(), "active-liveness", newVisionSettings);
  }

  /**
   * A private method to fetch Face Biometric data using an HTTP request.
   */
  private String fetch(
      RequestBody body, String url, VisionSettings newVisionSettings
  ) throws Exception {
    VisionSettings settingsToUse = (newVisionSettings == null) ?
        this.config.getSettings() : newVisionSettings;

    String method = "POST";
    String endpoint = "face/:version/" + url;

    Request request = new Request.RequestBuilder(endpoint, method).body(body).build();

    return Util.visionFetch(this.config.getConfig(settingsToUse), request);
  }

  /**
   * Log information for a Face Biometric operation.
   */
  private void log(String logTitle, Object param) {
    logger.info("Face Biometric -", logTitle);
    logger.debug("Param", param);
  }
}