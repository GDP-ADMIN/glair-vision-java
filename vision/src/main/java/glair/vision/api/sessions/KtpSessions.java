package glair.vision.api.sessions;

import glair.vision.api.Config;
import glair.vision.model.BaseSessions;
import glair.vision.model.param.sessions.KtpSessionsParam;

import java.util.HashMap;

/**
 * Represents a session for KTP (Kartu Tanda Penduduk) operations.
 * Extends the {@link BaseSessions} class with specific configuration and session details for KTP sessions.
 */
public class KtpSessions extends BaseSessions<KtpSessionsParam> {
  /**
   * Constructs a KtpSessions instance with the provided configuration.
   *
   * @param config The configuration settings to use for KTP sessions.
   */
  public KtpSessions(Config config) {
    super(config, "KTP", "ocr/:version/ktp-sessions");
  }

  @Override
  protected HashMap<String, String> createBody(KtpSessionsParam param) {
    HashMap<String, String> map = super.createBody(param);
    map.put("qualities_detector", String.valueOf(param.getQualitiesDetector()));

    return map;
  }
}
