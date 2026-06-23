package glair.vision;

import glair.vision.logger.Logger;
import glair.vision.logger.LoggerConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Logger singleton. Each test saves and restores logger
 * state so other test suites are not affected by level changes.
 */
public class LoggerUnitTests {

  private String originalPattern;

  @BeforeEach
  void saveLoggerState() {
    Logger logger = Logger.getInstance();
    originalPattern = logger.getPattern();
  }

  @AfterEach
  void restoreLoggerState() {
    Logger logger = Logger.getInstance();
    // Restore to ERROR so other tests remain unaffected by noisy output
    logger.setLogLevel(LoggerConfig.ERROR);
    logger.setPattern(originalPattern);
  }

  // -----------------------------------------------------------------------
  // Singleton
  // -----------------------------------------------------------------------

  @Test
  public void getInstance_returnsSameInstance() {
    assertSame(Logger.getInstance(), Logger.getInstance());
  }

  // -----------------------------------------------------------------------
  // Log level get/set
  // -----------------------------------------------------------------------

  @Test
  public void setAndGetLogLevel() {
    Logger logger = Logger.getInstance();

    logger.setLogLevel(LoggerConfig.DEBUG);
    assertTrue(logger.getLogLevel().trim().equalsIgnoreCase("DEBUG"),
        "Expected DEBUG level");

    logger.setLogLevel(LoggerConfig.INFO);
    assertTrue(logger.getLogLevel().trim().equalsIgnoreCase("INFO"),
        "Expected INFO level");

    logger.setLogLevel(LoggerConfig.WARN);
    assertTrue(logger.getLogLevel().trim().equalsIgnoreCase("WARN"),
        "Expected WARN level");

    logger.setLogLevel(LoggerConfig.ERROR);
    assertTrue(logger.getLogLevel().trim().equalsIgnoreCase("ERROR"),
        "Expected ERROR level");
  }

  // -----------------------------------------------------------------------
  // Pattern get/set
  // -----------------------------------------------------------------------

  @Test
  public void setAndGetPattern() {
    Logger logger = Logger.getInstance();
    logger.setPattern("test-pattern");
    assertEquals("test-pattern", logger.getPattern());
  }

  // -----------------------------------------------------------------------
  // Logging methods — all should not throw even when output goes to stdout
  // -----------------------------------------------------------------------

  @Test
  public void debug_atDebugLevel_doesNotThrow() {
    Logger logger = Logger.getInstance();
    logger.setLogLevel(LoggerConfig.DEBUG);
    logger.setPattern("[{level}] {message}");
    assertDoesNotThrow(() -> logger.debug("debug message"));
  }

  @Test
  public void info_atDebugLevel_doesNotThrow() {
    Logger logger = Logger.getInstance();
    logger.setLogLevel(LoggerConfig.DEBUG);
    logger.setPattern("[{level}] {message}");
    assertDoesNotThrow(() -> logger.info("info", "message"));
  }

  @Test
  public void warn_atDebugLevel_doesNotThrow() {
    assertDoesNotThrow(() -> {
      Logger.getInstance().setLogLevel(LoggerConfig.DEBUG);
      Logger.getInstance().setPattern("[{level}] {message}");
      Logger.getInstance().warn("warn message");
    });
  }

  @Test
  public void error_atDebugLevel_doesNotThrow() {
    assertDoesNotThrow(() -> {
      Logger.getInstance().setLogLevel(LoggerConfig.DEBUG);
      Logger.getInstance().setPattern("[{level}] {message}");
      Logger.getInstance().error("error message");
    });
  }

  // -----------------------------------------------------------------------
  // Suppressed output (logLevel < threshold)
  // -----------------------------------------------------------------------

  @Test
  public void debug_belowLogLevel_isSkipped_doesNotThrow() {
    // Log level = ERROR; calling debug should skip silently
    Logger logger = Logger.getInstance();
    logger.setLogLevel(LoggerConfig.ERROR);
    logger.setPattern("[{level}] {message}");
    assertDoesNotThrow(() -> logger.debug("should be skipped"));
  }

  // -----------------------------------------------------------------------
  // Non-string arguments (covers stringify(obj) branch)
  // -----------------------------------------------------------------------

