package glair.vision;

import glair.vision.logger.LoggerConfig;
import glair.vision.model.VisionSettings;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;

/**
 * Base class for mocked unit tests. Starts a MockWebServer before each test,
 * builds a Vision SDK instance pointed at it, and shuts down after each test.
 *
 * No network and no config.properties are required.
 */
public abstract class MockSdkTest {

  protected MockWebServer server;
  protected Vision vision;

  /** Path to the tiny sample image bundled with the test resources. */
  protected String sampleImagePath;

  @BeforeEach
  public void setUp() throws Exception {
    server = new MockWebServer();
    server.start();

    String baseUrl = server.url("/").toString();
    // Strip trailing slash — Config.getUrl() adds its own separator.
    if (baseUrl.endsWith("/")) {
      baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
    }

    VisionSettings settings = new VisionSettings.Builder()
        .baseUrl(baseUrl)
        .username("u")
        .password("p")
        .apiKey("k")
        .build();

    vision = new Vision(settings, new LoggerConfig(LoggerConfig.ERROR));

    // Resolve sample image from test resources on the classpath
    sampleImagePath = new File(getClass()
        .getClassLoader()
        .getResource("sample.png")
        .toURI())
        .getAbsolutePath();
  }

  @AfterEach
  public void tearDown() throws IOException {
    server.shutdown();
  }

  /**
   * Enqueue a canned JSON response with the given HTTP status code and body.
   */
  protected void enqueueJson(int code, String body) {
    server.enqueue(new MockResponse()
        .setResponseCode(code)
        .addHeader("Content-Type", "application/json")
        .setBody(body));
  }

  /**
   * Wait for and return the next request recorded by the mock server.
   */
  protected RecordedRequest takeRequest() throws InterruptedException {
    return server.takeRequest();
  }
}
