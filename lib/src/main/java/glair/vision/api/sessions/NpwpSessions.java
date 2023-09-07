package glair.vision.api.sessions;

import glair.vision.api.Config;
import glair.vision.model.SessionsBase;
import glair.vision.model.param.sessions.BasicSessionsParam;

public class NpwpSessions extends SessionsBase<BasicSessionsParam> {
  /**
   * Represents a session for NPWP operations.
   * Extends the {@link SessionsBase} class with specific configuration and session details.
   *
   * @param config The configuration settings to use for NPWP sessions.
   */
  public NpwpSessions(Config config) {
    super(config, "NPWP", "ocr/:version/npwp-sessions");
  }
}