  @Test
  public void log_withNonStringArg_callsToString() {
    Logger logger = Logger.getInstance();
    logger.setLogLevel(LoggerConfig.DEBUG);
    logger.setPattern("[{level}] {message}");
    // Pass an Integer, Boolean, and plain Object to exercise the non-String branch
    assertDoesNotThrow(() -> logger.info(42, true, new Object()));
  }

  // -----------------------------------------------------------------------
  // toString()
  // -----------------------------------------------------------------------

  @Test
  public void toString_returnsNonNullString() {
    Logger logger = Logger.getInstance();
    logger.setLogLevel(LoggerConfig.INFO);
    logger.setPattern("test");
    String str = logger.toString();
    assertNotNull(str);
    // getLogLevel() for INFO returns "INFO " (with trailing space from mapping),
    // toUpperCase preserves the trailing space — check with trim
    assertTrue(str.contains("INFO"), "toString should contain the log level");
  }

  // -----------------------------------------------------------------------
  // Suppression at INFO when level is WARN
  // -----------------------------------------------------------------------

  @Test
  public void info_belowLogLevel_isSkipped() {
    Logger logger = Logger.getInstance();
    logger.setLogLevel(LoggerConfig.WARN);
    logger.setPattern("[{level}] {message}");
    // logLevel=WARN(2), calling info(level=1) → 1 < 2 → returns early, no output
    assertDoesNotThrow(() -> logger.info("should be skipped"));
  }

  // -----------------------------------------------------------------------
  // Multi-arg debug logging
  // -----------------------------------------------------------------------

  @Test
  public void debug_withMultipleArgs_doesNotThrow() {
    Logger logger = Logger.getInstance();
    logger.setLogLevel(LoggerConfig.DEBUG);
    logger.setPattern("[{level}] {message}");
    assertDoesNotThrow(() -> logger.debug("arg1", "arg2", "arg3"));
  }

  // -----------------------------------------------------------------------
  // WARN and ERROR suppressed by higher level
  // -----------------------------------------------------------------------

  @Test
  public void warn_suppressedWhenLevelIsError_doesNotThrow() {
    Logger logger = Logger.getInstance();
    logger.setLogLevel(LoggerConfig.ERROR);
    logger.setPattern("[{level}] {message}");
    // warn level=2 < ERROR level=3 → skipped silently
    assertDoesNotThrow(() -> logger.warn("suppressed warn"));
  }

  // -----------------------------------------------------------------------
  // Pattern with timestamp placeholder
  // -----------------------------------------------------------------------

  @Test
  public void log_patternWithTimestamp_doesNotThrow() {
    Logger logger = Logger.getInstance();
    logger.setLogLevel(LoggerConfig.DEBUG);
    logger.setPattern("[{timestamp}] [{level}] {message}");
    assertDoesNotThrow(() -> logger.debug("timestamp test"));
  }

  // -----------------------------------------------------------------------
  // LoggerConfig constructors
  // -----------------------------------------------------------------------

  @Test
  public void loggerConfig_defaultConstructor_hasDefaults() {
    LoggerConfig config = new LoggerConfig();
    assertEquals(LoggerConfig.INFO, config.getLogLevel());
    assertNotNull(config.getLogPattern());
  }

  @Test
  public void loggerConfig_intConstructor_setsLogLevel() {
    LoggerConfig config = new LoggerConfig(LoggerConfig.DEBUG);
    assertEquals(LoggerConfig.DEBUG, config.getLogLevel());
    assertNotNull(config.getLogPattern());
  }

  @Test
  public void loggerConfig_stringConstructor_setsPattern() {
    LoggerConfig config = new LoggerConfig("custom-pattern");
    assertEquals("custom-pattern", config.getLogPattern());
    assertEquals(LoggerConfig.INFO, config.getLogLevel());
  }

  @Test
  public void loggerConfig_stringIntConstructor_setBoth() {
    LoggerConfig config = new LoggerConfig("my-pattern", LoggerConfig.WARN);
    assertEquals("my-pattern", config.getLogPattern());
    assertEquals(LoggerConfig.WARN, config.getLogLevel());
  }

  @Test
  public void loggerConfig_intStringConstructor_setBoth() {
    LoggerConfig config = new LoggerConfig(LoggerConfig.ERROR, "another-pattern");
    assertEquals("another-pattern", config.getLogPattern());
    assertEquals(LoggerConfig.ERROR, config.getLogLevel());
  }
}
