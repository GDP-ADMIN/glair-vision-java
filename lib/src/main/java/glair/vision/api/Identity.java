package glair.vision.api;

import glair.vision.logger.Logger;
import glair.vision.model.Request;
import glair.vision.model.VisionSettings;
import glair.vision.model.param.IdentityFaceVerificationParam;
import glair.vision.model.param.IdentityVerificationParam;
import glair.vision.util.Util;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

/**
 * The Identity class provides methods for performing Identity Verification operations.
 */
public class Identity {
  private static final Logger logger = Logger.getInstance();
  private final Config config;

  /**
   * Constructs an Identity instance with the provided configuration.
   *
   * @param config The configuration settings to use for Identity operations.
   */
  public Identity(Config config) {
    this.config = config;
  }

  /**
   * Performs basic identity verification using default configuration settings.
   *
   * @param param The identity verification parameters.
   * @return The verification result.
   * @throws Exception If an error occurs during the verification process.
   */
  public String verification(IdentityVerificationParam param) throws Exception {
    return verification(param, null);
  }

  /**
   * Performs basic identity verification using custom configuration settings.
   *
   * @param param             The identity verification parameters.
   * @param newVisionSettings The custom vision settings to use.
   * @return The verification result.
   * @throws Exception If an error occurs during the verification process.
   */
  public String verification(IdentityVerificationParam param,
                             VisionSettings newVisionSettings) throws Exception {
    log("Basic Verification", param);

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    bodyBuilder.addTextBody("nik", param.getNik());
    bodyBuilder.addTextBody("name", param.getName());
    bodyBuilder.addTextBody("date_of_birth", param.getDateOfBirth());

    return performVerification(bodyBuilder, "verification", newVisionSettings);
  }

  /**
   * Performs face verification using default configuration settings.
   *
   * @param param The face verification parameters.
   * @return The verification result.
   * @throws Exception If an error occurs during the verification process.
   */
  public String faceVerification(IdentityFaceVerificationParam param) throws Exception {
    return faceVerification(param, null);
  }

  /**
   * Performs face verification using custom configuration settings.
   *
   * @param param             The face verification parameters.
   * @param newVisionSettings The custom vision settings to use.
   * @return The verification result.
   * @throws Exception If an error occurs during the verification process.
   */
  public String faceVerification(IdentityFaceVerificationParam param,
                                 VisionSettings newVisionSettings) throws Exception {
    log("Face Verification", param);
    Util.checkFileExist(param.getFaceImagePath());

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    bodyBuilder.addTextBody("nik", param.getNik());
    bodyBuilder.addTextBody("name", param.getName());
    bodyBuilder.addTextBody("date_of_birth", param.getDateOfBirth());
    Util.addFileToFormData(bodyBuilder, "face_image", param.getFaceImagePath());

    return performVerification(bodyBuilder, "face-verification", newVisionSettings);
  }

  /**
   * Performs identity verification with the specified parameters.
   *
   * @param bodyBuilder       The builder for constructing the HTTP request body.
   * @param url               The URL endpoint for the verification.
   * @param newVisionSettings The custom vision settings to use (can be null for
   *                          default settings).
   * @return The verification result as a string.
   * @throws Exception If an error occurs during the verification process.
   */
  private String performVerification(MultipartEntityBuilder bodyBuilder, String url,
                                     VisionSettings newVisionSettings) throws Exception {
    VisionSettings settingsToUse = (newVisionSettings == null) ?
        this.config.getSettings() : newVisionSettings;

    String method = "POST";
    String endpoint = "identity/:version/" + url;

    HttpEntity body = bodyBuilder.build();
    Request request = new Request.RequestBuilder(endpoint, method).body(body).build();

    return Util.visionFetch(this.config.getConfig(settingsToUse), request);
  }

  /**
   * Log information for an Identity operation.
   *
   * @param logTitle The title of the Identity operation.
   * @param param    The parameter to log.
   */
  private void log(String logTitle, Object param) {
    logger.info("Identity -", logTitle);
    logger.debug("Param", param);
  }
}
