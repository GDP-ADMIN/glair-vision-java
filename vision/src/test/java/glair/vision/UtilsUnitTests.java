package glair.vision;

import glair.vision.util.Env;
import glair.vision.util.Json;
import glair.vision.util.Util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pure unit tests for Util, Json, and Env — no mock server required.
 */
public class UtilsUnitTests {

  // ----------------------------------------------------------------
  // Util.trimAll
  // ----------------------------------------------------------------

  @Test
  public void trimAll_removesAllWhitespace() {
    assertEquals("helloworld", Util.trimAll("hello world"));
  }

  @Test
  public void trimAll_removesLeadingTrailingWhitespace() {
    assertEquals("hi", Util.trimAll("  hi  "));
  }

  @Test
  public void trimAll_emptyStringStaysEmpty() {
    assertEquals("", Util.trimAll(""));
  }

  // ----------------------------------------------------------------
  // Util.require
  // ----------------------------------------------------------------

  @Test
  public void require_nonNullValue_doesNotThrow() {
    assertDoesNotThrow(() -> Util.require("key", "value"));
  }

  @Test
  public void require_nullValue_throwsWithKeyInMessage() {
    Exception ex = assertThrows(Exception.class, () -> Util.require("myKey", null));
    assertTrue(ex.getMessage().contains("myKey"));
  }

  // ----------------------------------------------------------------
  // Util.checkFileExist
  // ----------------------------------------------------------------

  @Test
  public void checkFileExist_existingFile_doesNotThrow() throws Exception {
    File file = File.createTempFile("test", ".png");
    file.deleteOnExit();
    assertDoesNotThrow(() -> Util.checkFileExist(file.getAbsolutePath()));
  }

  @Test
  public void checkFileExist_nonExistingFile_throwsCorrectMessage() {
    Exception ex = assertThrows(
        Exception.class,
        () -> Util.checkFileExist("/nonexistent/path.png")
    );
    assertEquals("The file does not exist.", ex.getMessage());
  }

  // ----------------------------------------------------------------
  // Util.fileToBase64
  // ----------------------------------------------------------------

  @Test
  public void fileToBase64_existingFile_returnsNonEmptyString() throws Exception {
    File temp = File.createTempFile("test", ".bin");
    temp.deleteOnExit();
    java.nio.file.Files.write(temp.toPath(), new byte[]{1, 2, 3, 4, 5});
    String result = Util.fileToBase64(temp.getAbsolutePath());
    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  @Test
  public void fileToBase64_largeFile_encodesAllBytesWithoutTruncation() throws Exception {
    // Guards against the single-read() truncation regression: a multi-KB file
    // must round-trip to exactly the original bytes.
    File temp = File.createTempFile("large", ".bin");
    temp.deleteOnExit();
    byte[] original = new byte[64 * 1024];
    for (int i = 0; i < original.length; i++) {
      original[i] = (byte) (i % 251);
    }
    java.nio.file.Files.write(temp.toPath(), original);

    String encoded = Util.fileToBase64(temp.getAbsolutePath());
    byte[] decoded = glair.vision.util.Base64.decode(encoded,
        glair.vision.util.Base64.DEFAULT);

    assertArrayEquals(original, decoded);
  }

  // ----------------------------------------------------------------
  // Json.toJsonString
  // ----------------------------------------------------------------

  @Test
  public void jsonString_singleKeyValue_formatsCorrectly() {
    assertEquals("{\"name\": \"Alice\"}", Json.toJsonString("name", "Alice"));
  }

  @Test
  public void jsonString_map_containsAllPairs() {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("a", "1");
    map.put("b", "2");
    String result = Json.toJsonString(map);
    assertTrue(result.contains("\"a\": \"1\""));
    assertTrue(result.contains("\"b\": \"2\""));
    assertTrue(result.startsWith("{") && result.endsWith("}"));
  }

  @Test
  public void jsonString_mapWithIndent_isMultiline() {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("key", "val");
    String result = Json.toJsonString(map, 2);
    assertTrue(result.contains("\n"));
    assertTrue(result.contains("\"key\": \"val\""));
  }

  @Test
  public void jsonString_mapWithIndent_multipleEntries_isValidJson() {
    // Guards against the comma-placement bug (no trailing comma, comma between
    // every entry). Uses LinkedHashMap only for a deterministic assertion.
    java.util.LinkedHashMap<String, String> map = new java.util.LinkedHashMap<String, String>();
    map.put("a", "1");
    map.put("b", "2");
    map.put("c", "3");
    String result = Json.toJsonString(map, 2);

    // Exactly one comma per gap between the three entries, none trailing.
    assertEquals(2, result.chars().filter(ch -> ch == ',').count(),
        "expected exactly two commas for three entries");
    assertFalse(result.replaceAll("\\s+", "").contains(",}"),
        "must not produce a trailing comma before the closing brace");
  }

  // ----------------------------------------------------------------
  // Env
  // ----------------------------------------------------------------

  @Test
  public void env_loadsPropertiesFromFile_returnsCorrectValues() throws Exception {
    String path = new File(getClass()
        .getClassLoader()
        .getResource("test.properties")
        .toURI())
        .getAbsolutePath();
    Env env = new Env(path, false);
    assertEquals("testUser", env.getUsername());
    assertEquals("testPass", env.getPassword());
    assertEquals("testKey", env.getApiKey());
    assertEquals("/path/to/ktp.png", env.getKtp());
    assertNull(env.getNpwp());
  }

  @Test
  public void env_missingFile_throwsException() {
    assertThrows(Exception.class, () -> new Env("/nonexistent/config.properties", false));
  }

  @Test
  public void env_loadsAllProperties_returnsCorrectValues() throws Exception {
    String path = new File(getClass()
        .getClassLoader()
        .getResource("test-all.properties")
        .toURI())
        .getAbsolutePath();
    Env env = new Env(path, false);
    assertEquals("/path/npwp.png", env.getNpwp());
    assertEquals("/path/kk.png", env.getKk());
    assertEquals("/path/stnk.png", env.getStnk());
    assertEquals("/path/bpkb.png", env.getBpkb());
    assertEquals("/path/passport.png", env.getPassport());
    assertEquals("/path/plate.png", env.getLicensePlate());
    assertEquals("/path/doc.png", env.getGeneralDocument());
    assertEquals("/path/invoice.png", env.getInvoice());
    assertEquals("/path/receipt.png", env.getReceipt());
    assertEquals("1234567890123456:Name:01-01-1990", env.getIdentityBasicVerification());
    assertEquals("/path/face.png", env.getIdentityFaceVerification());
    assertEquals("/path/face.png", env.getFace());
  }

  @Test
  public void env_debugMode_doesNotThrow() throws Exception {
    String path = new File(getClass()
        .getClassLoader()
        .getResource("test-all.properties")
        .toURI())
        .getAbsolutePath();
    assertDoesNotThrow(() -> new Env(path, true));
  }
}
