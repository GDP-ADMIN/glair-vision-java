package glair.vision.model.param.sessions;

import glair.vision.util.Json;

import java.util.HashMap;

/**
 * Represents the parameters for an Active Liveness session.
 * This class extends the {@link BaseSessionsParam} class to include the number of
 * gestures.
 */
public class ActiveLivenessSessionsParam extends BaseSessionsParam {
  private int numberOfGestures = 1;

  /**
   * Constructs an ActiveLivenessSessionsParam instance with the specified success URL.
   *
   * @param successUrl The URL to redirect to upon successful session completion.
   */
  public ActiveLivenessSessionsParam(String successUrl) {
    super(successUrl);
  }

  /**
   * Gets the number of gestures for the Active Liveness session.
   *
   * @return The number of gestures.
   */
  public int getNumberOfGestures() {
    return numberOfGestures;
  }

  /**
   * Sets the number of gestures for the Active Liveness session.
   *
   * @param numberOfGestures The number of gestures to set. If not set, the default
   *                         will be 1.
   */
  public void setNumberOfGestures(int numberOfGestures) {
    this.numberOfGestures = numberOfGestures;
  }

  /**
   * Returns a JSON representation of the Active Liveness session parameters.
   *
   * @return A JSON string representing the session parameters.
   */

  @Override
  public String toString() {
    HashMap<String, String> map = new HashMap<>();
    map.put("Success Url", super.getSuccessUrl());
    map.put("Cancel Url", super.getCancelUrl());
    map.put("Number of Gestures", Integer.toString(numberOfGestures));

    return Json.toJsonString(map, 2);
  }
}
