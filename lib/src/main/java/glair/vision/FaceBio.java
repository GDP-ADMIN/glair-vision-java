package glair.vision;

import glair.vision.sessions.ActiveLivenessSessions;
import glair.vision.sessions.PassiveLivenessSessions;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class FaceBio {
  private static final Logger logger = LogManager.getLogger();

  private final Config config;
  private final ActiveLivenessSessions activeLivenessSessions;
  private final PassiveLivenessSessions passiveLivenessSessions;

  public FaceBio(Config config) {
    this.config = config;
    this.activeLivenessSessions = new ActiveLivenessSessions(config);
    this.passiveLivenessSessions = new PassiveLivenessSessions(config);
  }

  public ActiveLivenessSessions activeLivenessSessions() {
    return this.activeLivenessSessions;
  }

  public PassiveLivenessSessions passiveLivenessSessions() {
    return this.passiveLivenessSessions;
  }

  public String match(MatchParam param) throws Exception {
    return match(param, this.config.getSettings());
  }

  public String match(MatchParam param, Settings newSettings) throws Exception {
    logger.info("Face Biometric - Match " + param);

    String url = "face/:version/match";
    String method = "POST";

    HashMap<String, String> map = new HashMap<>();
    map.put("captured_image", Util.fileToBase64(param.captured));
    map.put("stored_image", Util.fileToBase64(param.stored));
    HttpEntity body = Util.createJsonBody(map);

    Request request = new Request.RequestBuilder(url, method)
        .body(body)
        .build();

    return Util.visionFetch(this.config.getConfig(newSettings), request);
  }

  public String passiveLiveness(PassiveLivenessParam param) throws Exception {
    return passiveLiveness(param, this.config.getSettings());
  }

  public String passiveLiveness(PassiveLivenessParam param,
                                Settings newSettings) throws Exception {
    logger.info("Face Biometric - Passive Liveness " + param);

    String url = "face/:version/passive-liveness";
    String method = "POST";

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    Util.addImageToFormData(bodyBuilder, "image", param.image);
    HttpEntity body = bodyBuilder.build();

    Request request = new Request.RequestBuilder(url, method)
        .body(body)
        .build();

    return Util.visionFetch(this.config.getConfig(newSettings), request);
  }

  public String activeLiveness(ActiveLivenessParam param) throws Exception {
    return activeLiveness(param, this.config.getSettings());
  }

  public String activeLiveness(ActiveLivenessParam param,
                               Settings newSettings) throws Exception {
    logger.info("Face Biometric - Active Liveness " + param);

    String url = "face/:version/active-liveness";
    String method = "POST";

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    Util.addImageToFormData(bodyBuilder, "image", param.image);
    bodyBuilder.addTextBody("gesture-code", param.gestureCode);
    HttpEntity body = bodyBuilder.build();

    Request request = new Request.RequestBuilder(url, method)
        .body(body)
        .build();

    return Util.visionFetch(this.config.getConfig(newSettings), request);
  }

  public record MatchParam(String captured, String stored) {
    @Override
    public String toString() {
      HashMap<String, String> map = new HashMap<>();
      map.put("captured", this.captured);
      map.put("stored", this.stored);

      return Util.json(map, 2);
    }
  }

  public record ActiveLivenessParam(String image, String gestureCode) {
    @Override
    public String toString() {
      HashMap<String, String> map = new HashMap<>();
      map.put("image", this.image);
      map.put("gestureCode", this.gestureCode);

      return Util.json(map, 2);
    }
  }

  public record PassiveLivenessParam(String image) {
    @Override
    public String toString() {
      return Util.json("image", this.image);
    }
  }
}
