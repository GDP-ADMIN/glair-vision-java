package glair.vision;

import glair.vision.model.param.sessions.ActiveLivenessSessionsParam;
import glair.vision.model.param.sessions.BaseSessionsParam;
import glair.vision.model.param.sessions.KtpSessionsParam;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Sessions endpoints (KTP, NPWP, Active Liveness, Passive Liveness)
 * using MockWebServer. No network required; no config.properties required.
 */
public class SessionsUnitTests extends MockSdkTest {

  private static final String SESSION_RESPONSE = "{\"status\":200,\"sid\":\"abc123\"}";

  // ── KtpSessions ──────────────────────────────────────────────────────────────

  @Test
  public void ktpSessions_create_requestHasCorrectPath() throws Exception {
    KtpSessionsParam param = new KtpSessionsParam("https://success.url");
    enqueueJson(200, SESSION_RESPONSE);

    vision.ocr().ktpSessions().create(param);

    RecordedRequest req = takeRequest();
    assertTrue(req.getPath().endsWith("/ocr/v1/ktp-sessions"),
        "Expected path ending in /ocr/v1/ktp-sessions but got: " + req.getPath());
    assertEquals("POST", req.getMethod());
  }

  @Test
  public void ktpSessions_create_bodyContainsSuccessUrl() throws Exception {
    KtpSessionsParam param = new KtpSessionsParam("https://success.url");
    enqueueJson(200, SESSION_RESPONSE);

    vision.ocr().ktpSessions().create(param);

    RecordedRequest req = takeRequest();
    String body = req.getBody().readUtf8();
    assertTrue(body.contains("success_url"), "Body should contain 'success_url' key");
    assertTrue(body.contains("https://success.url"), "Body should contain the success URL value");
  }

  @Test
  public void ktpSessions_create_bodyContainsQualitiesDetector() throws Exception {
    KtpSessionsParam param = new KtpSessionsParam("https://success.url");
    param.setQualitiesDetector(true);
    enqueueJson(200, SESSION_RESPONSE);

    vision.ocr().ktpSessions().create(param);

    RecordedRequest req = takeRequest();
    String body = req.getBody().readUtf8();
    assertTrue(body.contains("qualities_detector"), "Body should contain 'qualities_detector' key");
    assertTrue(body.contains("true"), "Body should contain 'true' for qualities_detector");
  }

  @Test
  public void ktpSessions_retrieve_requestIsGet() throws Exception {
    enqueueJson(200, SESSION_RESPONSE);

    vision.ocr().ktpSessions().retrieve("abc123");

    RecordedRequest req = takeRequest();
    assertEquals("GET", req.getMethod());
    assertTrue(req.getPath().endsWith("/ocr/v1/ktp-sessions/abc123"),
        "Expected path ending in /ocr/v1/ktp-sessions/abc123 but got: " + req.getPath());
  }

  // ── NpwpSessions ─────────────────────────────────────────────────────────────

  @Test
  public void npwpSessions_create_requestHasCorrectPath() throws Exception {
    BaseSessionsParam param = new BaseSessionsParam("https://success.url");
    enqueueJson(200, SESSION_RESPONSE);

    vision.ocr().npwpSessions().create(param);

    RecordedRequest req = takeRequest();
    assertTrue(req.getPath().endsWith("/ocr/v1/npwp-sessions"),
        "Expected path ending in /ocr/v1/npwp-sessions but got: " + req.getPath());
    assertEquals("POST", req.getMethod());
  }

  @Test
  public void npwpSessions_retrieve_requestIsGet() throws Exception {
    enqueueJson(200, SESSION_RESPONSE);

    vision.ocr().npwpSessions().retrieve("abc123");

    RecordedRequest req = takeRequest();
    assertEquals("GET", req.getMethod());
    assertTrue(req.getPath().endsWith("/ocr/v1/npwp-sessions/abc123"),
        "Expected path ending in /ocr/v1/npwp-sessions/abc123 but got: " + req.getPath());
  }

  // ── ActiveLivenessSessions ────────────────────────────────────────────────────

