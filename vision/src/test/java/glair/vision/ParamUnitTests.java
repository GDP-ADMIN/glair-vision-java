package glair.vision;

import glair.vision.model.VisionSettings;
import glair.vision.model.param.FaceMatchParam;
import glair.vision.model.param.IdentityFaceVerificationParam;
import glair.vision.model.param.IdentityVerificationParam;
import glair.vision.model.param.KtpParam;
import glair.vision.model.param.sessions.ActiveLivenessSessionsParam;
import glair.vision.model.param.sessions.BaseSessionsParam;
import glair.vision.model.param.sessions.KtpSessionsParam;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pure unit tests for Param classes — no mock server required.
 */
public class ParamUnitTests {

  // ----------------------------------------------------------------
  // KtpParam
  // ----------------------------------------------------------------

  @Test
  public void ktpParam_toString_containsImagePath() {
    KtpParam param = new KtpParam("/img.png");
    String s = param.toString();
    assertNotNull(s);
    assertTrue(s.contains("/img.png"));
  }

  @Test
  public void ktpParam_withQualitiesDetector_toString_containsTrue() {
    KtpParam param = new KtpParam("/img.png", true);
    String s = param.toString();
    assertNotNull(s);
    assertTrue(s.contains("true"));
  }

  @Test
  public void ktpParam_getters_returnCorrectValues() {
    KtpParam param = new KtpParam("/path/ktp.png", true);
    assertEquals("/path/ktp.png", param.getImagePath());
    assertTrue(param.getQualitiesDetector());
  }

  @Test
  public void ktpParam_defaultQualitiesDetector_isFalse() {
    KtpParam param = new KtpParam("/img.png");
    assertFalse(param.getQualitiesDetector());
  }

  // ----------------------------------------------------------------
  // FaceMatchParam
  // ----------------------------------------------------------------

  @Test
  public void faceMatchParam_toString_containsBothPaths() {
    FaceMatchParam param = new FaceMatchParam("/captured.png", "/stored.png");
    String s = param.toString();
    assertNotNull(s);
    assertTrue(s.contains("/captured.png"));
    assertTrue(s.contains("/stored.png"));
  }

  @Test
  public void faceMatchParam_getters_returnCorrectValues() {
    FaceMatchParam param = new FaceMatchParam("/cap.png", "/sto.png");
    assertEquals("/cap.png", param.getCapturedPath());
    assertEquals("/sto.png", param.getStoredPath());
  }

  // ----------------------------------------------------------------
  // IdentityVerificationParam
  // ----------------------------------------------------------------

  @Test
  public void identityVerificationParam_toString_containsNik() throws Exception {
    IdentityVerificationParam param = new IdentityVerificationParam.Builder()
        .nik("1234567890123456")
        .name("Name")
        .dateOfBirth("01-01-1990")
        .build();
    String s = param.toString();
    assertNotNull(s);
    assertTrue(s.contains("1234567890123456"));
  }

  @Test
  public void identityVerificationParam_getters_returnCorrectValues() throws Exception {
    IdentityVerificationParam param = new IdentityVerificationParam.Builder()
        .nik("9876543210987654")
        .name("John Doe")
        .dateOfBirth("15-06-1985")
        .build();
    assertEquals("9876543210987654", param.getNik());
    assertEquals("John Doe", param.getName());
    assertEquals("15-06-1985", param.getDateOfBirth());
  }

  @Test
  public void identityVerificationParam_missingNik_throwsException() {
    assertThrows(Exception.class, () -> new IdentityVerificationParam.Builder()
        .name("Name")
        .dateOfBirth("01-01-1990")
        .build());
  }

  @Test
  public void identityVerificationParam_missingName_throwsException() {
    assertThrows(Exception.class, () -> new IdentityVerificationParam.Builder()
        .nik("1234567890123456")
        .dateOfBirth("01-01-1990")
        .build());
  }

  @Test
  public void identityVerificationParam_missingDateOfBirth_throwsException() {
    assertThrows(Exception.class, () -> new IdentityVerificationParam.Builder()
        .nik("1234567890123456")
        .name("Name")
        .build());
  }

  // ----------------------------------------------------------------
  // IdentityFaceVerificationParam
  // ----------------------------------------------------------------

  @Test
  public void identityFaceVerificationParam_toString_containsNik() throws Exception {
    IdentityFaceVerificationParam param = new IdentityFaceVerificationParam.Builder()
        .nik("123")
        .name("Name")
        .dateOfBirth("01-01-1990")
        .faceImagePath("/img.png")
        .build();
    String s = param.toString();
    assertNotNull(s);
    assertTrue(s.contains("123"));
  }

  @Test
  public void identityFaceVerificationParam_toString_containsFaceImagePath() throws Exception {
    IdentityFaceVerificationParam param = new IdentityFaceVerificationParam.Builder()
        .nik("1234567890123456")
        .name("Jane Doe")
        .dateOfBirth("20-03-1992")
        .faceImagePath("/face.png")
        .build();
    String s = param.toString();
    assertNotNull(s);
    assertTrue(s.contains("/face.png"));
  }

