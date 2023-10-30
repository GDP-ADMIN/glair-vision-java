package glair.vision.enums;

/**
 * Enum representing gesture codes.
 */
public enum GestureCode {
  /**
   * Represents a hand gesture with code HAND_00000.
   */
  HAND_00000("HAND_00000"),
  /**
   * Represents a hand gesture with code HAND_01000.
   */
  HAND_01000("HAND_01000"),
  /**
   * Represents a hand gesture with code HAND_01100.
   */
  HAND_01100("HAND_01100"),
  /**
   * Represents a hand gesture with code HAND_01110.
   */
  HAND_01110("HAND_01110"),
  /**
   * Represents a hand gesture with code HAND_01111.
   */
  HAND_01111("HAND_01111"),
  /**
   * Represents a hand gesture with code HAND_10000.
   */
  HAND_10000("HAND_10000"),
  /**
   * Represents a hand gesture with code HAND_11000.
   */
  HAND_11000("HAND_11000"),
  /**
   * Represents a hand gesture with code HAND_11001.
   */
  HAND_11001("HAND_11001"),
  /**
   * Represents a hand gesture with code HAND_11111.
   */
  HAND_11111("HAND_11111"),

  /**
   * Represents a hand gesture with code HEAD_00.
   */
  HEAD_00("HEAD_00"),
  /**
   * Represents a hand gesture with code HEAD_01.
   */
  HEAD_01("HEAD_01"),
  /**
   * Represents a hand gesture with code HEAD_10.
   */
  HEAD_10("HEAD_10"),
  /**
   * Represents a hand gesture with code HEAD_11.
   */
  HEAD_11("HEAD_11"),
  /**
   * Represents a hand gesture with code HEAD_LEFT.
   */
  HEAD_LEFT("HEAD_LEFT"),
  /**
   * Represents a hand gesture with code HEAD_OPEN_MOUTH.
   */
  HEAD_OPEN_MOUTH("HEAD_OPEN_MOUTH"),
  /**
   * Represents a hand gesture with code HEAD_RIGHT.
   */
  HEAD_RIGHT("HEAD_RIGHT"),
  /**
   * Represents a hand gesture with code HEAD_UP.
   */
  HEAD_UP("HEAD_UP"),
  /**
   * Represents a hand gesture with code HEAD_CLOSE_MOUTH.
   */
  HEAD_CLOSE_MOUTH("HEAD_CLOSE_MOUTH"),
  /**
   * Represents a hand gesture with code HEAD_DOWN.
   */
  HEAD_DOWN("HEAD_DOWN");

  /**
   * The label associated with the gesture code.
   */
  public final String label;

  /**
   * Constructs a new GestureCode with the given label.
   *
   * @param label The label for the gesture code.
   */
  GestureCode(String label) {
    this.label = label;
  }
}

