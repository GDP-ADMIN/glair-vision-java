package glair.vision.model;

import org.apache.http.HttpEntity;

/**
 * Represents an HTTP request configuration.
 */
public class Request {
  private final String path;
  private final String method;
  private final HttpEntity body;

  /**
   * Constructs a new HTTP request using the provided builder.
   *
   * @param builder The builder used to construct this HTTP request.
   */
  private Request(RequestBuilder builder) {
    this.path = builder.path;
    this.method = builder.method;
    this.body = builder.body;
  }

  /**
   * Gets the path of the HTTP request.
   *
   * @return The request path.
   */
  public String getPath() {
    return path;
  }

  /**
   * Gets the HTTP method (e.g., GET, POST) of the request.
   *
   * @return The request method.
   */
  public String getMethod() {
    return method;
  }

  /**
   * Gets the HTTP request body, if present.
   *
   * @return The request body as an HttpEntity.
   */
  public HttpEntity getBody() {
    return body;
  }

  /**
   * Builder class for creating instances of HTTP request configurations.
   */
  public static class RequestBuilder {
    private final String path;
    private final String method;
    private HttpEntity body;

    /**
     * Constructs a new request builder with the specified path and HTTP method.
     *
     * @param path   The request path.
     * @param method The HTTP method (e.g., GET, POST).
     */
    public RequestBuilder(String path, String method) {
      this.path = path;
      this.method = method;
    }

    /**
     * Sets the HTTP request body.
     *
     * @param body The request body as an HttpEntity.
     * @return The builder instance for method chaining.
     */
    public RequestBuilder body(HttpEntity body) {
      this.body = body;
      return this;
    }

    /**
     * Builds a new instance of an HTTP request with the configured options.
     *
     * @return The constructed Request instance.
     */
    public Request build() {
      return new Request(this);
    }
  }
}
