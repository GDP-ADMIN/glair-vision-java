package glair.vision;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
public class Ocr {
  private final Config config;

  public Ocr(Config config) {
    this.config = config;
  }

  public String ktp(String imagePath) throws Exception {
    this.config.print();

    // Add Image to Form Data
    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
    Util.addImageToFormData(multipartEntityBuilder, "image", imagePath);
    HttpEntity body = multipartEntityBuilder.build();

    // Create Request
    Request request = new Request.RequestBuilder("ocr/:version/ktp",
        "POST").body(body).build();

    return Util.visionFetch(this.config, request);
  }

  public String ktp(String imagePath, Settings newSettings) throws Exception {
    Config config1 = this.config.getConfig(newSettings);
    config1.print();

    // Add Image to Form Data
    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
    Util.addImageToFormData(multipartEntityBuilder, "image", imagePath);
    HttpEntity body = multipartEntityBuilder.build();

    Request request = new Request.RequestBuilder("ocr/:version/ktp",
        "POST").body(body).build();

    return Util.visionFetch(config1, request);
  }
}
