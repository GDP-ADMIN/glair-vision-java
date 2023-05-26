package glair.vision;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
public class Config {
  public static final String BASE_URL = "https://api.vision.glair.ai";
  public static final String API_VERSION = "v1";
  public static final String API_KEY = "default-api-key";
  public static final String USERNAME = "default-username";
  public static final String PASSWORD = "default-password";

  private final String baseUrl;
  private final String apiVersion;
  private final String apiKey;
  private final String username;
  private final String password;
  private String basicAuth;

  public Config(Settings config) {
    this.baseUrl = config.getBaseUrl() == null ? Config.BASE_URL :
        config.getBaseUrl();
    this.apiVersion = config.getApiVersion() == null ? Config.API_VERSION :
        config.getApiVersion();
    this.apiKey = config.getApiKey() == null ? Config.API_KEY :
        config.getApiKey();
    this.username = config.getUsername() == null ? Config.USERNAME :
        config.getUsername();
    this.password = config.getPassword() == null ? Config.PASSWORD :
        config.getPassword();
  }

  public String getApiKey() {
    return this.apiKey;
  }

  public String getUrl(String path) {
    return this.baseUrl + "/" + this.replaceVersion(path);
  }

  public String getBasicAuth() {
    if (this.basicAuth == null) {
      String buff =
          Base64.getEncoder().encodeToString((this.username + ":" + this.password).getBytes(
              StandardCharsets.UTF_8));
      String auth = "Basic " + buff;
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

    Settings settings =
        new Settings.SettingsBuilder().baseUrl(baseUrl).apiVersion(apiVersion).apiKey(
            apiKey).username(username).password(password).build();
    return new Config(settings);
  }

  public void print() {
    System.out.println(this.baseUrl + " " + this.apiVersion + " " + this.apiKey + " " + this.username + " " + this.password);
  }

  private String replaceVersion(String path) {
    return path.replaceAll(":version", this.apiVersion);
  }
}
