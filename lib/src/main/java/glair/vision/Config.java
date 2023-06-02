package glair.vision;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

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
  private final Settings settings;
  private String basicAuth;

  public Config(Settings config) {
    this.baseUrl = config.getBaseUrl() == null ? BASE_URL : config.getBaseUrl();
    this.apiVersion = config.getApiVersion() == null ? API_VERSION :
        config.getApiVersion();
    this.apiKey = config.getApiKey() == null ? API_KEY : config.getApiKey();
    this.username = config.getUsername() == null ? USERNAME :
        config.getUsername();
    this.password = config.getPassword() == null ? PASSWORD :
        config.getPassword();
    this.settings = config;
  }

  public String getApiKey() {
    return this.apiKey;
  }

  public String getUrl(String path) {
    return this.baseUrl + "/" + this.replaceVersion(path);
  }

  public String getBasicAuth() {
    if (this.basicAuth == null) {
      String buffer = Base64
          .getEncoder()
          .encodeToString((this.username + ":" + this.password).getBytes(
              StandardCharsets.UTF_8));
      String auth = "Basic " + buffer;
      this.basicAuth = auth;
      return auth;
    }

    return this.basicAuth;
  }

  public Config getConfig() {
    return this;
  }

  public Config getConfig(Settings newConfig) {
    String baseUrl = newConfig.getBaseUrl() == null ? this.baseUrl :
        newConfig.getBaseUrl();
    String apiVersion = newConfig.getApiVersion() == null ? this.apiVersion :
        newConfig.getApiVersion();
    String apiKey = newConfig.getApiKey() == null ? this.apiKey :
        newConfig.getApiKey();
    String username = newConfig.getUsername() == null ? this.username :
        newConfig.getUsername();
    String password = newConfig.getPassword() == null ? this.password :
        newConfig.getPassword();

    Settings settings = new Settings.Builder()
        .baseUrl(baseUrl)
        .apiVersion(apiVersion)
        .apiKey(apiKey)
        .username(username)
        .password(password)
        .build();
    return new Config(settings);
  }

  public Settings getSettings() {
    return this.settings;
  }

  private String replaceVersion(String path) {
    return path.replaceAll(":version", this.apiVersion);
  }

  @Override
  public String toString() {
    HashMap<String, String> map = new HashMap<>();
    map.put("baseUrl", this.baseUrl);
    map.put("apiVersion", this.apiVersion);
    map.put("apiKey", this.apiKey);
    map.put("username", this.username);
    map.put("password", this.password);

    return "Config " + Util.json(map, 2);
  }
}
