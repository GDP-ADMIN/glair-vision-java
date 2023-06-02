package glair.vision;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class Identity {
  private static final Logger logger = LogManager.getLogger();

  private final Config config;

  public Identity(Config config) {
    this.config = config;
  }

  public String verification(VerificationParam param) throws Exception {
    return verification(param, this.config.getSettings());
  }

  public String verification(VerificationParam param, Settings newSettings) throws Exception {
    logger.info("Identity - Basic Verification");

    String url = "identity/:version/verification";
    String method = "POST";

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    bodyBuilder.addTextBody("nik", param.getNik());
    bodyBuilder.addTextBody("name", param.getName());
    bodyBuilder.addTextBody("date_of_birth", param.getDateOfBirth());
    HttpEntity body = bodyBuilder.build();

    Request request = new Request.RequestBuilder(url, method)
        .body(body)
        .build();

    return Util.visionFetch(this.config.getConfig(newSettings), request);
  }

  public String faceVerification(FaceVerificationParam param) throws Exception {
    return faceVerification(param, this.config.getSettings());
  }

  public String faceVerification(FaceVerificationParam param,
                                 Settings newSettings) throws Exception {
    logger.info("Identity - Face Verification");

    String url = "identity/:version/face-verification";
    String method = "POST";

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    bodyBuilder.addTextBody("nik", param.getNik());
    bodyBuilder.addTextBody("name", param.getName());
    bodyBuilder.addTextBody("date_of_birth", param.getDateOfBirth());
    Util.addImageToFormData(bodyBuilder, "face_image", param.getFaceImage());
    HttpEntity body = bodyBuilder.build();

    Request request = new Request.RequestBuilder(url, method)
        .body(body)
        .build();

    return Util.visionFetch(this.config.getConfig(newSettings), request);
  }

  public static class VerificationParam {
    private final String nik;
    private final String name;
    private final String dateOfBirth;

    private VerificationParam(Builder builder) {
      this.nik = builder.nik;
      this.name = builder.name;
      this.dateOfBirth = builder.dateOfBirth;
    }

    public String getNik() {
      return this.nik;
    }

    public String getName() {
      return this.name;
    }

    public String getDateOfBirth() {
      return this.dateOfBirth;
    }

    @Override
    public String toString() {
      HashMap<String, String> map = new HashMap<>();
      map.put("nik", this.nik);
      map.put("name", this.name);
      map.put("date_of_birth", this.dateOfBirth);

      return Util.json(map, 2);
    }

    public static class Builder {
      private String nik;
      private String name;
      private String dateOfBirth;

      public Builder() {}

      public Builder nik(String nik) {
        this.nik = nik;
        return this;
      }

      public Builder name(String name) {
        this.name = name;
        return this;
      }

      public Builder dateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
      }

      public VerificationParam build() {
        return new VerificationParam(this);
      }
    }
  }

  public static class FaceVerificationParam {
    private final String nik;
    private final String name;
    private final String dateOfBirth;
    private final String faceImage;

    private FaceVerificationParam(Builder builder) {
      this.nik = builder.nik;
      this.name = builder.name;
      this.dateOfBirth = builder.dateOfBirth;
      this.faceImage = builder.faceImage;
    }

    public String getNik() {
      return this.nik;
    }

    public String getName() {
      return this.name;
    }

    public String getDateOfBirth() {
      return this.dateOfBirth;
    }

    public String getFaceImage() {
      return this.faceImage;
    }

    @Override
    public String toString() {
      HashMap<String, String> map = new HashMap<>();
      map.put("nik", this.nik);
      map.put("name", this.name);
      map.put("date_of_birth", this.dateOfBirth);
      map.put("face_image", this.faceImage);

      return Util.json(map, 2);
    }

    public static class Builder {
      private String nik;
      private String name;
      private String dateOfBirth;
      private String faceImage;

      public Builder() {}

      public Builder nik(String nik) {
        this.nik = nik;
        return this;
      }

      public Builder name(String name) {
        this.name = name;
        return this;
      }

      public Builder dateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
      }

      public Builder faceImage(String faceImage) {
        this.faceImage = faceImage;
        return this;
      }

      public FaceVerificationParam build() {
        return new FaceVerificationParam(this);
      }
    }
  }

}
