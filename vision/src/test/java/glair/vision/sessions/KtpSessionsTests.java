package glair.vision.sessions;

import com.fasterxml.jackson.databind.JsonNode;
import glair.vision.TestsCommon;
import glair.vision.api.sessions.KtpSessions;
import glair.vision.model.VisionSettings;
import glair.vision.model.param.sessions.BaseSessionsParam;
import glair.vision.model.param.sessions.KtpSessionsParam;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KtpSessionsTests {
  private final KtpSessions sessions = TestsCommon.vision.ocr().ktpSessions();
  private final String[] outerKeys = {"status", "success_url", "cancel_url", "url",
      "qualities_detector"};
  private final String dataPtr = "/status";
  private final String[] dataKeys = {};

  @Test
  public void testConfiguration() {
    assertEquals(sessions.getSessionType(), "KTP");
    assertEquals(sessions.getBaseUrl(), "ocr/:version/ktp-sessions");
  }

  @Test
  public void testCreate() {
    KtpSessionsParam param = new KtpSessionsParam("https://docs.glair.ai/vision" +
        "#success");

    BiFunction<Object, VisionSettings, String> fun = getFunction();

    JsonNode jsonNode = TestsCommon.testSuccessScenario(fun,
        param,
        outerKeys,
        dataPtr,
        dataKeys
    );

    assertFalse(jsonNode.get("qualities_detector").asBoolean());
  }

  @Test
  public void testCreateQualities() {
    KtpSessionsParam param =
        new KtpSessionsParam("https://docs.glair" + ".ai/vision" + "#success");
    param.setQualitiesDetector(true);

    BiFunction<Object, VisionSettings, String> fun = getFunction();

    JsonNode jsonNode = TestsCommon.testSuccessScenario(fun,
        param,
        outerKeys,
        dataPtr,
        dataKeys
    );

    assertTrue(jsonNode.get("qualities_detector").asBoolean());
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
