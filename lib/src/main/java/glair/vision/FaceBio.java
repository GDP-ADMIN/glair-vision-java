package glair.vision;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class FaceBio {
  private static final Logger logger = LogManager.getLogger();

  private final Config config;

  public FaceBio(Config config) {
    this.config = config;
  }

  public String match(MatchParam param) {
    logger.info("Face Biometric - Match " + param);

    HashMap<String, String> map = new HashMap<>();
    map.put("captured_image", Util.fileToBase64(param.captured));
    map.put("stored_image", Util.fileToBase64(param.stored));
    HttpEntity body = Util.createJsonBody(map);

    Request request = new Request.RequestBuilder("face/:version/match",
        "POST").body(body).build();

    String response;

    try {
      response = Util.visionFetch(this.config, request);
    } catch (Exception e) {
      response = e.getMessage();
    }

    return response;
  }

  public String passiveLiveness(PassiveLivenessParam param) {
    logger.info("Face Biometric - Passive Liveness " + param);

    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
    Util.addImageToFormData(multipartEntityBuilder, "image", param.image);
    HttpEntity body = multipartEntityBuilder.build();

    Request request = new Request.RequestBuilder("face/:version/passive" +
        "-liveness",
        "POST").body(body).build();

    String response;

    try {
      response = Util.visionFetch(this.config, request);
    } catch (Exception e) {
      response = e.getMessage();
    }

    return response;
  }

  public String activeLiveness(ActiveLivenessParam param) {
    logger.info("Face Biometric - Active Liveness " + param);

    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
    Util.addImageToFormData(multipartEntityBuilder, "image", param.image);
    multipartEntityBuilder.addTextBody("gesture-code", param.gestureCode,
        ContentType.TEXT_PLAIN);
    HttpEntity body = multipartEntityBuilder.build();

    Request request = new Request.RequestBuilder("face/:version/active" +
        "-liveness",
        "POST").body(body).build();

    String response;

    try {
      response = Util.visionFetch(this.config, request);
    } catch (Exception e) {
      response = e.getMessage();
    }

    return response;
  }

  public static class MatchParam {
    private final String captured;
    private final String stored;
    private final HashMap<String, String> map;

    public MatchParam(String captured, String stored) {
      this.captured = captured;
      this.stored = stored;

      HashMap<String, String> map = new HashMap<>();
      map.put("captured", this.captured);
      map.put("stored", this.stored);

      this.map = map;
    }

    @Override
    public String toString() {
      return Util.json(this.map);
    }
  }

  public static class ActiveLivenessParam {
    private final String image;
    private final String gestureCode;
    private final HashMap<String, String> map;

    public ActiveLivenessParam(String image, String gestureCode) {
      this.image = image;
      this.gestureCode = gestureCode;

      HashMap<String, String> map = new HashMap<>();
      map.put("image", this.image);
      map.put("gestureCode", this.gestureCode);

      this.map = map;
    }

    @Override
    public String toString() {
      return Util.json(this.map);
    }
  }

  public record PassiveLivenessParam(String image) {
    @Override
    public String toString() {
      return Util.json("image", this.image);
    }
  }
}
