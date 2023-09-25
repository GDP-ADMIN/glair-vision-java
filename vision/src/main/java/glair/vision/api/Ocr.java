package glair.vision.api;

import glair.vision.api.sessions.NpwpSessions;
import glair.vision.model.Request;
import glair.vision.model.VisionSettings;
import glair.vision.model.param.BpkbParam;
import glair.vision.model.param.KtpParam;
import glair.vision.util.Json;
import glair.vision.util.Util;
import glair.vision.api.sessions.KtpSessions;
import glair.vision.logger.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

/**
 * The Ocr class provides methods for performing Optical Character Recognition (OCR)
 * operations.
 */
public class Ocr {
  private static final Logger logger = Logger.getInstance();
  private final Config config;
  private final KtpSessions ktpSessions;
  private final NpwpSessions npwpSessions;

  /**
   * Constructs an Ocr instance with the provided configuration.
   *
   * @param config The configuration settings to use for OCR operations.
   */
  public Ocr(Config config) {
    this.config = config;
    this.ktpSessions = new KtpSessions(config);
    this.npwpSessions = new NpwpSessions(config);
  }

  /**
   * Get access to KTP Sessions related operations.
   *
   * @return An instance of KtpSessions for KTP operations.
   */
  public KtpSessions ktpSessions() {
    return this.ktpSessions;
  }

  /**
   * Get access to NPWP Sessions related operations.
   *
   * @return An instance of NpwpSessions for NPWP operations.
   */
  public NpwpSessions npwpSessions() {
    return this.npwpSessions;
  }

  /**
   * Perform OCR on a KTP image using default configuration settings.
   *
   * @param param The OCR parameters, including the path to the KTP image file
   *              and an optional qualities detector setting.
   * @return The OCR result for the KTP image.
   * @throws Exception If an error occurs during the OCR process.
   */
  public String ktp(KtpParam param) throws Exception {
    return ktp(param, null);
  }

  /**
   * Perform OCR on a KTP image using custom configuration settings.
   *
   * @param param The OCR parameters, including the path to the KTP image file
   *              and an optional qualities detector setting.
   * @return The OCR result for the KTP image.
   * @throws Exception If an error occurs during the OCR process.
   */
  public String ktp(KtpParam param, VisionSettings newVisionSettings) throws Exception {
    log("KTP", param);
    Util.checkFileExist(param.getImagePath());

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    Util.addFileToFormData(bodyBuilder, "image", param.getImagePath());

    String endpoint = param.getQualitiesDetector() ? "ktp/qualities" : "ktp";

    return fetchOcr(bodyBuilder, newVisionSettings, endpoint);
  }

  /**
   * Perform OCR on an NPWP image using default configuration
   * settings.
   *
   * @param imagePath The path to the NPWP image file.
   * @return The OCR result for the NPWP image.
   * @throws Exception If an error occurs during the OCR process.
   */
  public String npwp(String imagePath) throws Exception {
    return npwp(imagePath, null);
  }

  /**
   * Perform OCR on an NPWP image using custom configuration
   * settings.
   *
   * @param imagePath         The path to the NPWP image file.
   * @param newVisionSettings The custom vision settings to use.
   * @return The OCR result for the NPWP image.
   * @throws Exception If an error occurs during the OCR process.
   */
  public String npwp(String imagePath, VisionSettings newVisionSettings) throws Exception {
    logSingle("NPWP", imagePath);
    return fetchOcrSingle(imagePath, newVisionSettings, "npwp");
  }

  /**
   * Perform OCR on an KK image using default configuration settings.
   *
   * @param imagePath The path to the KK image file.
   * @return The OCR result for the KK image.
   * @throws Exception If an error occurs during the OCR process.
   */
  public String kk(String imagePath) throws Exception {
    return kk(imagePath, null);
  }

  /**
   * Perform OCR on an KK image using custom configuration settings.
   *
   * @param imagePath         The path to the KK image file.
   * @param newVisionSettings The custom vision settings to use.
   * @return The OCR result for the KK image.
   * @throws Exception If an error occurs during the OCR process.
   */
  public String kk(String imagePath, VisionSettings newVisionSettings) throws Exception {
    logSingle("KK", imagePath);
    return fetchOcrSingle(imagePath, newVisionSettings, "kk");
  }

