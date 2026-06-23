package glair.vision;

import glair.vision.util.Base64;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Base64 encode/decode covering branch coverage gaps in
 * Encoder and Decoder inner classes.
 */
public class Base64UnitTests {

  // -----------------------------------------------------------------------
  // Roundtrip tests (cover Decoder fast path and finish state machine)
  // -----------------------------------------------------------------------

  @Test
  public void encodeDecodeRoundtrip_default_emptyInput() {
    byte[] input = new byte[0];
    byte[] encoded = Base64.encode(input, Base64.DEFAULT);
    byte[] decoded = Base64.decode(encoded, Base64.DEFAULT);
    assertArrayEquals(input, decoded);
  }

  @Test
  public void encodeDecodeRoundtrip_oneByte_state2finish() throws Exception {
    // len%3==1 → encoder finish writes 2 chars + 2 padding
    byte[] input = {(byte) 0xAB};
    String encoded = Base64.encodeToString(input, Base64.DEFAULT);
    byte[] decoded = Base64.decode(encoded, Base64.DEFAULT);
    assertArrayEquals(input, decoded);
  }

  @Test
  public void encodeDecodeRoundtrip_twoBytes_state3finish() throws Exception {
    // len%3==2 → encoder finish writes 3 chars + 1 padding
    byte[] input = {(byte) 0xAB, (byte) 0xCD};
    String encoded = Base64.encodeToString(input, Base64.DEFAULT);
    byte[] decoded = Base64.decode(encoded, Base64.DEFAULT);
    assertArrayEquals(input, decoded);
  }

