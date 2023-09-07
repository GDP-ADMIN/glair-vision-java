package glair.vision.logger;

import glair.vision.util.Json;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple logger class for logging messages with various log levels and a customizable log pattern.
 */
public class Logger {
  private static Logger instance;

  private final String[] logLevelMapping = {"debug", "info ", "warn ", "error"};

  private int logLevel;
  private String pattern;

  private Logger() {}

  /**
   * Get the singleton instance of the logger.
   *
   * @return The logger instance.
   */
  public static synchronized Logger getInstance() {
    if (instance == null) {
      instance = new Logger();
    }
    return instance;
  }

  /**
   * Get the current log level.
   *
   * @return The log level as a string.
   */
  public String getLogLevel() {
    return logLevelMapping[logLevel].toUpperCase();
  }

  /**
   * Set the log level.
   *
   * @param logLevel The log level to set.
   *                 Possible values:<br>
   *                 - {@link LoggerConfig#DEBUG} for debugging messages.<br>
   *                 - {@link LoggerConfig#INFO} for informational messages.<br>
   *                 - {@link LoggerConfig#WARN} for warning messages.<br>
   *                 - {@link LoggerConfig#ERROR} for error messages.
   */
  public void setLogLevel(int logLevel) {
    this.logLevel = logLevel;
  }

  /**
   * Get the log pattern.
   *
   * @return The log pattern.
   */
  public String getPattern() {
    return pattern;
  }

  /**
   * Set the log pattern.
   *
   * @param pattern The log pattern to set.
   */
  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  /**
   * Log a debug message.
   *
   * @param args The objects to log.
   */
  public void debug(Object... args) {
    log(LoggerConfig.DEBUG, args);
  }

  /**
   * Log an info message.
   *
   * @param args The objects to log.
   */
  public void info(Object... args) {
    log(LoggerConfig.INFO, args);
  }

  /**
   * Log a warning message.
   *
   * @param args The objects to log.
   */
  public void warn(Object... args) {
    log(LoggerConfig.WARN, args);
  }

  /**
   * Log an error message.
   *
   * @param args The objects to log.
   */
  public void error(Object... args) {
    log(LoggerConfig.ERROR, args);
  }

  private void log(int logLevel, Object... args) {
    if (logLevel < this.logLevel) {
      return;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS");
    String now = dateFormat.format(new Date());

    String level = logLevelMapping[logLevel];

    StringBuilder messageBuilder = new StringBuilder();
    for (Object arg : args) {
      messageBuilder
          .append(stringify(arg))
          .append(" ");
    }
    String message = messageBuilder
        .toString()
        .trim();

    String formattedLog = pattern
        .replace("{timestamp}", now)
        .replace("{level}", level.toUpperCase())
        .replace("{message}", message);

    System.out.println(formattedLog);
  }

  private String stringify(Object obj) {
    if (obj instanceof String) {
      return (String) obj;
    } else {
      try {
        return obj.toString();
      } catch (Exception e) {
        return "Failed to convert to JSON: " + obj;
      }
    }
  }

  /**
   * Get a string representation of the logger configuration.
   *
   * @return A JSON-formatted string representing the logger configuration.
   */
  @Override
  public String toString() {
    HashMap<String, String> map = new HashMap<>();
    map.put("Log Level", getLogLevel());
    map.put("Log Pattern", getPattern());

    return Json.toJsonString(map, 2);
  }

  /**
   * Main method for testing the logger.
   *
   * @param args Command-line arguments (not used).
   */
  public static void main(String[] args) {
    Logger logger = new Logger();
    logger.info("This is an info message.", 42, true);
    logger.debug("This is a debug message.", new int[]{1, 2, 3});
  }
}

