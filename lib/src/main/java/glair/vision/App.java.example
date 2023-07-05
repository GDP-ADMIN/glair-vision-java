package glair.vision;

public class App {
  public static void main(String[] args) {
    Settings settings = new Settings.Builder()
        .username("username")
        .password("password")
        .apiKey("apiKey")
        .build();
    Vision vision = new Vision(settings);

    String response = "";

    try {
      Ocr.KtpParam param = new Ocr.KtpParam("/path/to/image.jpg");
      response = vision
          .ocr()
          .ktp(param);
    } catch (Exception e) {
      response = e.getMessage();
    }

    System.out.println("Response: " + response);
  }
}
