package glair.vision;

import glair.vision.api.Config;
import glair.vision.api.FaceBio;
import glair.vision.api.Identity;
import glair.vision.api.Ocr;
import glair.vision.logger.Logger;
import glair.vision.logger.LoggerConfig;
import glair.vision.model.VisionSettings;

/**
 * The main entry point for interacting with the Vision SDK.
 * This class provides access to various vision-related functionalities such as OCR,
 * face recognition, and identity verification.
 */
public class Vision {
  /**
   * Represents the version of the software/library.
   */
  public static final String version = "0.0.1-beta.1";
  private static final Logger logger = Logger.getInstance();

  private final Config config;
  private final Ocr ocr;
  private final FaceBio faceBio;
  private final Identity identity;

  /**
   * Constructs a Vision instance with the given vision settings
   *
   * @param visionSettings The vision settings to configure the SDK.
   */
  public Vision(VisionSettings visionSettings) {
    this(visionSettings, new LoggerConfig());
  }

  /**
   * Constructs a Vision instance with the given vision settings and optional logger
   * configuration.
   *
   * @param visionSettings The vision settings to configure the SDK.
   * @param loggerConfig   (Optional) The logger configuration for customizing logging
   *                       behavior.
   */
  public Vision(VisionSettings visionSettings, LoggerConfig loggerConfig) {
    this.config = new Config(visionSettings);
    this.ocr = new Ocr(this.config);
    this.faceBio = new FaceBio(this.config);
    this.identity = new Identity(this.config);

    if (loggerConfig != null) {
      logger.setPattern(loggerConfig.getLogPattern());
      logger.setLogLevel(loggerConfig.getLogLevel());
    }
  }

  /**
   * Get the configuration settings for the Vision SDK.
   *
   * @return The configuration settings.
   */
  public Config config() {
    return config;
  }

  /**
   * Get access to the OCR functionality of the Vision SDK.
   *
   * @return An OCR instance.
   */
  public Ocr ocr() {
    return ocr;
  }

  /**
   * Get access to the FaceBio (Face Recognition) functionality of the Vision SDK.
   *
   * @return A FaceBio instance.
   */
  public FaceBio faceBio() {
    return faceBio;
  }

  /**
   * Get access to the Identity functionality of the Vision SDK.
   *
   * @return An Identity instance.
   */
  public Identity identity() {
    return identity;
  }

  /**
   * Print the current logger configuration to the standard output.
   */
  public void printLoggerConfig() {
    System.out.println("Logger Config " + logger);
  }
}
