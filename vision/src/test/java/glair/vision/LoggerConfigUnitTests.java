package glair.vision;

import glair.vision.logger.LoggerConfig;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pure unit tests for LoggerConfig — no mock server required.
 */
public class LoggerConfigUnitTests {

  @Test
  public void defaultConstructor_usesDefaultLevelAndPattern() {
    LoggerConfig cfg = new LoggerConfig();
    assertEquals(LoggerConfig.INFO, cfg.getLogLevel());
    assertNotNull(cfg.getLogPattern());
    assertTrue(cfg.getLogPattern().contains("{level}"));
  }

  @Test
  public void logLevelConstructor_setsCustomLevel_debug() {
    LoggerConfig cfg = new LoggerConfig(LoggerConfig.DEBUG);
    assertEquals(LoggerConfig.DEBUG, cfg.getLogLevel());
  }

  @Test
  public void logLevelConstructor_setsCustomLevel_error() {
    LoggerConfig cfg = new LoggerConfig(LoggerConfig.ERROR);
    assertEquals(LoggerConfig.ERROR, cfg.getLogLevel());
  }

  @Test
  public void logPatternConstructor_setsCustomPattern() {
    String pattern = "[{level}] {message}";
    LoggerConfig cfg = new LoggerConfig(pattern);
    assertEquals(pattern, cfg.getLogPattern());
    assertEquals(LoggerConfig.INFO, cfg.getLogLevel());
  }

  @Test
  public void fullConstructor_intLevel_stringPattern() {
    LoggerConfig cfg = new LoggerConfig(LoggerConfig.WARN, "custom-pattern");
    assertEquals(LoggerConfig.WARN, cfg.getLogLevel());
    assertEquals("custom-pattern", cfg.getLogPattern());
  }

  @Test
  public void swappedConstructor_stringPattern_intLevel() {
    LoggerConfig cfg = new LoggerConfig("my-pattern", LoggerConfig.DEBUG);
    assertEquals(LoggerConfig.DEBUG, cfg.getLogLevel());
    assertEquals("my-pattern", cfg.getLogPattern());
  }

  @Test
  public void constants_haveCorrectValues() {
    assertEquals(0, LoggerConfig.DEBUG);
    assertEquals(1, LoggerConfig.INFO);
    assertEquals(2, LoggerConfig.WARN);
    assertEquals(3, LoggerConfig.ERROR);
  }
}
