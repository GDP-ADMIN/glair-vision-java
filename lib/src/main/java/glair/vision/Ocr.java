package glair.vision;

import glair.vision.sessions.KtpSessions;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Ocr {
  private static final Logger logger = LogManager.getLogger();

  private final Config config;
  private final KtpSessions ktpSessions;

  public Ocr(Config config) {
    this.config = config;
    this.ktpSessions = new KtpSessions(config);
  }

  public KtpSessions ktpSessions() {
    return this.ktpSessions;
  }

  public String ktp(KtpParam param) throws Exception {
    return ktp(param, this.config.getSettings());
  }

  public String ktp(KtpParam param, Settings newSettings) throws Exception {
    logger.info("OCR - KTP " + param);

    String url = "ocr/:version/ktp";
    String method = "POST";

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    Util.addImageToFormData(bodyBuilder, "image", param.image);
    HttpEntity body = bodyBuilder.build();

    Request request = new Request.RequestBuilder(url, method)
        .body(body)
        .build();

    return Util.visionFetch(this.config.getConfig(newSettings), request);
  }

  public record KtpParam(String image) {
    @Override
    public String toString() {
      return Util.json("image", this.image);
    }
  }
}
