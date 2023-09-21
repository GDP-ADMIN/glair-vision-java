package glair.vision.app.result;

import glair.vision.util.Util;

public class VisionException {
  public static String INVALID_USER() {
    return Util.trimAll("{\"error\": \"Access to this API has been " +
        "disallowed\"}");
  }
}
