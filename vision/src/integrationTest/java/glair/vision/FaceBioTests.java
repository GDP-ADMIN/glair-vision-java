package glair.vision;

import glair.vision.api.FaceBio;
import glair.vision.enums.GestureCode;
import glair.vision.model.VisionSettings;
import glair.vision.model.param.ActiveLivenessParam;
import glair.vision.model.param.FaceMatchParam;
import glair.vision.util.Env;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;

public class FaceBioTests {
  private final Env env = TestsCommon.env;
  private final FaceBio faceBio = TestsCommon.vision.faceBio();

  private final String[] outerKeys = {"status"};

  @Test
  public void testMatch() {
    String funName = "match";
    String[] resultKeys = {"match_percentage", "match_status", "match_status_code"};
    BiFunction<Object, VisionSettings, String> fun = getFunction(funName);

    String capturedImagePath = env.getFace();
    String storedImagePath = env.getFace();
    FaceMatchParam param = new FaceMatchParam(capturedImagePath, storedImagePath);
    FaceMatchParam invalidCapturedParam = new FaceMatchParam(capturedImagePath + "abc",
        storedImagePath
    );
    FaceMatchParam invalidStoredParam = new FaceMatchParam(capturedImagePath,
        storedImagePath + "abc"
    );

    testWithScenarios(funName, param, resultKeys);
    TestsCommon.testFileNotFoundScenario(fun, invalidCapturedParam);
    TestsCommon.testFileNotFoundScenario(fun, invalidStoredParam);
  }

  @Test
  public void testPassiveLiveness() {
    String funName = "passiveLiveness";
    String[] resultKeys = {"spoof_percentage", "status"};

    String param = env.getFace();
    String invalidParam = param + "abc";

    testWithScenarios(funName, param, resultKeys);
    TestsCommon.testFileNotFoundScenario(getFunction(funName), invalidParam);
  }

  @Test
  public void testActiveLiveness() {
    String funName = "activeLiveness";
    String[] resultKeys = {"gesture_status", "detected_gesture"};

    String imagePath = env.getFace();
    ActiveLivenessParam param = new ActiveLivenessParam(imagePath, GestureCode.HEAD_11);
    ActiveLivenessParam invalidParam = new ActiveLivenessParam(imagePath + "abc",
        GestureCode.HEAD_11
    );

    testWithScenarios(funName, param, resultKeys);
    TestsCommon.testFileNotFoundScenario(getFunction(funName), invalidParam);
  }

  private void testWithScenarios(
      String methodName, Object param, String[] resultKeys
  ) {
    BiFunction<Object, VisionSettings, String> function = getFunction(methodName);
    String dataPtr = "/result";

    TestsCommon.testSuccessScenario(function, param, outerKeys, dataPtr, resultKeys);
    TestsCommon.testInvalidCredentialScenario(function, param);
  }

  private BiFunction<Object, VisionSettings, String> getFunction(String methodName) {
    return (param, settings) -> {
      try {
        if (settings == null) {
          return faceBio
              .getClass()
              .getMethod(methodName, param.getClass())
              .invoke(faceBio, param)
              .toString();
        }
        return faceBio
            .getClass()
            .getMethod(methodName, param.getClass(), settings.getClass())
            .invoke(faceBio, param, settings)
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
