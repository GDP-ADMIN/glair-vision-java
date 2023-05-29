package glair.vision;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Ocr {
  private static final Logger logger = LogManager.getLogger();

  private final Config config;

  public Ocr(Config config) {
    this.config = config;
  }

  public String ktp(String imagePath) {
    logger.info("OCR - KTP " + new KtpParam(imagePath));

    // Add Image to Form Data
    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
    Util.addImageToFormData(multipartEntityBuilder, "image", imagePath);
    HttpEntity body = multipartEntityBuilder.build();

    // Create Request
    Request request = new Request.RequestBuilder("ocr/:version/ktp",
        "POST").body(body).build();

    String response;

    try {
      response = Util.visionFetch(this.config, request);
    } catch (Exception e) {
      response = e.getMessage();
    }

    return response;
  }

  public String ktp(String imagePath, Settings newSettings) {
    Config config1 = this.config.getConfig(newSettings);
    logger.info("OCR - KTP " + new KtpParam(imagePath));

    // Add Image to Form Data
    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
    Util.addImageToFormData(multipartEntityBuilder, "image", imagePath);
    HttpEntity body = multipartEntityBuilder.build();

    Request request = new Request.RequestBuilder("ocr/:version/ktp",
        "POST").body(body).build();

    String response;

    try {
      response = Util.visionFetch(this.config, request);
    } catch (Exception e) {
      response = e.getMessage();
    }

    return response;
  }

  private record KtpParam(String image) {
    @Override
      public String toString() {
        return Util.json("image", this.image);
      }
    }
}
