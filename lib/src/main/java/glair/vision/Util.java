package glair.vision;

import java.io.File;
import java.nio.charset.StandardCharsets;

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

public class Util {
  public static String visionFetch(Config config, Request request) throws Exception {
    String path = request.getPath(), method = request.getMethod();
    HttpEntity body = request.getBody();

    // API endpoint URL
    String apiEndpoint = config.getUrl(path);

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

  public static HttpEntity createJsonBody(String jsonPayload) {
    return new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
  }
}
