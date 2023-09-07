package glair.vision.api;

import glair.vision.model.VisionSettings;
import glair.vision.util.Json;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

/**
 * The Config class provides configuration settings for the Glair Vision API.
 */
public class Config {
  private static final String BASE_URL = "https://api.vision.glair.ai";
  private static final String API_VERSION = "v1";
  private static final String API_KEY = "default-api-key";
  private static final String USERNAME = "default-username";
  private static final String PASSWORD = "default-password";

  private final String baseUrl;
  private final String apiVersion;
  private final String apiKey;
  private final String username;
  private final String password;
  private final VisionSettings visionSettings;
  private String basicAuth;

  /**
   * Constructs a Config instance based on the provided VisionSettings.
   *
   * @param config The VisionSettings to initialize the configuration.
   */
  public Config(VisionSettings config) {
    this.baseUrl = config.getBaseUrl() == null ? BASE_URL : config.getBaseUrl();
    this.apiVersion = config.getApiVersion() == null ? API_VERSION :
        config.getApiVersion();
    this.apiKey = config.getApiKey() == null ? API_KEY : config.getApiKey();
    this.username = config.getUsername() == null ? USERNAME : config.getUsername();
    this.password = config.getPassword() == null ? PASSWORD : config.getPassword();
    this.visionSettings = config;
  }

  /**
   * Gets the API key associated with the configuration.
   *
   * @return The API key.
   */

  public String getApiKey() {
    return this.apiKey;
  }

  /**
   * Constructs a full URL by combining the base URL and the given path.
   *
   * @param path The path to append to the base URL.
   * @return The full URL.
   */
  public String getUrl(String path) {
    return this.baseUrl + "/" + this.replaceVersion(path);
  }

  /**
   * Gets the Basic Authentication string for HTTP requests.
   *
   * @return The Basic Authentication string.
   */
  public String getBasicAuth() {
    if (this.basicAuth == null) {
      String buffer = Base64
          .getEncoder()
          .encodeToString((this.username + ":" + this.password).getBytes(StandardCharsets.UTF_8));
      String auth = "Basic " + buffer;
      this.basicAuth = auth;
      return auth;
    }

    return this.basicAuth;
  }

  /**
   * Gets a reference to the current Config instance.
   *
   * @return The Config instance.
   */
  public Config getConfig() {
    return this;
  }

  /**
   * Gets a new Config instance with updated settings based on the provided
   * VisionSettings.
   *
   * @param newConfig The VisionSettings with updated settings.
   * @return A new Config instance with the updated settings.
   */
  public Config getConfig(VisionSettings newConfig) {
    String baseUrl = newConfig.getBaseUrl() == null ? this.baseUrl :
        newConfig.getBaseUrl();
    String apiVersion = newConfig.getApiVersion() == null ? this.apiVersion :
        newConfig.getApiVersion();
    String apiKey = newConfig.getApiKey() == null ? this.apiKey : newConfig.getApiKey();
    String username = newConfig.getUsername() == null ? this.username :
        newConfig.getUsername();
    String password = newConfig.getPassword() == null ? this.password :
        newConfig.getPassword();

    VisionSettings visionSettings = new VisionSettings.Builder()
        .baseUrl(baseUrl)
        .apiVersion(apiVersion)
        .apiKey(apiKey)
        .username(username)
        .password(password)
        .build();

    return new Config(visionSettings);
  }

  /**
   * Gets the VisionSettings associated with the configuration.
   *
   * @return The VisionSettings.
   */
  public VisionSettings getSettings() {
    return this.visionSettings;
  }

  /**
   * Replaces the ":version" placeholder in the given path with the API version.
   *
   * @param path The path containing the ":version" placeholder.
   * @return The path with the placeholder replaced by the API version.
   */
  private String replaceVersion(String path) {
    return path.replaceAll(":version", this.apiVersion);
  }

  /**
   * Returns a JSON representation of the configuration.
   *
   * @return A JSON string representing the configuration settings.
   */
  @Override
  public String toString() {
    HashMap<String, String> map = new HashMap<>();
    map.put("baseUrl", this.baseUrl);
    map.put("apiVersion", this.apiVersion);
    map.put("apiKey", this.apiKey);
    map.put("username", this.username);
    map.put("password", this.password);

    return "Config " + Json.toJsonString(map, 2);
  }
}
