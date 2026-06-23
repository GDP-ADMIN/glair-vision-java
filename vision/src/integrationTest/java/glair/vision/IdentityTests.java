package glair.vision;

import glair.vision.api.Identity;
import glair.vision.model.VisionSettings;
import glair.vision.model.param.IdentityFaceVerificationParam;
import glair.vision.model.param.IdentityVerificationParam;
import glair.vision.util.Env;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;

public class IdentityTests {
  private final Env env = TestsCommon.env;
  private final Identity identity = TestsCommon.vision.identity();

  @Test
  public void testBasicVerification() throws Exception {
    String[] resultKeys = {"nik", "name", "date_of_birth"};

    String[] basicData = env.getIdentityBasicVerification().split(":");
    IdentityVerificationParam param = new IdentityVerificationParam.Builder()
        .nik(basicData[0])
        .name(basicData[1])
        .dateOfBirth(basicData[2])
        .build();

    testWithScenarios("verification", param, resultKeys);
  }

  @Test
  public void testFaceVerification() throws Exception {
    String funName = "faceVerification";
    String[] resultKeys = {"nik", "name", "date_of_birth", "face_image_percentage"};

    String[] basicData = env.getIdentityBasicVerification().split(":");
    String faceImagePath = env.getIdentityFaceVerification();

    IdentityFaceVerificationParam param = new IdentityFaceVerificationParam.Builder()
        .nik(basicData[0])
        .name(basicData[1])
        .dateOfBirth(basicData[2])
        .faceImagePath(faceImagePath)
        .build();
    IdentityFaceVerificationParam invalidFileParam =
        new IdentityFaceVerificationParam.Builder()
        .nik(basicData[0])
        .name(basicData[1])
        .dateOfBirth(basicData[2])
        .faceImagePath(faceImagePath + "abc")
        .build();

    BiFunction<Object, VisionSettings, String> fun = getFunction(funName);
    testWithScenarios(funName, param, resultKeys);
    TestsCommon.testFileNotFoundScenario(fun, invalidFileParam);
  }

  private void testWithScenarios(
      String methodName, Object param, String[] resultKeys
  ) {
    BiFunction<Object, VisionSettings, String> fun = getFunction(methodName);
    String[] outerKeys = {"verification_status", "reason"};

    TestsCommon.testSuccessScenario(fun, param, outerKeys, "/result", resultKeys);
    TestsCommon.testInvalidCredentialScenario(fun, param);
  }

  private BiFunction<Object, VisionSettings, String> getFunction(String methodName) {
    return (param, settings) -> {
      try {
        if (settings == null) {
          return identity
              .getClass()
              .getMethod(methodName, param.getClass())
              .invoke(identity, param)
              .toString();
        }
        return identity
            .getClass()
            .getMethod(methodName, param.getClass(), settings.getClass())
            .invoke(identity, param, settings)
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