  @Test
  public void identityFaceVerificationParam_getters_returnCorrectValues() throws Exception {
    IdentityFaceVerificationParam param = new IdentityFaceVerificationParam.Builder()
        .nik("1111111111111111")
        .name("Test User")
        .dateOfBirth("01-01-2000")
        .faceImagePath("/path/face.png")
        .build();
    assertEquals("1111111111111111", param.getNik());
    assertEquals("Test User", param.getName());
    assertEquals("01-01-2000", param.getDateOfBirth());
    assertEquals("/path/face.png", param.getFaceImagePath());
  }

  @Test
  public void identityFaceVerificationParam_missingFaceImagePath_throwsException() {
    assertThrows(Exception.class, () -> new IdentityFaceVerificationParam.Builder()
        .nik("1234567890123456")
        .name("Name")
        .dateOfBirth("01-01-1990")
        .build());
  }

  // ----------------------------------------------------------------
  // BaseSessionsParam
  // ----------------------------------------------------------------

  @Test
  public void baseSessionsParam_toString_containsSuccessUrl() {
    BaseSessionsParam param = new BaseSessionsParam("https://example.com/success");
    String s = param.toString();
    assertNotNull(s);
    assertTrue(s.contains("https://example.com/success"));
  }

  @Test
  public void baseSessionsParam_withCancelUrl_toString_containsBothUrls() {
    BaseSessionsParam param = new BaseSessionsParam("https://example.com/success");
    param.setCancelUrl("https://example.com/cancel");
    String s = param.toString();
    assertNotNull(s);
    assertTrue(s.contains("https://example.com/success"));
    assertTrue(s.contains("https://example.com/cancel"));
  }

  @Test
  public void baseSessionsParam_getters_returnCorrectValues() {
    BaseSessionsParam param = new BaseSessionsParam("https://success.url");
    param.setCancelUrl("https://cancel.url");
    assertEquals("https://success.url", param.getSuccessUrl());
    assertEquals("https://cancel.url", param.getCancelUrl());
  }

  @Test
  public void baseSessionsParam_noCancelUrl_getCancelUrlReturnsNull() {
    BaseSessionsParam param = new BaseSessionsParam("https://success.url");
    assertNull(param.getCancelUrl());
  }

  // ----------------------------------------------------------------
  // KtpSessionsParam
  // ----------------------------------------------------------------

  @Test
  public void ktpSessionsParam_toString_containsSuccessUrl() {
    KtpSessionsParam param = new KtpSessionsParam("https://example.com/success");
    String s = param.toString();
    assertNotNull(s);
    assertTrue(s.contains("https://example.com/success"));
  }

  @Test
  public void ktpSessionsParam_withQualitiesDetector_toString_containsTrue() {
    KtpSessionsParam param = new KtpSessionsParam("https://example.com/success");
    param.setQualitiesDetector(true);
    String s = param.toString();
    assertNotNull(s);
    assertTrue(s.contains("true"));
  }

  @Test
  public void ktpSessionsParam_defaultQualitiesDetector_isFalse() {
    KtpSessionsParam param = new KtpSessionsParam("https://example.com/success");
    assertFalse(param.getQualitiesDetector());
  }

  @Test
  public void ktpSessionsParam_setQualitiesDetector_updatesValue() {
    KtpSessionsParam param = new KtpSessionsParam("https://example.com/success");
    param.setQualitiesDetector(true);
    assertTrue(param.getQualitiesDetector());
  }

  // ----------------------------------------------------------------
  // ActiveLivenessSessionsParam
  // ----------------------------------------------------------------

  @Test
  public void activeLivenessSessionsParam_toString_containsSuccessUrl() {
    ActiveLivenessSessionsParam param = new ActiveLivenessSessionsParam("https://example.com/success");
    String s = param.toString();
    assertNotNull(s);
    assertTrue(s.contains("https://example.com/success"));
  }

  @Test
  public void activeLivenessSessionsParam_toString_containsNumberOfGestures() {
    ActiveLivenessSessionsParam param = new ActiveLivenessSessionsParam("https://example.com/success");
    param.setNumberOfGestures(3);
    String s = param.toString();
    assertNotNull(s);
    assertTrue(s.contains("3"));
  }

  @Test
  public void activeLivenessSessionsParam_defaultNumberOfGestures_isOne() {
    ActiveLivenessSessionsParam param = new ActiveLivenessSessionsParam("https://example.com/success");
    assertEquals(1, param.getNumberOfGestures());
  }

  @Test
  public void activeLivenessSessionsParam_setNumberOfGestures_updatesValue() {
    ActiveLivenessSessionsParam param = new ActiveLivenessSessionsParam("https://example.com/success");
    param.setNumberOfGestures(5);
    assertEquals(5, param.getNumberOfGestures());
  }

  // ----------------------------------------------------------------
  // Vision.config() and Vision.printLoggerConfig()
  // ----------------------------------------------------------------

  @Test
  public void vision_configAndPrintLogger() {
    VisionSettings settings = new VisionSettings.Builder()
        .username("u")
        .password("p")
        .apiKey("k")
        .build();
    Vision v = new Vision(settings);
    assertNotNull(v.config());
    assertDoesNotThrow(() -> v.printLoggerConfig());
  }
}
