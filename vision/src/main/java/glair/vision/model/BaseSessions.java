package glair.vision.model;

import glair.vision.api.Config;
import glair.vision.logger.Logger;
import glair.vision.model.param.sessions.BaseSessionsParam;
import glair.vision.util.Json;
import glair.vision.util.Util;
import org.apache.http.HttpEntity;

import java.util.HashMap;

/**
 * The base class for sessions handling, providing common functionality and
 * configurations for different session types.
 *
 * @param <T> The type of session parameters extending {@link BaseSessionsParam}.
 */
public class BaseSessions<T extends BaseSessionsParam> {
  /**
   * The logger instance used for logging within this class and its subclasses.
   */
  protected static final Logger logger = Logger.getInstance();
  /**
   * The configuration settings used for session operations.
   */
  protected final Config config;
  /**
   * The type of the session, e.g., "KTP".
   */
  protected final String sessionType;
  /**
   * The base URL for the session operations, e.g., "ocr/:version/ktp-sessions".
   */
  protected final String baseUrl;

  /**
   * Constructs a BaseSessions instance with the specified configuration, session type,
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

  /**
   * Creates a session with the provided parameters using default configuration settings.
   *
   * @param param The session parameters.
   * @return The result of the session creation.
   * @throws Exception If an error occurs during the session creation.
   */
  public String create(T param) throws Exception {
    return create(param, null);
  }

  /**
   * Creates a session with the provided parameters using custom configuration settings.
   *
   * @param param             The session parameters.
   * @param newVisionSettings The custom vision settings to use.
   * @return The result of the session creation.
   * @throws Exception If an error occurs during the session creation.
   */
  public String create(T param, VisionSettings newVisionSettings) throws Exception {
    log("Create", param);

    HttpEntity body = Util.createJsonBody(createBody(param));
    Request request = new Request.RequestBuilder(baseUrl, "POST").body(body).build();

    return fetch(request, newVisionSettings);
  }

  /**
   * Retrieves a session with the specified session ID using default configuration
   * settings.
   *
   * @param sid The session ID to retrieve.
   * @return The result of the session retrieval.
   * @throws Exception If an error occurs during the session retrieval.
   */
  public String retrieve(String sid) throws Exception {
    return retrieve(sid, null);
  }

  /**
   * Retrieves a session with the specified session ID using custom configuration
   * settings.
   *
   * @param sid               The session ID to retrieve.
   * @param newVisionSettings The custom vision settings to use.
   * @return The result of the session retrieval.
   * @throws Exception If an error occurs during the session retrieval.
   */
  public String retrieve(String sid, VisionSettings newVisionSettings) throws Exception {
    log("Retrieve", Json.toJsonString("sid", sid));

    String url = baseUrl + "/" + sid;
    Request request = new Request.RequestBuilder(url, "GET").build();

    return fetch(request, newVisionSettings);
  }

  /**
   * Creates the body of the session request based on the provided parameters.
   *
   * @param param The session parameters.
   * @return A map representing the session request body.
   */
  protected HashMap<String, String> createBody(T param) {
    HashMap<String, String> map = new HashMap<>();
    map.put("success_url", param.getSuccessUrl());

    if (param.getCancelUrl() != null) {
      map.put("cancel_url", param.getCancelUrl());
    }

    return map;
  }

  /**
   * Fetches data using the specified request and vision settings.
   *
   * @param request        The request to fetch data.
   * @param visionSettings The vision settings to use.
   * @return The fetched data.
   * @throws Exception If an error occurs during the data fetch.
   */
  private String fetch(Request request, VisionSettings visionSettings) throws Exception {
    VisionSettings settingsToUse = (visionSettings == null) ?
        this.config.getSettings() : visionSettings;

    return Util.visionFetch(this.config.getConfig(settingsToUse), request);
  }

  /**
   * Logs information about a session operation.
   *
   * @param type  The type of the session operation, e.g., "Create" or "Retrieve".
   * @param param The parameter to log.
   */
  private void log(String type, Object param) {
    logger.info(sessionType, "Sessions -", type);
    logger.debug("Param", param);
  }
}