  @Test
  public void activeLivenessSessions_create_requestHasCorrectPath() throws Exception {
    ActiveLivenessSessionsParam param = new ActiveLivenessSessionsParam("https://success.url");
    enqueueJson(200, SESSION_RESPONSE);

    vision.faceBio().activeLivenessSessions().create(param);

    RecordedRequest req = takeRequest();
    assertTrue(req.getPath().endsWith("/face/v1/active-liveness-sessions"),
        "Expected path ending in /face/v1/active-liveness-sessions but got: " + req.getPath());
    assertEquals("POST", req.getMethod());
  }

  @Test
  public void activeLivenessSessions_create_bodyContainsNumberOfGestures() throws Exception {
    ActiveLivenessSessionsParam param = new ActiveLivenessSessionsParam("https://success.url");
    param.setNumberOfGestures(3);
    enqueueJson(200, SESSION_RESPONSE);

    vision.faceBio().activeLivenessSessions().create(param);

    RecordedRequest req = takeRequest();
    String body = req.getBody().readUtf8();
    assertTrue(body.contains("number_of_gestures"), "Body should contain 'number_of_gestures' key");
    assertTrue(body.contains("3"), "Body should contain '3' for number_of_gestures");
  }

  @Test
  public void activeLivenessSessions_retrieve_requestIsGet() throws Exception {
    enqueueJson(200, SESSION_RESPONSE);

    vision.faceBio().activeLivenessSessions().retrieve("sid-xyz");

    RecordedRequest req = takeRequest();
    assertEquals("GET", req.getMethod());
    assertTrue(req.getPath().endsWith("/face/v1/active-liveness-sessions/sid-xyz"),
        "Expected path ending in /face/v1/active-liveness-sessions/sid-xyz but got: " + req.getPath());
  }

  // ── PassiveLivenessSessions ───────────────────────────────────────────────────

  @Test
  public void passiveLivenessSessions_create_requestHasCorrectPath() throws Exception {
    BaseSessionsParam param = new BaseSessionsParam("https://success.url");
    enqueueJson(200, SESSION_RESPONSE);

    vision.faceBio().passiveLivenessSessions().create(param);

    RecordedRequest req = takeRequest();
    assertTrue(req.getPath().endsWith("/face/v1/passive-liveness-sessions"),
        "Expected path ending in /face/v1/passive-liveness-sessions but got: " + req.getPath());
    assertEquals("POST", req.getMethod());
  }

  @Test
  public void passiveLivenessSessions_retrieve_requestIsGet() throws Exception {
    enqueueJson(200, SESSION_RESPONSE);

    vision.faceBio().passiveLivenessSessions().retrieve("sid-xyz");

    RecordedRequest req = takeRequest();
    assertEquals("GET", req.getMethod());
    assertTrue(req.getPath().endsWith("/face/v1/passive-liveness-sessions/sid-xyz"),
        "Expected path ending in /face/v1/passive-liveness-sessions/sid-xyz but got: " + req.getPath());
  }

  // ── Param class coverage (no server needed) ───────────────────────────────────

  @Test
  public void baseSessionsParam_cancelUrl_defaultIsNull() {
    KtpSessionsParam param = new KtpSessionsParam("https://success.url");
    assertNull(param.getCancelUrl(), "cancelUrl should be null by default");
  }

  @Test
  public void baseSessionsParam_cancelUrl_canBeSet() {
    KtpSessionsParam param = new KtpSessionsParam("https://success.url");
    param.setCancelUrl("https://cancel.url");
    assertEquals("https://cancel.url", param.getCancelUrl());
  }

  @Test
  public void ktpSessionsParam_qualitiesDetector_defaultIsFalse() {
    KtpSessionsParam param = new KtpSessionsParam("https://success.url");
    assertFalse(param.getQualitiesDetector(), "qualitiesDetector should be false by default");
  }

  @Test
  public void activeLivenessSessionsParam_numberOfGestures_defaultIsOne() {
    ActiveLivenessSessionsParam param = new ActiveLivenessSessionsParam("https://success.url");
    assertEquals(1, param.getNumberOfGestures(), "numberOfGestures should default to 1");
  }

  @Test
  public void sessionType_andBaseUrl_returnCorrectValues() {
    assertEquals("KTP", vision.ocr().ktpSessions().getSessionType());
    assertEquals("ocr/:version/ktp-sessions", vision.ocr().ktpSessions().getBaseUrl());
  }
}
