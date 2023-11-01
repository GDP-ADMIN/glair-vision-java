package glair.vision.sessions;

import com.fasterxml.jackson.databind.JsonNode;
import glair.vision.TestsCommon;
import glair.vision.api.sessions.NpwpSessions;
import glair.vision.model.VisionSettings;
import glair.vision.model.param.sessions.BaseSessionsParam;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;

// This is a test for Default Sessions Functionality
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BaseSessionsTests {
  private final NpwpSessions npwpSessions = TestsCommon.vision.ocr().npwpSessions();
  private static String sid;

  @Test
  @Order(1)
  public void testCreate() {
    BaseSessionsParam param = new BaseSessionsParam("https://docs.glair" +
        ".ai/vision#success");

    String[] outerKeys = {"status", "success_url", "cancel_url", "url"};
    String[] dataKeys = {};

    BiFunction<Object, VisionSettings, String> fun = getFunction("create");

    JsonNode jsonNode = TestsCommon.testSuccessScenario(fun,
        param,
        outerKeys,
        "/status",
        dataKeys
    );

    String[] splitUrl = jsonNode.get("url").asText().split("/");
    sid = splitUrl[splitUrl.length-1];
  }

  @Test
  @Order(2)
  public void testRetrieve() {

    String[] outerKeys = {"status", "url", "success_url", "cancel_url", "result"};
    String[] dataKeys = {};

    BiFunction<Object, VisionSettings, String> fun = getFunction("retrieve");
    TestsCommon.testSuccessScenario(fun, sid, outerKeys, "/status", dataKeys);
  }

  private BiFunction<Object, VisionSettings, String> getFunction(String methodName) {
    return (param, settings) -> {
      try {
        if (settings == null) {
          return npwpSessions
              .getClass()
              .getMethod(methodName, param.getClass())
              .invoke(npwpSessions, param)
              .toString();
        }
        return npwpSessions
            .getClass()
            .getMethod(methodName, param.getClass(), settings.getClass())
            .invoke(npwpSessions, param, settings)
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
