package glair.vision.model.param.sessions;

import glair.vision.util.Json;

import java.util.HashMap;

/**
 * Represents the basic parameters for a session.
 * This class includes success and cancel URLs that can be set for the session.
 */
public class BasicSessionsParam {
  private final String successUrl;
  private String cancelUrl;

  /**
   * Constructs a BasicSessionsParam instance with the specified success URL.
   *
   * @param successUrl The URL to redirect to upon successful session completion.
   */
  public BasicSessionsParam(String successUrl) {
    this.successUrl = successUrl;
  }

  /**
   * Gets the success URL for the session.
   *
   * @return The success URL.
   */
  public String getSuccessUrl() {
    return successUrl;
  }

  /**
   * Gets the cancel URL for the session.
   *
   * @return The cancel URL. If set, GLAIR will show a back button on the prebuilt-UI, and your user will be directed to this URL when the button is clicked.
   */
  public String getCancelUrl() {
    return cancelUrl;
  }

  /**
   * Sets the cancel URL for the session.
   *
   * @param cancelUrl The URL to redirect to if the session is canceled. If set, GLAIR will show a back button on the prebuilt-UI, and your user will be directed to this URL when the button is clicked.
   */
  public void setCancelUrl(String cancelUrl) {
    this.cancelUrl = cancelUrl;
  }

  /**
   * Returns a JSON representation of the session parameters.
   *
   * @return A JSON string representing the session parameters.
   */

  @Override
  public String toString() {
    HashMap<String, String> map = new HashMap<>();
    map.put("Success Url", this.successUrl);
    map.put("Cancel Url", this.cancelUrl);

    return Json.toJsonString(map, 2);
  }
}