  @Test
  public void encodeDecodeRoundtrip_threeBytes_multipleOf3() throws Exception {
    // len%3==0 → encoder main loop only, no tail
    byte[] input = {(byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
    String encoded = Base64.encodeToString(input, Base64.DEFAULT);
    byte[] decoded = Base64.decode(encoded, Base64.DEFAULT);
    assertArrayEquals(input, decoded);
  }

  @Test
  public void encodeDecodeRoundtrip_largeInput_multipleBlocks() throws Exception {
    // 60 bytes = 20 full 3-byte groups → exercises the fast path loop in Decoder
    byte[] input = new byte[60];
    for (int i = 0; i < input.length; i++) {
      input[i] = (byte) (i & 0xFF);
    }
    String encoded = Base64.encodeToString(input, Base64.DEFAULT);
    byte[] decoded = Base64.decode(encoded, Base64.DEFAULT);
    assertArrayEquals(input, decoded);
  }

  // -----------------------------------------------------------------------
  // NO_PADDING flag (covers NO_PADDING branch in Encoder)
  // -----------------------------------------------------------------------

  @Test
  public void encode_noPadding_oneByte_noEqualsSign() {
    byte[] input = {(byte) 0x01};
    String encoded = Base64.encodeToString(input, Base64.NO_PADDING);
    assertFalse(encoded.contains("="), "NO_PADDING output should not contain '='");
    // roundtrip decode should still work
    assertArrayEquals(input, Base64.decode(encoded, Base64.NO_PADDING));
  }

  @Test
  public void encode_noPadding_twoBytes_noEqualsSign() {
    byte[] input = {(byte) 0x01, (byte) 0x02};
    String encoded = Base64.encodeToString(input, Base64.NO_PADDING);
    assertFalse(encoded.contains("="), "NO_PADDING output should not contain '='");
  }

  // -----------------------------------------------------------------------
  // URL_SAFE flag (covers URL_SAFE branches in both Encoder and Decoder)
  // -----------------------------------------------------------------------

  @Test
  public void encode_urlSafe_usesMinusAndUnderscore() {
    // Use bytes that would produce + or / in standard encoding
    byte[] input = new byte[3];
    input[0] = (byte) 0xFB;
    input[1] = (byte) 0xFF;
    input[2] = (byte) 0xFE;

    String standard = Base64.encodeToString(input, Base64.DEFAULT);
    String urlSafe = Base64.encodeToString(input, Base64.URL_SAFE);

    // URL safe should not contain + or /
    assertFalse(urlSafe.contains("+"), "URL_SAFE output should not contain '+'");
    assertFalse(urlSafe.contains("/"), "URL_SAFE output should not contain '/'");

    // roundtrip
    assertArrayEquals(input, Base64.decode(urlSafe, Base64.URL_SAFE));
  }

  // -----------------------------------------------------------------------
  // decode(String, int) overload
  // -----------------------------------------------------------------------

  @Test
  public void decode_fromString_standard() throws Exception {
    String encoded = "SGVsbG8="; // "Hello" in base64
    byte[] decoded = Base64.decode(encoded, Base64.DEFAULT);
    assertEquals("Hello", new String(decoded, "UTF-8"));
  }

  // -----------------------------------------------------------------------
  // decode(byte[], int) overload
  // -----------------------------------------------------------------------

  @Test
  public void decode_fromByteArray_standard() throws Exception {
    byte[] original = "World".getBytes("UTF-8");
    byte[] encoded = Base64.encode(original, Base64.DEFAULT);
    byte[] decoded = Base64.decode(encoded, Base64.DEFAULT);
    assertArrayEquals(original, decoded);
  }

  // -----------------------------------------------------------------------
  // encode/decode with offset and len variants
  // -----------------------------------------------------------------------

  @Test
  public void encodeWithOffsetAndLen() {
    byte[] data = {(byte) 0x00, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, (byte) 0x00};
    // Encode bytes at offset=1, len=3
    byte[] encoded = Base64.encode(data, 1, 3, Base64.DEFAULT);
    byte[] expected = Base64.encode(new byte[]{(byte) 0xAB, (byte) 0xCD, (byte) 0xEF},
        Base64.DEFAULT);
    assertArrayEquals(expected, encoded);
  }

  @Test
  public void encodeToStringWithOffsetAndLen() throws Exception {
    byte[] data = "Hello".getBytes("UTF-8");
    String encoded = Base64.encodeToString(data, 0, 3, Base64.DEFAULT);
    assertNotNull(encoded);
    assertFalse(encoded.trim().isEmpty());
  }

  // -----------------------------------------------------------------------
  // Constants
  // -----------------------------------------------------------------------

  @Test
  public void constants_haveCorrectValues() {
    assertEquals(0, Base64.DEFAULT);
    assertEquals(1, Base64.NO_PADDING);
    assertEquals(2, Base64.NO_WRAP);
    assertEquals(4, Base64.CRLF);
    assertEquals(8, Base64.URL_SAFE);
    assertEquals(16, Base64.NO_CLOSE);
  }

  // -----------------------------------------------------------------------
  // Known-value tests (cover state machine branches in Decoder)
  // -----------------------------------------------------------------------

  @Test
  public void decode_knownValue_matchesExpected() throws Exception {
    // "Man" encodes to "TWFu" in standard Base64
    byte[] decoded = Base64.decode("TWFu", Base64.DEFAULT);
    assertArrayEquals("Man".getBytes("UTF-8"), decoded);
  }

  @Test
  public void decode_withPadding_twoEquals() throws Exception {
    // "M" encodes to "TQ==" (state=2 finish in decoder)
    byte[] decoded = Base64.decode("TQ==", Base64.DEFAULT);
    assertArrayEquals("M".getBytes("UTF-8"), decoded);
  }

  @Test
  public void decode_withPadding_oneEquals() throws Exception {
    // "Ma" encodes to "TWE=" (state=3 finish in decoder, EQUALS branch)
    byte[] decoded = Base64.decode("TWE=", Base64.DEFAULT);
    assertArrayEquals("Ma".getBytes("UTF-8"), decoded);
  }

  // -----------------------------------------------------------------------
  // Error paths — invalid base64 input causes IllegalArgumentException
  // -----------------------------------------------------------------------

  @Test
  public void decode_invalidInput_leadingEquals_throwsIllegalArgument() {
    // '=' at position 0 → state=0, d=EQUALS (-2), d!=SKIP → state=6, returns false
    assertThrows(IllegalArgumentException.class,
        () -> Base64.decode("=abc", Base64.DEFAULT));
  }

  @Test
  public void decode_invalidInput_earlyEquals_throwsIllegalArgument() {
    // 'T' valid (state→1), then '=' → state=1, d=EQUALS, d!=SKIP → returns false
    assertThrows(IllegalArgumentException.class,
        () -> Base64.decode("T=bc", Base64.DEFAULT));
  }

  @Test
  public void decode_invalidInput_badCharAfterFirstPadding_throwsIllegalArgument() {
    // "TQ=X": state→2 on '=', goes to state 4; then 'X' (d>=0) → not EQUALS/SKIP → error
    assertThrows(IllegalArgumentException.class,
        () -> Base64.decode("TQ=X", Base64.DEFAULT));
  }

  @Test
  public void decode_invalidInput_singleBase64Char_throwsIllegalArgument() {
    // Only 1 real base64 char in input — state=1 at finish → illegal
    assertThrows(IllegalArgumentException.class,
        () -> Base64.decode("T", Base64.DEFAULT));
  }

  // -----------------------------------------------------------------------
  // URL_SAFE decode — uses DECODE_WEBSAFE table (- and _ instead of + and /)
  // -----------------------------------------------------------------------

  @Test
  public void decode_urlSafe_withMinusAndUnderscore_roundtrips() throws Exception {
    byte[] input = {(byte) 0xFB, (byte) 0xFF, (byte) 0xFE};
    String urlSafeEncoded = Base64.encodeToString(input, Base64.URL_SAFE);
    // URL safe encoded contains - or _ instead of + or /
    byte[] decoded = Base64.decode(urlSafeEncoded, Base64.URL_SAFE);
    assertArrayEquals(input, decoded);
  }

  // -----------------------------------------------------------------------
  // NO_PADDING with 3-byte-multiple input (len%3==0, no tail output)
  // -----------------------------------------------------------------------

  @Test
  public void encode_noPadding_threeByteMultiple_noEquals() {
    byte[] input = {(byte) 0xAB, (byte) 0xCD, (byte) 0xEF};  // len%3==0
    String encoded = Base64.encodeToString(input, Base64.NO_PADDING);
    assertFalse(encoded.contains("="));
    // Should still decode correctly
    assertArrayEquals(input, Base64.decode(encoded, Base64.NO_PADDING));
  }

  // -----------------------------------------------------------------------
  // decode(byte[], int, int, int) — offset+len variant covering shorten-array path
  // -----------------------------------------------------------------------

  @Test
  public void decode_withOffsetAndLen_correctlySlices() throws Exception {
    // Encode "Hello" (5 bytes)
    byte[] helloBytes = "Hello".getBytes("UTF-8");
    byte[] encoded = Base64.encode(helloBytes, Base64.DEFAULT);
    // Wrap in a larger array with padding bytes around the encoded data
    byte[] wrapped = new byte[encoded.length + 4];
    System.arraycopy(encoded, 0, wrapped, 2, encoded.length);
    byte[] decoded = Base64.decode(wrapped, 2, encoded.length, Base64.DEFAULT);
    assertArrayEquals(helloBytes, decoded);
  }

  // -----------------------------------------------------------------------
  // getBasicAuth cached path — call twice to cover the cache branch
  // -----------------------------------------------------------------------

  @Test
  public void decode_state1Finish_oneSingleCharInput_throwsIllegalArgument() {
    // Verify state=1 at finish (exactly 1 valid base64 char) throws
    assertThrows(IllegalArgumentException.class,
        () -> Base64.decode(new byte[]{'A'}, Base64.DEFAULT));
  }

  @Test
  public void decode_state4OnePadding_throwsIllegalArgument() {
    // "TQ=" has only one padding but needs two: state=4 at finish → illegal
    assertThrows(IllegalArgumentException.class,
        () -> Base64.decode("TQ=", Base64.DEFAULT));
  }
}
