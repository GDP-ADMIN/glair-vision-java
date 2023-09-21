package glair.vision.model;

/**
 * Represents configuration settings for a vision-related service.
 */
public class VisionSettings {
  private final String baseUrl;
  private final String apiVersion;
  private final String apiKey;
  private final String username;
  private final String password;

  /**
   * Constructs a new instance of VisionSettings using the provided builder.
   *
   * @param builder The builder used to construct this VisionSettings instance.
   */
  private VisionSettings(Builder builder) {
    this.baseUrl = builder.baseUrl;
    this.apiVersion = builder.apiVersion;
    this.apiKey = builder.apiKey;
    this.username = builder.username;
    this.password = builder.password;
  }

  /**
   * Gets the base URL for the vision-related service.
   *
   * @return The base URL.
   */
  public String getBaseUrl() {
    return baseUrl;
  }

  /**
   * Gets the API version used for communication with the service.
   *
   * @return The API version.
   */
  public String getApiVersion() {
    return apiVersion;
  }

  /**
   * Gets the API key used for authentication with the service.
   *
   * @return The API key.
   */
  public String getApiKey() {
    return apiKey;
  }

  /**
   * Gets the username used for authentication with the service (if applicable).
   *
   * @return The username.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Gets the password used for authentication with the service (if applicable).
   *
   * @return The password.
   */
  public String getPassword() {
    return password;
  }

  /**
   * A builder class for creating instances of VisionSettings with specific
   * configurations.
   */
  public static class Builder {
    private String baseUrl;
    private String apiVersion;
    private String apiKey;
    private String username;
    private String password;

    /**
     * Sets the base URL for the vision-related service.
     *
     * @param baseUrl The base URL.
     * @return The builder instance for method chaining.
     */
    public Builder baseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
      return this;
    }

    /**
     * Sets the API version for communication with the service.
     *
     * @param apiVersion The API version.
     * @return The builder instance for method chaining.
     */
    public Builder apiVersion(String apiVersion) {
      this.apiVersion = apiVersion;
      return this;
    }

    /**
     * Sets the API key for authentication with the service.
     *
     * @param apiKey The API key.
     * @return The builder instance for method chaining.
     */
    public Builder apiKey(String apiKey) {
      this.apiKey = apiKey;
      return this;
    }

    /**
     * Sets the username for authentication with the service (if applicable).
     *
     * @param username The username.
     * @return The builder instance for method chaining.
     */
    public Builder username(String username) {
      this.username = username;
      return this;
    }

    /**
     * Sets the password for authentication with the service (if applicable).
     *
     * @param password The password.
     * @return The builder instance for method chaining.
     */
    public Builder password(String password) {
      this.password = password;
      return this;
    }

    /**
     * Builds a new instance of VisionSettings with the configured options.
     *
     * @return The constructed VisionSettings instance.
     */
    public VisionSettings build() {
      return new VisionSettings(this);
    }
  }
}
