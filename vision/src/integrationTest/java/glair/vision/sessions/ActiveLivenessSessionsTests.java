package glair.vision.sessions;

import com.fasterxml.jackson.databind.JsonNode;
import glair.vision.TestsCommon;
import glair.vision.api.sessions.ActiveLivenessSessions;
import glair.vision.model.VisionSettings;
import glair.vision.model.param.sessions.ActiveLivenessSessionsParam;
import glair.vision.model.param.sessions.BaseSessionsParam;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActiveLivenessSessionsTests {
  private final ActiveLivenessSessions sessions = TestsCommon.vision
      .faceBio()
      .activeLivenessSessions();

  private final String[] outerKeys = {"status", "success_url", "cancel_url", "url",
      "number_of_gestures"};
  private final String dataPtr = "/status";
  private final String[] dataKeys = {};

  @Test
  public void testConfiguration() {
    assertEquals(sessions.getSessionType(), "Active Liveness");
    assertEquals(sessions.getBaseUrl(), "face/:version/active-liveness-sessions");
  }

  @Test
  public void testCreate() {
    ActiveLivenessSessionsParam param = new ActiveLivenessSessionsParam(
        "https://docs.glair" + ".ai/vision" + "#success");

    BiFunction<Object, VisionSettings, String> fun = getFunction();

    JsonNode jsonNode = TestsCommon.testSuccessScenario(fun,
        param,
        outerKeys,
        dataPtr,
        dataKeys
    );

    assertEquals(jsonNode.get("number_of_gestures").asInt(), 1);
  }

  @Test
  public void testCreateCustomGestures() {
    ActiveLivenessSessionsParam param = new ActiveLivenessSessionsParam(
        "https://docs.glair.ai/vision#success");
    param.setNumberOfGestures(3);

    BiFunction<Object, VisionSettings, String> fun = getFunction();

    JsonNode jsonNode = TestsCommon.testSuccessScenario(fun,
        param,
        outerKeys,
        dataPtr,
        dataKeys
    );

    assertEquals(jsonNode.get("number_of_gestures").asInt(), 3);
  }

  private BiFunction<Object, VisionSettings, String> getFunction() {
    return (param, settings) -> {
      try {
        if (settings == null) {
          return sessions
              .getClass()
              .getMethod("create", BaseSessionsParam.class)
              .invoke(sessions, param)
              .toString();
        }
        return sessions
            .getClass()
            .getMethod("create", BaseSessionsParam.class, settings.getClass())
            .invoke(sessions, param, settings)
            .toString();
      } catch (InvocationTargetException e) {
        Throwable cause = e.getCause();
        throw new RuntimeException(cause);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    };
  }
}
