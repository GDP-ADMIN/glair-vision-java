package glair.vision;

public class App {
  public static void main(String[] args) {
    Settings settings =
        new Settings.SettingsBuilder().username(
            "sdk-tester").password("bzJ0Vt0a8R2XqVbCPrgH").apiKey(
            "OKMjceKWLXdmTKYwoSCXAVVDWtQWRrhr").build();
    Vision vision = new Vision(settings);

    String imagePath = "/Users/vincent.cuardi/GDP/glair-ocr/demo/kontext-id" +
        "/frontend/public/sample/ktp.png";
//    String response = vision.ocr().ktp(imagePath);

//    FaceBio.MatchParam matchParam = new FaceBio.MatchParam(imagePath,
//        imagePath);
//    String response = vision.faceBio().match(matchParam);

//    FaceBio.PassiveLivenessParam passiveLivenessParam =
//        new FaceBio.PassiveLivenessParam(imagePath);
//    String response = vision.faceBio().passiveLiveness(passiveLivenessParam);

    FaceBio.ActiveLivenessParam activeLivenessParam =
        new FaceBio.ActiveLivenessParam(imagePath, "HAND_00000");
    String response = vision.faceBio().activeLiveness(activeLivenessParam);

    System.out.println("Response: " + response);
  }
}
