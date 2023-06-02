package glair.vision;

import org.apache.http.HttpEntity;

public class Request {
  private final String path;
  private final String method;
  private final HttpEntity body;

  private Request(RequestBuilder builder) {
    this.path = builder.path;
    this.method = builder.method;
    this.body = builder.body;
  }

  public String getPath() {
    return this.path;
  }

  public String getMethod() {
    return this.method;
  }

  public HttpEntity getBody() {
    return this.body;
  }

  public static class RequestBuilder {
    private final String path;
    private final String method;
    private HttpEntity body;

    public RequestBuilder(String path, String method) {
      this.path = path;
      this.method = method;
    }

    public RequestBuilder body(HttpEntity body) {
      this.body = body;
      return this;
    }

    public Request build() {
      return new Request(this);
    }
  }
}