  /**
   * Perform OCR on an STNK image using default configuration settings.
   *
   * @param imagePath The path to the STNK image file.
   * @return The OCR result for the STNK image.
   * @throws Exception If an error occurs during the OCR process.
   */
  public String stnk(String imagePath) throws Exception {
    return stnk(imagePath, null);
  }

  /**
   * Perform OCR on an STNK image using custom configuration settings.
   *
   * @param imagePath         The path to the STNK image file.
   * @param newVisionSettings The custom vision settings to use.
   * @return The OCR result for the STNK image.
   * @throws Exception If an error occurs during the OCR process.
   */
  public String stnk(String imagePath, VisionSettings newVisionSettings) throws Exception {
    logSingle("STNK", imagePath);
    return fetchOcrSingle(imagePath, newVisionSettings, "stnk");
  }

  /**
   * Perform OCR on a BPKB image using default configuration settings.
   *
   * @param param The parameters for OCR, including the path to the BPKB
   *              image file and an optional page
   *              number (1-4).
   * @return The OCR result for the BPKB image.
   * @throws Exception If an error occurs during the OCR process, such as if the file
   *                   does not exist.
   */
  public String bpkb(BpkbParam param) throws Exception {
    return bpkb(param, null);
  }

  /**
   * Perform OCR on a BPKB image using custom configuration settings.
   *
   * @param param             The parameters for OCR, including the path to the BPKB
   *                          image file and an optional page
   *                          number (1-4).
   * @param newVisionSettings The custom vision settings to use.
   * @return The OCR result for the BPKB image.
   * @throws Exception If an error occurs during the OCR process, such as if the file
   *                   does not exist.
   */
  public String bpkb(BpkbParam param, VisionSettings newVisionSettings) throws Exception {
    log("BPKB", param);
    Util.checkFileExist(param.getImagePath());

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    Util.addFileToFormData(bodyBuilder, "image", param.getImagePath());
    if (param.getPage() >= 1 && param.getPage() <= 4) {
      bodyBuilder.addTextBody("page", Integer.toString(param.getPage()));
    }

    return fetchOcr(bodyBuilder, newVisionSettings, "bpkb");
  }

  /**
   * Perform OCR on a Passport image using default configuration settings.
   *
   * @param imagePath The path to the Passport image file.
   * @return The OCR result for the Passport image.
   * @throws Exception If an error occurs during the OCR process.
   */
  public String passport(String imagePath) throws Exception {
    return passport(imagePath, null);
  }

  /**
   * Perform OCR on a Passport image using custom configuration settings.
   *
   * @param imagePath         The path to the Passport image file.
   * @param newVisionSettings The custom vision settings to use.
   * @return The OCR result for the Passport image.
   * @throws Exception If an error occurs during the OCR process.
   */
  public String passport(String imagePath, VisionSettings newVisionSettings) throws Exception {
    logSingle("Passport", imagePath);
    return fetchOcrSingle(imagePath, newVisionSettings, "passport");
  }

  /**
   * Perform OCR on a License Plate image using default configuration settings.
   *
   * @param imagePath The path to the License Plate image file.
   * @return The OCR result for the License Plate image.
   * @throws Exception If an error occurs during the OCR process.
   */
  public String licensePlate(String imagePath) throws Exception {
    return licensePlate(imagePath, null);
  }

  /**
   * Perform OCR on a License Plate image using custom configuration settings.
   *
   * @param imagePath         The path to the License Plate image file.
   * @param newVisionSettings The custom vision settings to use.
   * @return The OCR result for the License Plate image.
   * @throws Exception If an error occurs during the OCR process.
   */
  public String licensePlate(String imagePath, VisionSettings newVisionSettings) throws Exception {
    logSingle("License Plate", imagePath);
    return fetchOcrSingle(imagePath, newVisionSettings, "plate");
  }

  /**
   * Perform OCR on a General Document image using default configuration settings.
   *
   * @param imagePath The path to the General Document image file.
   * @return The OCR result for the General Document image.
   * @throws Exception If an error occurs during the OCR process.
   */
  public String generalDocument(String imagePath) throws Exception {
    return generalDocument(imagePath, null);
  }

