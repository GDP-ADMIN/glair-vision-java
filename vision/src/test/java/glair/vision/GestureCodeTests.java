package glair.vision;

import glair.vision.enums.GestureCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GestureCodeTests {
  @Test
  public void testGestureCode() {
    assertEquals(GestureCode.HAND_00000.label, "HAND_00000");
    assertEquals(GestureCode.HAND_01000.label, "HAND_01000");
    assertEquals(GestureCode.HAND_01100.label, "HAND_01100");
    assertEquals(GestureCode.HAND_01110.label, "HAND_01110");
    assertEquals(GestureCode.HAND_01111.label, "HAND_01111");
    assertEquals(GestureCode.HAND_10000.label, "HAND_10000");
    assertEquals(GestureCode.HAND_11000.label, "HAND_11000");
    assertEquals(GestureCode.HAND_11001.label, "HAND_11001");
    assertEquals(GestureCode.HAND_11111.label, "HAND_11111");

    assertEquals(GestureCode.HEAD_00.label, "HEAD_00");
    assertEquals(GestureCode.HEAD_01.label, "HEAD_01");
    assertEquals(GestureCode.HEAD_10.label, "HEAD_10");
    assertEquals(GestureCode.HEAD_11.label, "HEAD_11");
    assertEquals(GestureCode.HEAD_LEFT.label, "HEAD_LEFT");
    assertEquals(GestureCode.HEAD_OPEN_MOUTH.label, "HEAD_OPEN_MOUTH");
    assertEquals(GestureCode.HEAD_RIGHT.label, "HEAD_RIGHT");
    assertEquals(GestureCode.HEAD_UP.label, "HEAD_UP");
    assertEquals(GestureCode.HEAD_CLOSE_MOUTH.label, "HEAD_CLOSE_MOUTH");
    assertEquals(GestureCode.HEAD_DOWN.label, "HEAD_DOWN");
  }
}
