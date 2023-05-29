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
import org.apache.http.entity.mime.content.FileBody;
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

    // API endpoint URL
    String apiEndpoint = config.getUrl(path);
    logger.debug("URL " + Util.json("url", apiEndpoint));

    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpRequestBase httpRequestBase;

    if (method.equalsIgnoreCase("GET")) {
      HttpPost httpPost = new HttpPost(apiEndpoint);
      httpRequestBase = httpPost;

    } else if (method.equalsIgnoreCase("POST")) {
      HttpPost httpPost = new HttpPost(apiEndpoint);

      httpPost.setHeader(HttpHeaders.AUTHORIZATION, config.getBasicAuth());
      httpPost.setHeader("x-api-key", config.getApiKey());

      httpPost.setEntity(body);

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

    if (response.getStatusLine().getStatusCode() == 200) {
      return responseBody;
    }

    throw new Exception(responseBody);
  }

  public static void addImageToFormData(MultipartEntityBuilder entityBuilder,
                                        String name, String imagePath) {
    File imageFile = new File(imagePath);
    FileBody fileBody = new FileBody(imageFile);
    entityBuilder.addPart(name, fileBody);
  }

  public static HttpEntity createJsonBody(HashMap<String, String> map) {
    String jsonString = Util.json(map);
    return new StringEntity(jsonString, ContentType.APPLICATION_JSON);
  }

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

  private static String toString(String key, String value) {
    return "\"" + key + "\": \"" + value + "\"";
  }

  public static String json(String key, String value) {
    return "{" + toString(key, value) + "}";
  }

  public static String json(HashMap<String, String> map) {
    String result = "{";
    boolean first = true;

    for (String key: map.keySet()) {
      String value = map.get(key);
      if (first) {
        result = result + toString(key, value);
        first = false;
      } else {
        result = result + ", " + toString(key, value);
      }
    }

    return result + "}";
  }
}
