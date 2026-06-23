package glair.vision;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import glair.vision.logger.LoggerConfig;
import glair.vision.model.VisionSettings;
import glair.vision.result.VisionException;
import glair.vision.util.Env;
import glair.vision.util.Util;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TestsCommon {
  public static final String EXPECT_EXCEPTION = "Expected an Exception, but no " +
      "exception was thrown.";
  public static final String NO_EXCEPTION = "Exception was thrown unexpectedly.";

  public static final Env env;

  static {
    try {
      env = new Env();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static final VisionSettings visionSettings = new VisionSettings.Builder()
      .username(env.getUsername())
      .password(env.getPassword())
      .apiKey(env.getApiKey())
      .build();
  private static final VisionSettings invalidVisionSettings = new VisionSettings.Builder()
      .username("testUsername")
      .apiKey("testApiKey")
      .build();

  public static final Vision vision = new Vision(visionSettings,
      new LoggerConfig(LoggerConfig.DEBUG)
  );

  public static JsonNode testSuccessScenario(
      BiFunction<Object, VisionSettings, String> function,
      Object param,
      Consumer<JsonNode> assertStatusAndReason,
      Consumer<JsonNode> assertFieldsMethod
  ) {
    try {
      String response = function.apply(param, null);
      JsonNode jsonNode = parseResponseToJson(response);

      // TODO comment/delete
      System.out.println(jsonNode);

      assertStatusAndReason.accept(jsonNode);
      assertFieldsMethod.accept(jsonNode);
      System.out.println("Success Passed.");
      return jsonNode;
    } catch (Exception e) {
      fail(NO_EXCEPTION, e);
      return null;
    }
  }

  public static JsonNode testSuccessScenario(
      BiFunction<Object, VisionSettings, String> function,
      Object param,
      String[] outerKeys,
      String dataPtr,
      String[] dataKeys
  ) {
    CustomAssert customAssert = new CustomAssert(outerKeys, dataPtr, dataKeys);

    return testSuccessScenario(function,
        param,
        customAssert::assertOuter,
        customAssert::assertInner
    );
  }

  public static void testInvalidCredentialScenario(
      BiFunction<Object, VisionSettings, String> function, Object param
  ) {
    try {
      function.apply(param, invalidVisionSettings);
      fail(EXPECT_EXCEPTION);
    } catch (Exception e) {
      String completeMessage = e.getMessage();
      String[] splitMessage = completeMessage.split("Exception:");
      String response = splitMessage[splitMessage.length - 1];

      assert Util.trimAll(response).equalsIgnoreCase(VisionException.INVALID_USER());
      System.out.println("Invalid Credential Passed.");
    }
  }

  public static void testFileNotFoundScenario(
      BiFunction<Object, VisionSettings, String> function, Object param
  ) {
    try {
      function.apply(param, null);
      fail(EXPECT_EXCEPTION);
    } catch (RuntimeException e) {
      Exception originalException = (Exception) e.getCause();
      assert originalException.getMessage().equalsIgnoreCase("The file does not exist.");
      System.out.println("File Not Found Passed.");
    }
  }

  public static JsonNode parseResponseToJson(String response) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readTree(response);
  }

  public static boolean checkAllKeyExist(JsonNode jsonNode, String[] keysToCheck) {
    boolean allKeysExist = true;
    for (String key : keysToCheck) {
      if (!jsonNode.has(key)) {
        allKeysExist = false;
        break;
      }
    }

    return allKeysExist;
  }
}

class CustomAssert {
  private final String[] outerKeys;
  private final String dataPtr;
  private final String[] dataKeys;

  CustomAssert(String[] outerKeys, String dataPtr, String[] dataKeys) {
    this.outerKeys = outerKeys;
    this.dataPtr = dataPtr;
    this.dataKeys = dataKeys;
  }

  void assertOuter(JsonNode jsonNode) {
    assertTrue(TestsCommon.checkAllKeyExist(jsonNode, outerKeys));
  }

  void assertInner(JsonNode jsonNode) {
    assertTrue(TestsCommon.checkAllKeyExist(jsonNode.at(dataPtr), dataKeys));
  }
}
