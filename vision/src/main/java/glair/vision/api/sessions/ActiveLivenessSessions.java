package glair.vision.api.sessions;

import glair.vision.api.Config;
import glair.vision.model.BaseSessions;
import glair.vision.model.param.sessions.ActiveLivenessSessionsParam;

import java.util.HashMap;

/**
 * Represents a session for Active Liveness operations.
 * Extends the {@link BaseSessions} class with specific configuration and session
 * details for Active Liveness sessions.
 */
public class ActiveLivenessSessions extends BaseSessions<ActiveLivenessSessionsParam> {
  /**
   * Constructs an ActiveLivenessSessions instance with the provided configuration.
   *
   * @param config The configuration settings to use for Active Liveness sessions.
   */
  public ActiveLivenessSessions(Config config) {
    super(config, "Active Liveness", "face/:version/active-liveness-sessions");
  }

  @Override
  protected HashMap<String, String> createBody(ActiveLivenessSessionsParam param) {
    HashMap<String, String> map = super.createBody(param);
    map.put("number_of_gestures", Integer.toString(param.getNumberOfGestures()));

    return map;
  }
}
