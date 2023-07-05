package glair.vision;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Util {
  private static final Logger logger = LogManager.getLogger();

  public static String visionFetch(Config config, Request request) throws Exception {
    String path = request.getPath(), method = request.getMethod();
    HttpEntity body = request.getBody();

    String apiEndpoint = config.getUrl(path);

    logger.debug("URL " + Util.json("url", apiEndpoint));

    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpRequestBase httpRequestBase;

    if (method.equalsIgnoreCase("GET")) {
      HttpGet httpGet = new HttpGet(apiEndpoint);

      httpGet.setHeader(HttpHeaders.AUTHORIZATION, config.getBasicAuth());
      httpGet.setHeader("x-api-key", config.getApiKey());
      httpGet.setHeader("GLAIR-Vision-Java-SDK-Version", "0.0.1-beta.1");

      httpRequestBase = httpGet;
    } else if (method.equalsIgnoreCase("POST")) {
      HttpPost httpPost = new HttpPost(apiEndpoint);

      httpPost.setHeader(HttpHeaders.AUTHORIZATION, config.getBasicAuth());
      httpPost.setHeader("x-api-key", config.getApiKey());
      httpPost.setHeader("GLAIR-Vision-Java-SDK-Version", "0.0.1-beta.1");

      if (body != null) {
        httpPost.setEntity(body);
      }

      httpRequestBase = httpPost;
    } else {
      throw new Exception("Wrong Request Method");
    }

    // Execute the request
    CloseableHttpResponse response = httpClient.execute(httpRequestBase);

    // Get the response body
    HttpEntity responseEntity = response.getEntity();
    String responseBody = EntityUtils.toString(responseEntity,
        StandardCharsets.UTF_8);

    response.close();
    httpClient.close();

    if (response
        .getStatusLine()
        .getStatusCode() == 200) {
      return responseBody;
    }

    throw new Exception(responseBody);
  }

  public static void addFileToFormData(MultipartEntityBuilder entityBuilder,
                                       String name, String filePath) throws Exception {
    File file = new File(filePath);
    entityBuilder.addBinaryBody(name, file,
        ContentType.create(Files.probeContentType(file.toPath())), file.getName());
  }

  public static HttpEntity createJsonBody(HashMap<String, String> map) {
    String jsonString = Util.json(map);
    return new StringEntity(jsonString, ContentType.APPLICATION_JSON);
  }

  public static String fileToBase64(String filePath) {
    try {
      Path path = Paths.get(filePath);
      byte[] imageBytes = Files.readAllBytes(path);
      return Base64
          .getEncoder()
          .encodeToString(imageBytes);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private static String formatKeyValueString(String key, String value) {
    return "\"" + key + "\": \"" + value + "\"";
  }

  public static String json(String key, String value) {
    return "{" + formatKeyValueString(key, value) + "}";
  }

  public static String json(HashMap<String, String> map) {
    StringBuilder stringBuilder = new StringBuilder();
    boolean first = true;

    stringBuilder.append("{");

    for (String key : map.keySet()) {
      String value = map.get(key);
      if (first) {
        stringBuilder.append(formatKeyValueString(key, value));
        first = false;
      } else {
        stringBuilder.append(", ");
        stringBuilder.append(formatKeyValueString(key, value));
      }
    }

    stringBuilder.append("}");

    return stringBuilder.toString();
  }

  public static String json(HashMap<String, String> map, int indent) {
    StringBuilder stringBuilder = new StringBuilder();
    String tab = " ".repeat(indent);
    boolean first = true;

    stringBuilder.append("{");

    for (String key : map.keySet()) {
      String value = map.get(key);
      if (first) {
        stringBuilder.append("\n");
        stringBuilder.append(tab);
        stringBuilder.append(formatKeyValueString(key, value));
        first = false;
      } else {
        stringBuilder.append(",\n");
        stringBuilder.append(tab);
        stringBuilder.append(formatKeyValueString(key, value));
      }
    }

    stringBuilder.append("\n}");

    return stringBuilder.toString();
  }

  public static void require(String key, String value) throws Exception {
    if (value == null) {
      throw new Exception("Require " + key);
    }
  }
}
