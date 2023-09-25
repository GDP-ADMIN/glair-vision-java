package glair.vision;

import com.fasterxml.jackson.databind.JsonNode;
import glair.vision.api.Identity;
import glair.vision.logger.LoggerConfig;
import glair.vision.model.VisionSettings;
import glair.vision.model.param.IdentityFaceVerificationParam;
import glair.vision.model.param.IdentityVerificationParam;
import glair.vision.util.Env;
import glair.vision.util.Json;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IdentityTests {
  private final Env env = new Env();
  private final VisionSettings visionSettings = new VisionSettings.Builder()
      .username(env.getUsername())
      .password(env.getPassword())
      .apiKey(env.getApiKey())
      .build();
  private final Identity identity = (new Vision(visionSettings,
      new LoggerConfig(LoggerConfig.DEBUG))).identity();

  public IdentityTests() throws Exception {}

//  @Test
  public void testBasicVerification() throws Exception {
    String[] basicData = env.getIdentityBasicVerification().split(":");

    IdentityVerificationParam param = new IdentityVerificationParam.Builder()
        .nik(basicData[0])
        .name(basicData[1])
        .dateOfBirth(basicData[2])
        .build();

    testWithScenarios("verification", param, this::assertBasicVerificationFields);
  }

  private void assertBasicVerificationFields(JsonNode jsonNode) {
    String[] resultKeys = {"nik", "name", "date_of_birth"};
    assertTrue(Json.checkAllKeyExist(jsonNode.get("result"), resultKeys));
  }

//  @Test
  public void testFaceVerification() throws Exception {
    String[] basicData = env.getIdentityBasicVerification().split(":");
    String faceImagePath = env.getIdentityFaceVerification();

    IdentityFaceVerificationParam param = new IdentityFaceVerificationParam.Builder()
        .nik(basicData[0])
        .name(basicData[1])
        .dateOfBirth(basicData[2])
        .faceImagePath(faceImagePath)
        .build();

    testWithScenarios("faceVerification",
        param,
        this::assertFaceVerificationFields);

    IdentityFaceVerificationParam invalidFileParam =
        new IdentityFaceVerificationParam.Builder()
            .nik(basicData[0])
            .name(basicData[1])
            .dateOfBirth(basicData[2])
            .faceImagePath(faceImagePath + "abc")
            .build();

    TestsCommon.testFileNotFoundScenario(getFunction("faceVerification"),
        invalidFileParam);
  }

  private void assertFaceVerificationFields(JsonNode jsonNode) {
    String[] resultKeys = {"nik", "name", "date_of_birth", "face_image_percentage"};
    assertTrue(Json.checkAllKeyExist(jsonNode.get("result"), resultKeys));
  }

  private void assertStatusAndReason(JsonNode jsonNode) {
    assertTrue(jsonNode.has("verification_status") && jsonNode
        .get("verification_status")
        .isBoolean());
    assertTrue(jsonNode.has("reason") && jsonNode.get("reason").isTextual());
  }

  private void testWithScenarios(String methodName, Object param,
                                 Consumer<JsonNode> assertFieldsMethod) {
    BiFunction<Object, VisionSettings, String> function = getFunction(methodName);

    TestsCommon.testSuccessScenario(function,
        param,
        this::assertStatusAndReason,
        assertFieldsMethod);
    TestsCommon.testInvalidCredentialScenario(function, param);
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
