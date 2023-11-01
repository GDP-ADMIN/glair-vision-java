package glair.vision.sessions;

import glair.vision.TestsCommon;
import glair.vision.api.sessions.PassiveLivenessSessions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PassiveLivenessSessionsTests {
  private final PassiveLivenessSessions sessions =
      TestsCommon.vision.faceBio().passiveLivenessSessions();

  @Test
  public void testConfiguration() {
    assertEquals(sessions.getSessionType(), "Passive Liveness");
    assertEquals(sessions.getBaseUrl(), "face/:version/passive-liveness-sessions");
  }
}
