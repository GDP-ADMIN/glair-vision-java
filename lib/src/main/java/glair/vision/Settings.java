package glair.vision;

public class Settings {
  private final String baseUrl;
  private final String apiVersion;
  private final String apiKey;
  private final String username;
  private final String password;

  private Settings(Builder builder) {
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

  public static class Builder {
    private String baseUrl;
    private String apiVersion;
    private String apiKey;
    private String username;
    private String password;

    public Builder() {}

    public Builder baseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
      return this;
    }

    public Builder apiVersion(String apiVersion) {
      this.apiVersion = apiVersion;
      return this;
    }

    public Builder apiKey(String apiKey) {
      this.apiKey = apiKey;
      return this;
    }

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Settings build() {
      return new Settings(this);
    }
  }
}
