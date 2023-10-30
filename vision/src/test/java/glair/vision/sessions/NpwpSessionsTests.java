package glair.vision.sessions;

import glair.vision.TestsCommon;
import glair.vision.api.sessions.NpwpSessions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NpwpSessionsTests {
  private final NpwpSessions sessions = TestsCommon.vision.ocr().npwpSessions();

  @Test
  public void testConfiguration() {
    assertEquals(sessions.getSessionType(), "NPWP");
    assertEquals(sessions.getBaseUrl(), "ocr/:version/npwp-sessions");
  }
}
