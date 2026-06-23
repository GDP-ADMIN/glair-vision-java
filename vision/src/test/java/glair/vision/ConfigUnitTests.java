package glair.vision;

import glair.vision.api.Config;
import glair.vision.model.VisionSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Config — covers toString(), getConfig() no-arg, getConfig(VisionSettings)
 * override branches, getUrl(), getBasicAuth() caching, and default fallback values.
 */
public class ConfigUnitTests {

  private VisionSettings defaultSettings() {
    return new VisionSettings.Builder()
        .username("user")
        .password("pass")
        .apiKey("key")
        .baseUrl("https://test.url")
        .apiVersion("v2")
        .build();
  }

  // -----------------------------------------------------------------------
  // toString()
  // -----------------------------------------------------------------------

  @Test
  public void config_toString_containsConfiguredValues() {
    Config config = new Config(defaultSettings());
    String str = config.toString();
    assertNotNull(str);
    assertTrue(str.contains("key"), "toString should contain the apiKey");
    assertTrue(str.contains("user"), "toString should contain the username");
  }

  @Test
  public void config_toString_containsBaseUrl() {
    Config config = new Config(defaultSettings());
    String str = config.toString();
    assertTrue(str.contains("https://test.url"), "toString should contain baseUrl");
  }

  // -----------------------------------------------------------------------
  // getConfig() no-arg — returns self
  // -----------------------------------------------------------------------

  @Test
  public void config_getConfig_noArg_returnsSelf() {
    Config config = new Config(defaultSettings());
    assertSame(config, config.getConfig());
  }

  // -----------------------------------------------------------------------
  // getUrl() — replaces :version placeholder
  // -----------------------------------------------------------------------

  @Test
  public void config_getUrl_replacesVersionPlaceholder() {
    Config config = new Config(defaultSettings());
    String url = config.getUrl("ocr/:version/ktp");
    assertTrue(url.contains("v2"), "URL should contain configured apiVersion");
    assertTrue(url.contains("ocr/v2/ktp"), "URL should replace :version with v2");
  }

  @Test
  public void config_getUrl_prependsBaseUrl() {
    Config config = new Config(defaultSettings());
    String url = config.getUrl("some/path");
    assertTrue(url.startsWith("https://test.url/"), "URL should start with the configured baseUrl");
  }

  // -----------------------------------------------------------------------
  // getConfig(VisionSettings) — override branches
  // -----------------------------------------------------------------------

  @Test
  public void config_getConfig_withNewSettings_overridesApiKey() {
    Config base = new Config(defaultSettings());
    VisionSettings overrideSettings = new VisionSettings.Builder()
        .apiKey("new-key")
        .build();
    Config overridden = base.getConfig(overrideSettings);
    assertEquals("new-key", overridden.getApiKey());
  }

  @Test
  public void config_getConfig_withNullFields_keepsBaseValues() {
    Config base = new Config(defaultSettings());
    // All fields null in new settings → should keep base values
    VisionSettings emptySettings = new VisionSettings.Builder().build();
    Config merged = base.getConfig(emptySettings);
    assertEquals("key", merged.getApiKey());
  }

  @Test
  public void config_getConfig_withOverrideSettings_returnsNewInstance() {
    Config base = new Config(defaultSettings());
    VisionSettings overrideSettings = new VisionSettings.Builder()
        .apiKey("override-key")
        .build();
    Config overridden = base.getConfig(overrideSettings);
    assertNotSame(base, overridden, "getConfig(VisionSettings) should return a new Config instance");
  }

  // -----------------------------------------------------------------------
  // Default fallback values when VisionSettings fields are null
  // -----------------------------------------------------------------------

  @Test
  public void config_defaultSettings_usesDefaults() {
    VisionSettings empty = new VisionSettings.Builder().build();
    Config config = new Config(empty);
    // Should fall back to "default-api-key"
    assertNotNull(config.getApiKey());
    assertFalse(config.getApiKey().isEmpty());
    // getBasicAuth() should not throw and should not be null
    assertNotNull(config.getBasicAuth());
  }

  // -----------------------------------------------------------------------
  // getSettings() — returns original VisionSettings
  // -----------------------------------------------------------------------

  @Test
  public void config_getSettings_returnsOriginalSettings() {
    VisionSettings settings = defaultSettings();
    Config config = new Config(settings);
    assertSame(settings, config.getSettings());
  }

  // -----------------------------------------------------------------------
  // getBasicAuth() — covers both first-call (null) and cached paths
  // -----------------------------------------------------------------------

  @Test
  public void config_getBasicAuth_firstCall_returnsNonNull() {
    Config config = new Config(defaultSettings());
    String auth = config.getBasicAuth();
    assertNotNull(auth);
    assertFalse(auth.isEmpty());
  }

  @Test
  public void config_getBasicAuth_cachedSecondCall_returnsSameValue() {
    Config config = new Config(defaultSettings());
    String first = config.getBasicAuth();
    String second = config.getBasicAuth();
    assertEquals(first, second, "getBasicAuth() should return the same cached value on repeated calls");
  }
}
