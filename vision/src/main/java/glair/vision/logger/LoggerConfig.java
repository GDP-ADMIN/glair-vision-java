package glair.vision.logger;

/**
 * Configuration for the logger.
 */
public class LoggerConfig {
  /**
   * Represents the DEBUG log level. This level is used for fine-grained debugging
   * information.
   */
  public static final int DEBUG = 0;
  /**
   * Represents the INFO log level. This level is used for general informational messages.
   */
  public static final int INFO = 1;
  /**
   * Represents the WARN log level. This level is used for warning messages, indicating
   * potential issues.
   */
  public static final int WARN = 2;
  /**
   * Represents the ERROR log level. This level is used for error messages, indicating
   * significant problems.
   */
  public static final int ERROR = 3;

  private static final int DEFAULT_LOG_LEVEL = INFO;
  private static final String DEFAULT_LOG_PATTERN = "[{timestamp}] [{level}] GLAIR" +
      " Vision SDK: {message}";

  private final int logLevel;
  private final String logPattern;

  /**
   * Constructs a new logger configuration with the default log level and pattern.
   */
  public LoggerConfig() {
    this(DEFAULT_LOG_LEVEL, DEFAULT_LOG_PATTERN);
  }

  /**
   * Constructs a new logger configuration with a custom log level and the default log
   * pattern.
   *
   * @param logLevel The custom log level.
   */
  public LoggerConfig(int logLevel) {
    this(logLevel, DEFAULT_LOG_PATTERN);
  }

  /**
   * Constructs a new logger configuration with a custom log pattern and the default
   * log level.
   *
   * @param logPattern The custom log pattern.
   */
  public LoggerConfig(String logPattern) {
    this(DEFAULT_LOG_LEVEL, logPattern);
  }

  /**
   * Constructs a new logger configuration with custom log level and log pattern.
   *
   * @param logPattern The custom log pattern.
   * @param logLevel   The custom log level.
   */
  public LoggerConfig(String logPattern, int logLevel) {
    this(logLevel, logPattern);
  }

  /**
   * Constructs a new logger configuration with custom log level and log pattern.
   *
   * @param logLevel   The custom log level.
   * @param logPattern The custom log pattern.
   */
  public LoggerConfig(int logLevel, String logPattern) {
    this.logLevel = logLevel;
    this.logPattern = logPattern;
  }

  /**
   * Get the log level.
   *
   * @return The log level.
   */
  public int getLogLevel() {
    return logLevel;
  }

  /**
   * Get the log pattern.
   *
   * @return The log pattern.
   */
  public String getLogPattern() {
    return logPattern;
  }
}
