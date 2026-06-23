package glair.vision;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import glair.vision.enums.GestureCode;
import glair.vision.model.param.ActiveLivenessParam;
import glair.vision.model.param.FaceMatchParam;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FaceBio endpoints using MockWebServer.
 * No network required; no config.properties required.
 */
public class FaceBioUnitTests extends MockSdkTest {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  // ── match success ────────────────────────────────────────────────────────────

  @Test
  public void matchSuccess_returnsParsedResponse() throws Exception {
    String responseBody = "{"
        + "\"status\":200,"
        + "\"reason\":\"OK\","
        + "\"matched\":true,"
        + "\"similarity\":0.98"
        + "}";
    enqueueJson(200, responseBody);

    String result = vision.faceBio().match(
        new FaceMatchParam(sampleImagePath, sampleImagePath)
    );

    JsonNode node = MAPPER.readTree(result);
    assertEquals(200, node.get("status").asInt());
    assertTrue(node.get("matched").asBoolean());
  }

  @Test
  public void matchSuccess_requestHasCorrectPathAndHeaders() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"matched\":true}");

    vision.faceBio().match(new FaceMatchParam(sampleImagePath, sampleImagePath));

    RecordedRequest req = takeRequest();
    // FaceBio.fetch builds path: "face/:version/match" → "face/v1/match"
    assertTrue(req.getPath().endsWith("/face/v1/match"),
        "Expected path ending in /face/v1/match but got: " + req.getPath());
    assertEquals("k", req.getHeader("x-api-key"));
    assertNotNull(req.getHeader("Authorization"));
    assertTrue(req.getHeader("Authorization").startsWith("Basic "));
    assertEquals(Vision.version, req.getHeader("GLAIR-Vision-Java-SDK-Version"));
  }

  @Test
  public void matchSuccess_requestBodyIsJson() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"matched\":true}");

    vision.faceBio().match(new FaceMatchParam(sampleImagePath, sampleImagePath));

    RecordedRequest req = takeRequest();
    // FaceBio.match sends a JSON body with captured_image and stored_image (base64)
    assertNotNull(req.getHeader("Content-Type"));
    assertTrue(req.getHeader("Content-Type").contains("application/json"),
        "Content-Type should be application/json for match endpoint");
    String body = req.getBody().readUtf8();
    assertTrue(body.contains("captured_image"),
        "Request body should contain captured_image field");
    assertTrue(body.contains("stored_image"),
        "Request body should contain stored_image field");
  }

  // ── file-not-found ───────────────────────────────────────────────────────────

  @Test
  public void matchFileNotFound_throwsWithCorrectMessage() {
    Exception ex = assertThrows(Exception.class,
        () -> vision.faceBio().match(
            new FaceMatchParam("/nonexistent/captured.png", sampleImagePath)
        ));
    assertEquals("The file does not exist.", ex.getMessage());
  }

  // ── passiveLiveness success ──────────────────────────────────────────────────

  @Test
  public void passiveLiveness_success_returnsParsedResponse() throws Exception {
    String responseBody = "{"
        + "\"status\":200,"
        + "\"reason\":\"OK\","
        + "\"result\":\"REAL\""
        + "}";
    enqueueJson(200, responseBody);

    String result = vision.faceBio().passiveLiveness(sampleImagePath);

    JsonNode node = MAPPER.readTree(result);
    assertEquals(200, node.get("status").asInt());
    assertEquals("OK", node.get("reason").asText());
    assertEquals("REAL", node.get("result").asText());
  }

  @Test
  public void passiveLiveness_requestHasCorrectPathAndHeaders() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"result\":\"REAL\"}");

    vision.faceBio().passiveLiveness(sampleImagePath);

    RecordedRequest req = takeRequest();
    assertTrue(req.getPath().endsWith("/face/v1/passive-liveness"),
        "Expected path ending in /face/v1/passive-liveness but got: " + req.getPath());
    assertEquals("k", req.getHeader("x-api-key"));
    assertNotNull(req.getHeader("Authorization"));
    assertTrue(req.getHeader("Authorization").startsWith("Basic "),
        "Authorization header should start with 'Basic '");
    assertNotNull(req.getHeader("GLAIR-Vision-Java-SDK-Version"),
        "SDK-version header should be present");
  }

  @Test
  public void passiveLiveness_fileNotFound_throws() {
    Exception ex = assertThrows(Exception.class,
        () -> vision.faceBio().passiveLiveness("/nonexistent.png"));
    assertEquals("The file does not exist.", ex.getMessage());
  }

  // ── activeLiveness success ───────────────────────────────────────────────────

  @Test
  public void activeLiveness_success_returnsParsedResponse() throws Exception {
    String responseBody = "{"
        + "\"status\":200,"
        + "\"reason\":\"OK\","
        + "\"result\":\"REAL\""
        + "}";
    enqueueJson(200, responseBody);

    String result = vision.faceBio().activeLiveness(
        new ActiveLivenessParam(sampleImagePath, GestureCode.HAND_00000)
    );

    JsonNode node = MAPPER.readTree(result);
    assertEquals(200, node.get("status").asInt());
    assertEquals("OK", node.get("reason").asText());
    assertEquals("REAL", node.get("result").asText());
  }

  @Test
  public void activeLiveness_requestHasCorrectPathAndHeaders() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"result\":\"REAL\"}");

    vision.faceBio().activeLiveness(
        new ActiveLivenessParam(sampleImagePath, GestureCode.HAND_00000)
    );

    RecordedRequest req = takeRequest();
    assertTrue(req.getPath().endsWith("/face/v1/active-liveness"),
        "Expected path ending in /face/v1/active-liveness but got: " + req.getPath());
    assertEquals("k", req.getHeader("x-api-key"));
    assertNotNull(req.getHeader("Authorization"));
    assertTrue(req.getHeader("Authorization").startsWith("Basic "),
        "Authorization header should start with 'Basic '");
    assertNotNull(req.getHeader("GLAIR-Vision-Java-SDK-Version"),
        "SDK-version header should be present");
  }

  @Test
  public void activeLiveness_requestBodyContainsGestureCode() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"result\":\"REAL\"}");

    vision.faceBio().activeLiveness(
        new ActiveLivenessParam(sampleImagePath, GestureCode.HAND_00000)
    );

    RecordedRequest req = takeRequest();
    String rawBody = req.getBody().readUtf8();
    assertTrue(rawBody.contains("gesture-code"),
        "Multipart body should contain 'gesture-code' field name");
    assertTrue(rawBody.contains("HAND_00000"),
        "Multipart body should contain gesture label 'HAND_00000'");
  }

  @Test
  public void activeLiveness_fileNotFound_throws() {
    Exception ex = assertThrows(Exception.class,
        () -> vision.faceBio().activeLiveness(
            new ActiveLivenessParam("/nonexistent.png", GestureCode.HAND_00000)
        ));
    assertEquals("The file does not exist.", ex.getMessage());
  }

  // ── ActiveLivenessParam unit ──────────────────────────────────────────────────

  @Test
  public void activeLivenessParam_gettersReturnCorrectValues() {
    ActiveLivenessParam param = new ActiveLivenessParam("img.png", GestureCode.HEAD_LEFT);
    assertEquals("img.png", param.getImagePath());
    assertEquals(GestureCode.HEAD_LEFT, param.getGestureCode());
    assertNotNull(param.toString());
  }

  // ── GestureCode enum ──────────────────────────────────────────────────────────

  @Test
  public void gestureCode_allValuesHaveMatchingLabels() {
    for (GestureCode gc : GestureCode.values()) {
      assertEquals(gc.name(), gc.label,
          "Label for " + gc.name() + " should match its enum constant name");
    }
  }
}
