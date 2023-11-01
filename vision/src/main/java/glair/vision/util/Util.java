package glair.vision.util;

import glair.vision.Vision;
import glair.vision.api.Config;
import glair.vision.logger.Logger;
import glair.vision.model.Request;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for common operations and HTTP requests.
 */
public class Util {
  private static final Logger logger = Logger.getInstance();
  private static final OkHttpClient client = new OkHttpClient.Builder()
      .connectTimeout(5, TimeUnit.MINUTES)
      .writeTimeout(5, TimeUnit.MINUTES)
      .readTimeout(5, TimeUnit.MINUTES)
      .build();

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
    String method = request.getMethod();
    String apiEndpoint = config.getUrl(request.getPath());

    logger.debug("URL", Json.toJsonString("url", apiEndpoint));
    logger.debug(config);

    okhttp3.Request.Builder httpRequestBuilder = new okhttp3.Request.Builder()
        .header("Authorization", config.getBasicAuth())
        .header("x-api-key", config.getApiKey())
        .header("GLAIR-Vision-Java-SDK-Version", Vision.version)
        .url(apiEndpoint);

    if (method.equalsIgnoreCase("GET")) {
      httpRequestBuilder.get();
    } else if (method.equalsIgnoreCase("POST")) {
      httpRequestBuilder.post(request.getBody());
    }

    okhttp3.Request httpRequest = httpRequestBuilder.build();

    try (Response response = client.newCall(httpRequest).execute()) {
      String body = response.body() != null ? response.body().string() : "null";
      if (response.isSuccessful()) {
        return body;
      } else {
        throw new Exception(body);
      }
    }
  }

  /**
   * Creates and returns a new instance of {@link MultipartBody.Builder} configured for
   * forming HTTP multipart requests with form data.
   *
   * @return A {@link MultipartBody.Builder} configured for form data.
   */
  public static MultipartBody.Builder createFormData() {
    return new MultipartBody.Builder().setType(MultipartBody.FORM);
  }

  /**
   * Adds a file to the specified {@link MultipartBody.Builder} as form data.
   *
   * @param builder   The {@link MultipartBody.Builder} to which the file should be added.
   * @param fieldName The name of the form field for the file.
   * @param filePath  The path to the file to be added.
   */
  public static void addFileToFormData(
      MultipartBody.Builder builder, String fieldName, String filePath
  ) {
    File file = new File(filePath);
    RequestBody filePart = RequestBody.create(file, MediaType.parse(getMimeType(file)));

    builder.addFormDataPart(fieldName, file.getName(), filePart);
  }

  /**
   * Adds text data to the specified {@link MultipartBody.Builder} as form data.
   *
   * @param builder    The {@link MultipartBody.Builder} to which the text data should
   *                   be added.
   * @param fieldName  The name of the form field for the text data.
   * @param fieldValue The value of the text data.
   */
  public static void addTextToFormData(
      MultipartBody.Builder builder, String fieldName, String fieldValue
  ) {
    builder.addFormDataPart(fieldName, fieldValue);
  }

  private static String getMimeType(File file) {
    return URLConnection.guessContentTypeFromName(file.getName());
  }

  /**
   * Converts a file located at the specified path to a Base64-encoded string.
   *
   * @param filePath The path to the file to be converted to Base64.
   * @return A Base64-encoded string representing the content of the file.
   * @throws Exception If an error occurs during the file conversion process.
   */
  public static String fileToBase64(String filePath) throws Exception {
    File file = new File(filePath);
    byte[] buffer = new byte[(int) file.length() + 100];
    @SuppressWarnings("resource") int length = new FileInputStream(file).read(buffer);

    return Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);
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
    File file = new File(filePath);

    if (!file.exists()) {
      throw new Exception("The file does not exist.");
    }
  }
}
