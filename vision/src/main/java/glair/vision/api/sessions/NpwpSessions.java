package glair.vision.api.sessions;

import glair.vision.api.Config;
import glair.vision.model.BaseSessions;
import glair.vision.model.param.sessions.BaseSessionsParam;

public class NpwpSessions extends BaseSessions<BaseSessionsParam> {
  /**
   * Represents a session for NPWP operations.
   * Extends the {@link BaseSessions} class with specific configuration and session details.
   *
   * @param config The configuration settings to use for NPWP sessions.
   */
  public NpwpSessions(Config config) {
    super(config, "NPWP", "ocr/:version/npwp-sessions");
  }
}
