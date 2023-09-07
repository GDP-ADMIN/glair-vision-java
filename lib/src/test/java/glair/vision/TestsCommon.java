package glair.vision;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import glair.vision.model.VisionSettings;
import glair.vision.result.VisionException;
import glair.vision.util.Util;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.fail;

public class TestsCommon {
  private static final String EXPECT_EXCEPTION = "Expected an Exception, but no " +
      "exception was thrown.";
  private static final String NO_EXCEPTION = "Exception was thrown unexpectedly.";

  private static final VisionSettings invalidVisionSettings = new VisionSettings.Builder()
      .username("testUsername")
      .apiKey("testApiKey")
      .build();

  public static void testSuccessScenario(BiFunction<Object, VisionSettings, String> function, Object param, Consumer<JsonNode> assertStatusAndReason, Consumer<JsonNode> assertFieldsMethod) {
    try {
      String response = function.apply(param, null);
      JsonNode jsonNode = parseResponseToJson(response);

      //      System.out.println(jsonNode);

      assertStatusAndReason.accept(jsonNode);
      assertFieldsMethod.accept(jsonNode);
      System.out.println("Success Passed.");
    } catch (Exception e) {
      fail(NO_EXCEPTION, e);
    }
  }

  public static void testInvalidCredentialScenario(BiFunction<Object, VisionSettings,
      String> function, Object param) {
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

  public static void testFileNotFoundScenario(BiFunction<Object, VisionSettings,
      String> function, Object param) {
    try {
      function.apply(param, null);
      fail(EXPECT_EXCEPTION);
    } catch (RuntimeException e) {
      Exception originalException = (Exception) e.getCause();
      assert originalException.getMessage().equalsIgnoreCase("The file does not exist.");
      System.out.println("File Not Found Passed.");
    }
  }

  private static JsonNode parseResponseToJson(String response) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readTree(response);
  }
}
