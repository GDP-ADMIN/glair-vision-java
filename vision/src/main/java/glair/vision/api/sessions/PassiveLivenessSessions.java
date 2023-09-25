package glair.vision.api.sessions;

import glair.vision.api.Config;
import glair.vision.model.BaseSessions;
import glair.vision.model.param.sessions.BaseSessionsParam;

public class PassiveLivenessSessions extends BaseSessions<BaseSessionsParam> {
  /**
   * Represents a session for Passive Liveness operations.
   * Extends the {@link BaseSessions} class with specific configuration and session details.
   *
   * @param config The configuration settings to use for Passive Liveness sessions.
   */
  public PassiveLivenessSessions(Config config) {
    super(config, "Passive Liveness", "face/:version/passive-liveness-sessions");
  }
}