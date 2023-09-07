package glair.vision.util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;

import glair.vision.Vision;
import glair.vision.api.Config;
import glair.vision.logger.Logger;
import glair.vision.model.Request;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Utility class for common operations and HTTP requests.
 */
public class Util {
  private static final Logger logger = Logger.getInstance();

  /**
   * Performs an HTTP request and fetches data from a specified endpoint.
   *
   * @param config  The configuration settings for the request.
   * @param request The HTTP request to be executed.
   * @return The response data from the HTTP request.
   * @throws Exception If an error occurs during the HTTP request or if the response
   *                   status is not OK (200).
   */
  public static String visionFetch(Config config, Request request) throws Exception {
    String path = request.getPath();
    String method = request.getMethod();
    HttpEntity body = request.getBody();

    String apiEndpoint = config.getUrl(path);

    logger.debug("URL", Json.toJsonString("url", apiEndpoint));
    logger.debug(config);

    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpRequestBase httpRequestBase;

      if (method.equalsIgnoreCase("GET")) {
        httpRequestBase = new HttpGet(apiEndpoint);
      } else if (method.equalsIgnoreCase("POST")) {
        HttpPost httpPost = new HttpPost(apiEndpoint);

        if (body != null) {
          httpPost.setEntity(body);
        }

        httpRequestBase = httpPost;
      } else {
        throw new Exception("Wrong Request Method");
      }

      setCommonHeaders(httpRequestBase, config);

      try (CloseableHttpResponse response = httpClient.execute(httpRequestBase)) {
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity(),
            StandardCharsets.UTF_8);

        if (statusCode == HttpStatus.SC_OK) {
          return responseBody;
        } else {
          throw new Exception(responseBody);
        }
      }
    }
  }

  /**
   * Sets common HTTP headers for the given HTTP request based on the provided
   * configuration.
   *
   * @param httpRequest The HTTP request for which headers need to be set.
   * @param config      The configuration settings containing headers to be set.
   */
  private static void setCommonHeaders(HttpRequestBase httpRequest, Config config) {
    httpRequest.setHeader(HttpHeaders.AUTHORIZATION, config.getBasicAuth());
    httpRequest.setHeader("x-api-key", config.getApiKey());
    httpRequest.setHeader("GLAIR-Vision-Java-SDK-Version", Vision.version);
  }

  /**
   * Adds a file to the given form data entity builder.
   *
   * @param entityBuilder The entity builder for the form data.
   * @param name          The name of the file field.
   * @param filePath      The path to the file.
   * @throws Exception If there is an issue adding the file to the entity.
   */
  public static void addFileToFormData(MultipartEntityBuilder entityBuilder,
                                       String name, String filePath) throws Exception {
    File file = new File(filePath);
    ContentType contentType = ContentType.create(Files.probeContentType(file.toPath()));
    entityBuilder.addBinaryBody(name, file, contentType, file.getName());
  }

  /**
   * Creates a JSON request body from a HashMap.
   *
   * @param map The HashMap containing key-value pairs for the JSON.
   * @return The HTTP entity representing the JSON request body.
   */
  public static HttpEntity createJsonBody(HashMap<String, String> map) {
    String jsonString = Json.toJsonString(map);
    return new StringEntity(jsonString, ContentType.APPLICATION_JSON);
  }

  /**
   * Converts a file to a Base64-encoded string.
   *
   * @param filePath The path to the file.
   * @return The Base64-encoded string.
   */
  public static String fileToBase64(String filePath) {
    try {
      Path path = Paths.get(filePath);
      byte[] imageBytes = Files.readAllBytes(path);
      return Base64.getEncoder().encodeToString(imageBytes);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Checks if a value is null and throws an exception if it is.
   *
   * @param key   The key associated with the value.
   * @param value The value to check.
   * @throws Exception If the value is null.
   */
  public static void require(String key, String value) throws Exception {
    if (value == null) {
      throw new Exception("Require " + key);
    }
  }

  /**
   * Removes all whitespace from a string.
   *
   * @param str The input string.
   * @return The string with all whitespace removed.
   */
  public static String trimAll(String str) {
    return str.replaceAll("\\s+", "");
  }

  /**
   * Checks if a file exists at the specified file path.
   *
   * @param filePath The path to the file to be checked.
   * @throws Exception If the file does not exist or an error occurs during the check.
   */
  public static void checkFileExist(String filePath) throws Exception {
    Path path = Paths.get(filePath);
    boolean fileExists = Files.exists(path);

    if (!fileExists) {
      throw new Exception("The file does not exist.");
    }
  }
}