  /**
   * Perform OCR on a General Document image using custom configuration settings.
   *
   * @param imagePath         The path to the General Document image file.
   * @param newVisionSettings The custom vision settings to use.
   * @return The OCR result for the General Document image.
   * @throws Exception If an error occurs during the OCR process.
   */
  public String generalDocument(String imagePath, VisionSettings newVisionSettings) throws Exception {
    logSingle("General Document", imagePath);
    return fetchOcrSingle(imagePath, newVisionSettings, "general-document");
  }

  /**
   * Perform OCR on an Invoice image using default configuration settings.
   *
   * @param imagePath The path to the Invoice image file.
   * @return The OCR result for the Invoice image.
   * @throws Exception If an error occurs during the OCR process.
   */
  public String invoice(String imagePath) throws Exception {
    return invoice(imagePath, null);
  }

  /**
   * Perform OCR on an Invoice image using custom configuration settings.
   *
   * @param imagePath         The path to the Invoice image file.
   * @param newVisionSettings The custom vision settings to use.
   * @return The OCR result for the Invoice image.
   * @throws Exception If an error occurs during the OCR process.
   */
  public String invoice(String imagePath, VisionSettings newVisionSettings) throws Exception {
    logSingle("Invoice", imagePath);
    return fetchOcrSingle(imagePath, newVisionSettings, "invoice");
  }

  /**
   * Perform OCR on a Receipt image using default configuration settings.
   *
   * @param imagePath The path to the Receipt image file.
   * @return The OCR result for the Receipt image.
   * @throws Exception If an error occurs during the OCR process.
   */
  public String receipt(String imagePath) throws Exception {
    return receipt(imagePath, null);
  }

  /**
   * Perform OCR on a Receipt image using custom configuration settings.
   *
   * @param imagePath         The path to the Receipt image file.
   * @param newVisionSettings The custom vision settings to use.
   * @return The OCR result for the Receipt image.
   * @throws Exception If an error occurs during the OCR process.
   */
  public String receipt(String imagePath, VisionSettings newVisionSettings) throws Exception {
    logSingle("Receipt", imagePath);
    return fetchOcrSingle(imagePath, newVisionSettings, "receipt");
  }

  /**
   * Fetch OCR result for a single image using the provided settings.
   *
   * @param imagePath         The path to the image file.
   * @param newVisionSettings The custom vision settings to use.
   * @param endpoint          The OCR endpoint to use.
   * @return The OCR result for the image.
   * @throws Exception If an error occurs during the OCR process.
   */
  private String fetchOcrSingle(String imagePath, VisionSettings newVisionSettings,
                                String endpoint) throws Exception {
    Util.checkFileExist(imagePath);

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    Util.addFileToFormData(bodyBuilder, "image", imagePath);

    return fetchOcr(bodyBuilder, newVisionSettings, endpoint);
  }

  /**
   * Fetch OCR result using the provided HTTP request body and settings.
   *
   * @param bodyBuilder       The HTTP request body builder.
   * @param newVisionSettings The custom vision settings to use.
   * @param endpoint          The OCR endpoint to use.
   * @return The OCR result for the image.
   * @throws Exception If an error occurs during the OCR process.
   */
  private String fetchOcr(MultipartEntityBuilder bodyBuilder,
                          VisionSettings newVisionSettings, String endpoint) throws Exception {
    VisionSettings settingsToUse = (newVisionSettings == null) ?
        this.config.getSettings() : newVisionSettings;

    String url = "ocr/:version/" + endpoint;
    String method = "POST";

    HttpEntity body = bodyBuilder.build();

    Request request = new Request.RequestBuilder(url, method).body(body).build();

    return Util.visionFetch(this.config.getConfig(settingsToUse), request);
  }

  /**
   * Log information about a single OCR operation.
   *
   * @param logTitle  The title of the OCR operation.
   * @param imagePath The path to the image file.
   */
  private void logSingle(String logTitle, String imagePath) {
    log(logTitle, Json.toJsonString("image", imagePath));
  }

  /**
   * Log information for an OCR operation.
   *
   * @param logTitle The title of the OCR operation.
   * @param param    The parameter to log.
   */
  private void log(String logTitle, Object param) {
    logger.info("OCR -", logTitle);
    logger.debug("Param", param);
  }
}
