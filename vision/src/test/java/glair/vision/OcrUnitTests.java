package glair.vision;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import glair.vision.model.param.BpkbParam;
import glair.vision.model.param.KtpParam;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OCR endpoints using MockWebServer.
 * No network required; no config.properties required.
 */
public class OcrUnitTests extends MockSdkTest {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  // ── ktp success ─────────────────────────────────────────────────────────────

  @Test
  public void ktpSuccess_returnsParsedResponse() throws Exception {
    String responseBody = "{"
        + "\"status\":200,"
        + "\"reason\":\"OK\","
        + "\"read\":{"
        + "  \"nik\":\"1234567890123456\","
        + "  \"nama\":\"Test Name\","
        + "  \"provinsi\":\"DKI Jakarta\""
        + "}"
        + "}";
    enqueueJson(200, responseBody);

    String result = vision.ocr().ktp(new KtpParam(sampleImagePath));

    JsonNode node = MAPPER.readTree(result);
    assertEquals(200, node.get("status").asInt());
    assertEquals("OK", node.get("reason").asText());
    assertEquals("1234567890123456", node.at("/read/nik").asText());
    assertEquals("Test Name", node.at("/read/nama").asText());
  }

  @Test
  public void ktpSuccess_requestHasCorrectPathAndHeaders() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{}}");

    vision.ocr().ktp(new KtpParam(sampleImagePath));

    RecordedRequest req = takeRequest();
    // Config.getUrl builds: baseUrl + "/" + path-with-version-replaced
    // path = "ocr/:version/ktp" → "ocr/v1/ktp" → full = "<base>/ocr/v1/ktp"
    assertTrue(req.getPath().endsWith("/ocr/v1/ktp"),
        "Expected path ending in /ocr/v1/ktp but got: " + req.getPath());
    assertEquals("k", req.getHeader("x-api-key"));
    assertNotNull(req.getHeader("Authorization"),
        "Authorization header should be present");
    assertTrue(req.getHeader("Authorization").startsWith("Basic "),
        "Authorization should be Basic auth");
    assertEquals(Vision.version, req.getHeader("GLAIR-Vision-Java-SDK-Version"));
  }

  // ── ktp with qualitiesDetector=true ─────────────────────────────────────────

  @Test
  public void ktpQualitiesDetector_requestUsesQualitiesPath() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{},\"qualities\":{}}");

    vision.ocr().ktp(new KtpParam(sampleImagePath, true));

    RecordedRequest req = takeRequest();
    assertTrue(req.getPath().endsWith("/ocr/v1/ktp/qualities"),
        "Expected path ending in /ocr/v1/ktp/qualities but got: " + req.getPath());
  }

  // ── npwp success (single-image endpoint) ────────────────────────────────────

  @Test
  public void npwpSuccess_returnsParsedResponse() throws Exception {
    String responseBody = "{"
        + "\"status\":200,"
        + "\"reason\":\"OK\","
        + "\"read\":{"
        + "  \"nama\":\"Test Taxpayer\","
        + "  \"noNpwp\":\"12.345.678.9-012.000\""
        + "}"
        + "}";
    enqueueJson(200, responseBody);

    String result = vision.ocr().npwp(sampleImagePath);

    JsonNode node = MAPPER.readTree(result);
    assertEquals(200, node.get("status").asInt());
    assertEquals("Test Taxpayer", node.at("/read/nama").asText());
    assertTrue(takeRequest().getPath().endsWith("/ocr/v1/npwp"));
  }

  // ── error-body propagation ───────────────────────────────────────────────────

  @Test
  public void errorResponse_exceptionMessageContainsBody() {
    String errorBody = "{\"error\":\"Access to this API has been disallowed\"}";
    enqueueJson(400, errorBody);

    Exception ex = assertThrows(Exception.class,
        () -> vision.ocr().npwp(sampleImagePath));
    assertTrue(ex.getMessage().contains("error"),
        "Exception message should contain error body content, got: " + ex.getMessage());
  }

  // ── file-not-found ───────────────────────────────────────────────────────────

  @Test
  public void fileNotFound_throwsWithCorrectMessage() {
    Exception ex = assertThrows(Exception.class,
        () -> vision.ocr().npwp("/nonexistent/path/image.png"));
    assertEquals("The file does not exist.", ex.getMessage());
  }

  // ── bpkb without page ────────────────────────────────────────────────────────

  @Test
  public void bpkbWithoutPage_returnsParsedResponse() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{}}");

    String result = vision.ocr().bpkb(new BpkbParam(sampleImagePath));

    JsonNode node = MAPPER.readTree(result);
    assertEquals(200, node.get("status").asInt());
    assertEquals("OK", node.get("reason").asText());
  }

  @Test
  public void bpkbWithoutPage_requestHasCorrectPath() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{}}");

    vision.ocr().bpkb(new BpkbParam(sampleImagePath));

    RecordedRequest req = takeRequest();
    assertTrue(req.getPath().endsWith("/ocr/v1/bpkb"),
        "Expected path ending in /ocr/v1/bpkb but got: " + req.getPath());
  }

  // ── bpkb with page ───────────────────────────────────────────────────────────

  @Test
  public void bpkbWithPage_requestBodyContainsPage() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{}}");

    vision.ocr().bpkb(new BpkbParam(sampleImagePath, 2));

    RecordedRequest req = takeRequest();
    assertTrue(req.getPath().endsWith("/ocr/v1/bpkb"),
        "Expected path ending in /ocr/v1/bpkb but got: " + req.getPath());
    String body = req.getBody().readUtf8();
    assertTrue(body.contains("page"),
        "Request body should contain 'page' field but got: " + body);
    assertTrue(body.contains("2"),
        "Request body should contain page value '2' but got: " + body);
  }

  // ── kk ───────────────────────────────────────────────────────────────────────

  @Test
  public void kkSuccess_returnsParsedResponse() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{}}");

    String result = vision.ocr().kk(sampleImagePath);

    JsonNode node = MAPPER.readTree(result);
    assertEquals(200, node.get("status").asInt());
    assertEquals("OK", node.get("reason").asText());
  }

  @Test
  public void kkSuccess_requestHasCorrectPath() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{}}");

    vision.ocr().kk(sampleImagePath);

    RecordedRequest req = takeRequest();
    assertTrue(req.getPath().endsWith("/ocr/v1/kk"),
        "Expected path ending in /ocr/v1/kk but got: " + req.getPath());
  }

  // ── stnk ─────────────────────────────────────────────────────────────────────

  @Test
  public void stnkSuccess_returnsParsedResponse() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{}}");

    String result = vision.ocr().stnk(sampleImagePath);

    JsonNode node = MAPPER.readTree(result);
    assertEquals(200, node.get("status").asInt());
    assertEquals("OK", node.get("reason").asText());
  }

  @Test
  public void stnkSuccess_requestHasCorrectPath() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{}}");

    vision.ocr().stnk(sampleImagePath);

    RecordedRequest req = takeRequest();
    assertTrue(req.getPath().endsWith("/ocr/v1/stnk"),
        "Expected path ending in /ocr/v1/stnk but got: " + req.getPath());
  }

  // ── passport ─────────────────────────────────────────────────────────────────

  @Test
  public void passportSuccess_returnsParsedResponse() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{}}");

    String result = vision.ocr().passport(sampleImagePath);

    JsonNode node = MAPPER.readTree(result);
    assertEquals(200, node.get("status").asInt());
    assertEquals("OK", node.get("reason").asText());
  }

  @Test
  public void passportSuccess_requestHasCorrectPath() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{}}");

    vision.ocr().passport(sampleImagePath);

    RecordedRequest req = takeRequest();
    assertTrue(req.getPath().endsWith("/ocr/v1/passport"),
        "Expected path ending in /ocr/v1/passport but got: " + req.getPath());
  }

  // ── licensePlate ─────────────────────────────────────────────────────────────

  @Test
  public void licensePlateSuccess_returnsParsedResponse() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{}}");

    String result = vision.ocr().licensePlate(sampleImagePath);

    JsonNode node = MAPPER.readTree(result);
    assertEquals(200, node.get("status").asInt());
    assertEquals("OK", node.get("reason").asText());
  }

  @Test
  public void licensePlateSuccess_requestHasCorrectPath() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{}}");

    vision.ocr().licensePlate(sampleImagePath);

    RecordedRequest req = takeRequest();
    assertTrue(req.getPath().endsWith("/ocr/v1/plate"),
        "Expected path ending in /ocr/v1/plate but got: " + req.getPath());
  }

  // ── generalDocument ──────────────────────────────────────────────────────────

  @Test
  public void generalDocumentSuccess_returnsParsedResponse() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{}}");

    String result = vision.ocr().generalDocument(sampleImagePath);

    JsonNode node = MAPPER.readTree(result);
    assertEquals(200, node.get("status").asInt());
    assertEquals("OK", node.get("reason").asText());
  }

  @Test
  public void generalDocumentSuccess_requestHasCorrectPath() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{}}");

    vision.ocr().generalDocument(sampleImagePath);

    RecordedRequest req = takeRequest();
    assertTrue(req.getPath().endsWith("/ocr/v1/general-document"),
        "Expected path ending in /ocr/v1/general-document but got: " + req.getPath());
  }

  // ── invoice ──────────────────────────────────────────────────────────────────

  @Test
  public void invoiceSuccess_returnsParsedResponse() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{}}");

    String result = vision.ocr().invoice(sampleImagePath);

    JsonNode node = MAPPER.readTree(result);
    assertEquals(200, node.get("status").asInt());
    assertEquals("OK", node.get("reason").asText());
  }

  @Test
  public void invoiceSuccess_requestHasCorrectPath() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{}}");

    vision.ocr().invoice(sampleImagePath);

    RecordedRequest req = takeRequest();
    assertTrue(req.getPath().endsWith("/ocr/v1/invoice"),
        "Expected path ending in /ocr/v1/invoice but got: " + req.getPath());
  }

  // ── receipt ──────────────────────────────────────────────────────────────────

  @Test
  public void receiptSuccess_returnsParsedResponse() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{}}");

    String result = vision.ocr().receipt(sampleImagePath);

    JsonNode node = MAPPER.readTree(result);
    assertEquals(200, node.get("status").asInt());
    assertEquals("OK", node.get("reason").asText());
  }

  @Test
  public void receiptSuccess_requestHasCorrectPath() throws Exception {
    enqueueJson(200, "{\"status\":200,\"reason\":\"OK\",\"read\":{}}");

    vision.ocr().receipt(sampleImagePath);

    RecordedRequest req = takeRequest();
    assertTrue(req.getPath().endsWith("/ocr/v1/receipt"),
        "Expected path ending in /ocr/v1/receipt but got: " + req.getPath());
  }

  // ── BpkbParam builder (no server) ────────────────────────────────────────────

  @Test
  public void bpkbParam_defaultPageIsZero() {
    BpkbParam param = new BpkbParam("img.png");
    assertEquals("img.png", param.getImagePath());
    assertEquals(0, param.getPage());
    assertNotNull(param.toString());
  }

  @Test
  public void bpkbParam_customPageIsPreserved() {
    BpkbParam param = new BpkbParam("img.png", 3);
    assertEquals("img.png", param.getImagePath());
    assertEquals(3, param.getPage());
    assertNotNull(param.toString());
  }
}
