package glair.vision;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import glair.vision.model.param.IdentityFaceVerificationParam;
import glair.vision.model.param.IdentityVerificationParam;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Identity endpoints using MockWebServer.
 * No network required; no config.properties required.
 */
public class IdentityUnitTests extends MockSdkTest {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  private IdentityVerificationParam buildParam() throws Exception {
    return new IdentityVerificationParam.Builder()
        .nik("1234567890123456")
        .name("Test Name")
        .dateOfBirth("01-01-1990")
        .build();
  }

  // ── verification success ─────────────────────────────────────────────────────

  @Test
  public void verificationSuccess_returnsParsedResponse() throws Exception {
    String responseBody = "{"
        + "\"status\":200,"
        + "\"reason\":\"OK\","
        + "\"verificationStatus\":\"VERIFIED\""
        + "}";
    enqueueJson(200, responseBody);

    String result = vision.identity().verification(buildParam());

    JsonNode node = MAPPER.readTree(result);
    assertEquals(200, node.get("status").asInt());
    assertEquals("OK", node.get("reason").asText());
    assertEquals("VERIFIED", node.get("verificationStatus").asText());
  }

  @Test
  public void verificationSuccess_requestHasCorrectPathAndHeaders() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"verificationStatus\":\"VERIFIED\"}");

    vision.identity().verification(buildParam());

    RecordedRequest req = takeRequest();
    // Identity.fetch builds path: "identity/:version/verification" → "identity/v1/verification"
    assertTrue(req.getPath().endsWith("/identity/v1/verification"),
        "Expected path ending in /identity/v1/verification but got: " + req.getPath());
    assertEquals("k", req.getHeader("x-api-key"));
    assertNotNull(req.getHeader("Authorization"));
    assertTrue(req.getHeader("Authorization").startsWith("Basic "));
    assertEquals(Vision.version, req.getHeader("GLAIR-Vision-Java-SDK-Version"));
  }

  @Test
  public void verificationSuccess_requestBodyContainsFields() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"verificationStatus\":\"VERIFIED\"}");

    vision.identity().verification(buildParam());

    RecordedRequest req = takeRequest();
    String body = req.getBody().readUtf8();
    // Multipart form-data body should include the param values
    assertTrue(body.contains("1234567890123456"), "Body should contain nik value");
    assertTrue(body.contains("Test Name"), "Body should contain name value");
    assertTrue(body.contains("01-01-1990"), "Body should contain dateOfBirth value");
  }

  // ── error propagation ────────────────────────────────────────────────────────

  @Test
  public void verificationError_exceptionMessageContainsBody() {
    String errorBody = "{\"error\":\"NIK not found\"}";
    enqueueJson(404, errorBody);

    Exception ex = assertThrows(Exception.class,
        () -> vision.identity().verification(buildParam()));
    assertTrue(ex.getMessage().contains("NIK not found"),
        "Exception message should contain error body, got: " + ex.getMessage());
  }

  // ── faceVerification helpers ─────────────────────────────────────────────────

  private IdentityFaceVerificationParam buildFaceParam() throws Exception {
    return new IdentityFaceVerificationParam.Builder()
        .nik("1234567890123456")
        .name("Test Name")
        .dateOfBirth("01-01-1990")
        .faceImagePath(sampleImagePath)
        .build();
  }

  private static final String FACE_VERIFICATION_RESPONSE = "{"
      + "\"status\":200,"
      + "\"reason\":\"OK\","
      + "\"verificationStatus\":\"VERIFIED\""
      + "}";

  // ── faceVerification success ─────────────────────────────────────────────────

  @Test
  public void faceVerification_success_returnsParsedResponse() throws Exception {
    enqueueJson(200, FACE_VERIFICATION_RESPONSE);

    String result = vision.identity().faceVerification(buildFaceParam());

    JsonNode node = MAPPER.readTree(result);
    assertEquals(200, node.get("status").asInt());
    assertEquals("VERIFIED", node.get("verificationStatus").asText());
  }

  @Test
  public void faceVerification_requestHasCorrectPath() throws Exception {
    enqueueJson(200, FACE_VERIFICATION_RESPONSE);

    vision.identity().faceVerification(buildFaceParam());

    RecordedRequest req = takeRequest();
    assertTrue(req.getPath().endsWith("/identity/v1/face-verification"),
        "Expected path ending in /identity/v1/face-verification but got: " + req.getPath());
    assertEquals("k", req.getHeader("x-api-key"));
    assertNotNull(req.getHeader("Authorization"));
    assertTrue(req.getHeader("Authorization").startsWith("Basic "));
    assertNotNull(req.getHeader("GLAIR-Vision-Java-SDK-Version"));
  }

  @Test
  public void faceVerification_requestBodyContainsFields() throws Exception {
    enqueueJson(200, FACE_VERIFICATION_RESPONSE);

    vision.identity().faceVerification(buildFaceParam());

    RecordedRequest req = takeRequest();
    String body = req.getBody().readUtf8();
    assertTrue(body.contains("1234567890123456"), "Body should contain nik value");
    assertTrue(body.contains("Test Name"), "Body should contain name value");
    assertTrue(body.contains("01-01-1990"), "Body should contain dateOfBirth value");
  }

  @Test
  public void faceVerification_fileNotFound_throws() {
    Exception ex = assertThrows(Exception.class, () -> {
      IdentityFaceVerificationParam param = new IdentityFaceVerificationParam.Builder()
          .nik("1234567890123456")
          .name("Test Name")
          .dateOfBirth("01-01-1990")
          .faceImagePath("/nonexistent.png")
          .build();
      vision.identity().faceVerification(param);
    });
    assertEquals("The file does not exist.", ex.getMessage());
  }

  @Test
  public void identityFaceVerificationParam_builderRequiresAllFields() {
    // faceImagePath omitted → build() throws
    assertThrows(Exception.class, () ->
        new IdentityFaceVerificationParam.Builder()
            .nik("x")
            .name("y")
            .dateOfBirth("z")
            .build()
    );
    // all fields omitted → build() throws
    assertThrows(Exception.class, () ->
        new IdentityFaceVerificationParam.Builder().build()
    );
  }
}
