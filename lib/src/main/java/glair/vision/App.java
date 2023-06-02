package glair.vision;

import glair.vision.sessions.*;

import java.util.HashMap;

public class App {
  public static void main(String[] args) {
    Settings settings = new Settings.Builder()
        .username("sdk-tester2")
        .password("bzJ0Vt0a8R2XqVbCPrgH")
        .apiKey("OKMjceKWLXdmTKYwoSCXAVVDWtQWRrhr")
        .build();
    Vision vision = new Vision(settings);


    String response = "";

    try {
      //    String imagePath =
      //        "/Users/vincent.cuardi/GDP/glair-ocr/demo/kontext-id" +
      //        "/frontend" + "/public/sample/ktp.png";
      //      Ocr.KtpParam param = new Ocr.KtpParam(imagePath);
      //      response = vision
      //          .ocr()
      //          .ktp(param);

      //      String imagePath = "/Users/vincent.cuardi/Downloads/face.jpeg";
      //      FaceBio.MatchParam matchParam = new FaceBio.MatchParam(imagePath,
      //          imagePath);
      //      response = vision
      //          .faceBio()
      //          .match(matchParam);

      //          FaceBio.PassiveLivenessParam passiveLivenessParam =
      //              new FaceBio.PassiveLivenessParam(imagePath);
      //          response = vision
      //              .faceBio()
      //              .passiveLiveness(passiveLivenessParam);

      //      FaceBio.ActiveLivenessParam activeLivenessParam =
      //          new FaceBio.ActiveLivenessParam(
      //          imagePath,
      //          "HAND_00000");
      //      response = vision
      //          .faceBio()
      //          .activeLiveness(activeLivenessParam);

      //      ActiveLivenessSessions.CreateParam param =
      //          new ActiveLivenessSessions.CreateParam.Builder(
      //          "https://google.com")
      //          .cancelUrl("https://youtube.com")
      //          .numberOfGestures(2)
      //          .build();
      //      response = vision
      //          .faceBio()
      //          .activeLivenessSessions()
      //          .create(param);

      //      ActiveLivenessSessions.RetrieveParam param =
      //          new ActiveLivenessSessions.RetrieveParam(
      //          "GSh9Ylq2QN5SiIzEKqWX3binexTekbTd");
      //      response = vision
      //          .faceBio()
      //          .activeLivenessSessions()
      //          .retrieve(param);

      Identity.FaceVerificationParam param =
          new Identity.FaceVerificationParam.Builder()
          .nik("1234567890123456")
          .name("John Doe")
          .dateOfBirth("01-01-2000")
              .faceImage()
          .build();
      response = vision
          .identity()
          .faceVerification(param);
    } catch (Exception e) {
      response = e.getMessage();
    }

    System.out.println("Response: " + response);
  }
}
