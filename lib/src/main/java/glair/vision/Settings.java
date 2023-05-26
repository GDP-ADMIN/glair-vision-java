package glair.vision;

public class Settings {
  private final String baseUrl;
  private final String apiVersion;
  private final String apiKey;
  private final String username;
  private final String password;

  public static class SettingsBuilder {
    private String baseUrl;
    private String apiVersion;
    private String apiKey;
    private String username;
    private String password;

    public SettingsBuilder() {}

    public SettingsBuilder baseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
      return this;
    }

    public SettingsBuilder apiVersion(String apiVersion) {
      this.apiVersion = apiVersion;
      return this;
    }

    public SettingsBuilder apiKey(String apiKey) {
      this.apiKey = apiKey;
      return this;
    }

    public SettingsBuilder username(String username) {
      this.username = username;
      return this;
    }

    public SettingsBuilder password(String password) {
      this.password = password;
      return this;
    }

    public Settings build() {
      return new Settings(this);
    }
  }

  private Settings(SettingsBuilder builder) {
    this.baseUrl = builder.baseUrl;
    this.apiVersion = builder.apiVersion;
    this.apiKey = builder.apiKey;
    this.username = builder.username;
    this.password = builder.password;
  }

  public String getBaseUrl() {
    return this.baseUrl;
  }

  public String getApiVersion() {
    return this.apiVersion;
  }

  public String getApiKey() {
    return this.apiKey;
  }

  public String getUsername() {
    return this.username;
  }

  public String getPassword() {
    return this.password;
  }
}
