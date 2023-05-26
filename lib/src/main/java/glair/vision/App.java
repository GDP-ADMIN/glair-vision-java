package glair.vision;

public class App {
  public static void main(String[] args) {
    Settings settings =
        new Settings.SettingsBuilder().username(
            "sdk-tester").password("bzJ0Vt0a8R2XqVbCPrgH").apiKey(
            "OKMjceKWLXdmTKYwoSCXAVVDWtQWRrhr").build();
    Vision vision = new Vision(settings);

    try {
      String response = vision.ocr().ktp("/Users/vincent.cuardi" +
          "/GDP/glair-ocr/demo/kontext-id/frontend/public/sample/ktp.png");

      System.out.println("Response: " + response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
