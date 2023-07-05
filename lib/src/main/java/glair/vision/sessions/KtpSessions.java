package glair.vision.sessions;

import glair.vision.Config;
import glair.vision.Request;
import glair.vision.Settings;
import glair.vision.Util;
import org.apache.http.HttpEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class KtpSessions {
  private static final Logger logger = LogManager.getLogger();

  private final Config config;

  public KtpSessions(Config config) {
    this.config = config;
  }

  public String create(CreateParam param) throws Exception {
    return create(param, this.config.getSettings());
  }

  public String create(CreateParam param, Settings newSettings) throws Exception {
    logger.info("KTP Sessions - Create " + param);

    String url = "ocr/:version/ktp-sessions";
    String method = "POST";

    HashMap<String, String> map = new HashMap<>();
    map.put("success_url", param.successUrl);
    map.put("cancel_url", param.cancelUrl);
    HttpEntity body = Util.createJsonBody(map);

    Request request = new Request.RequestBuilder(url, method)
        .body(body)
        .build();

    return Util.visionFetch(this.config.getConfig(newSettings), request);
  }

  public String retrieve(String sid) throws Exception {
    return retrieve(sid, this.config.getSettings());
  }

  public String retrieve(String sid, Settings newSettings) throws Exception {
    logger.info("KTP Sessions - Retrieve " + Util.json("sid", sid));

    String url = "ocr/:version/ktp-sessions/" + sid;
    String method = "GET";

    Request request = new Request.RequestBuilder(url, method).build();

    return Util.visionFetch(this.config.getConfig(newSettings), request);
  }

  public static class CreateParam {
    private final String successUrl;
    private final String cancelUrl;

    private CreateParam(Builder builder) {
      this.successUrl = builder.successUrl;
      this.cancelUrl = builder.cancelUrl;
    }

    @Override
    public String toString() {
      HashMap<String, String> map = new HashMap<>();
      map.put("successUrl", this.successUrl);
      map.put("cancelUrl", this.cancelUrl);

      return Util.json(map, 2);
    }

    public static class Builder {
      private String successUrl;
      private String cancelUrl;

      public Builder successUrl(String successUrl) {
        this.successUrl = successUrl;
        return this;
      }

      public Builder cancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
        return this;
      }

      public CreateParam build() throws Exception {
        Util.require("Success URL", this.successUrl);
        return new CreateParam(this);
      }
    }
  }
}
