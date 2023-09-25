package glair.vision.model;

import glair.vision.api.Config;
import glair.vision.logger.Logger;
import glair.vision.model.param.sessions.BaseSessionsParam;
import glair.vision.util.Json;
import glair.vision.util.Util;
import org.apache.http.HttpEntity;

import java.util.HashMap;

public class BaseSessions<T extends BaseSessionsParam> {
  protected static final Logger logger = Logger.getInstance();
  protected final Config config;
  protected final String sessionType;
  protected final String baseUrl;

  /**
   * Constructs a SessionsBase instance with the specified configuration, session type,
   * and base URL.
   *
   * @param config      The configuration settings to use for session operations.
   * @param sessionType The type of the session, e.g., "KTP"
   * @param baseUrl     The base URL for the session operations, e.g.,
   *                    "ocr/:version/ktp-sessions"
   */
  protected BaseSessions(Config config, String sessionType, String baseUrl) {
    this.config = config;
    this.sessionType = sessionType;
    this.baseUrl = baseUrl;
  }

  public String create(T param) throws Exception {
    return create(param, null);
  }

  public String create(T param, VisionSettings newVisionSettings) throws Exception {
    log("Create", param);

    HttpEntity body = Util.createJsonBody(createBody(param));
    Request request = new Request.RequestBuilder(baseUrl, "POST").body(body).build();

    return fetch(request, newVisionSettings);
  }

  public String retrieve(String sid) throws Exception {
    return retrieve(sid, null);
  }

  public String retrieve(String sid, VisionSettings newVisionSettings) throws Exception {
    log("Retrieve", Json.toJsonString("sid", sid));

    String url = baseUrl + "/" + sid;
    Request request = new Request.RequestBuilder(url, "GET").build();

    return fetch(request, newVisionSettings);
  }

  protected HashMap<String, String> createBody(T param) {
    HashMap<String, String> map = new HashMap<>();
    map.put("success_url", param.getSuccessUrl());

    if (param.getCancelUrl() != null) {
      map.put("cancel_url", param.getCancelUrl());
    }

    return map;
  }

  private String fetch(Request request, VisionSettings visionSettings) throws Exception {
    VisionSettings settingsToUse = (visionSettings == null) ?
        this.config.getSettings() : visionSettings;

    return Util.visionFetch(this.config.getConfig(settingsToUse), request);
  }

  private void log(String type, Object param) {
    logger.info(sessionType, "Sessions -", type);
    logger.debug("Param", param);
  }
}
