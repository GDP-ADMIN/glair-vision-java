package glair.vision;

import glair.vision.sessions.KtpSessions;
//import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

//@Slf4j
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
//    System.setProperty("logback.configurationFile", "glair-logback.xml");
    //    log.info("any");

    String url = "ocr/:version/ktp";
    String method = "POST";

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    Util.addFileToFormData(bodyBuilder, "image", param.image);
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

  public String npwp(String image) throws Exception {
    return npwp(image, this.config.getSettings());
  }

  public String npwp(String image, Settings newSettings) throws Exception {
    logger.info("OCR - NPWP " + Util.json("image", image));

    String url = "ocr/:version/npwp";
    String method = "POST";

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    Util.addFileToFormData(bodyBuilder, "image", image);
    HttpEntity body = bodyBuilder.build();

    Request request = new Request.RequestBuilder(url, method)
        .body(body)
        .build();

    return Util.visionFetch(this.config.getConfig(newSettings), request);
  }

  public String kk(String image) throws Exception {
    return kk(image, this.config.getSettings());
  }

  public String kk(String image, Settings newSettings) throws Exception {
    logger.info("OCR - KK " + Util.json("image", image));

    String url = "ocr/:version/kk";
    String method = "POST";

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    Util.addFileToFormData(bodyBuilder, "image", image);
    HttpEntity body = bodyBuilder.build();

    Request request = new Request.RequestBuilder(url, method)
        .body(body)
        .build();

    return Util.visionFetch(this.config.getConfig(newSettings), request);
  }

  public String stnk(String image) throws Exception {
    return stnk(image, this.config.getSettings());
  }

  public String stnk(String image, Settings newSettings) throws Exception {
    logger.info("OCR - STNK " + Util.json("image", image));

    String url = "ocr/:version/stnk";
    String method = "POST";

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    Util.addFileToFormData(bodyBuilder, "image", image);
    HttpEntity body = bodyBuilder.build();

    Request request = new Request.RequestBuilder(url, method)
        .body(body)
        .build();

    return Util.visionFetch(this.config.getConfig(newSettings), request);
  }

  public String bpkb(BpkbParam param) throws Exception {
    return bpkb(param, this.config.getSettings());
  }

  public String bpkb(BpkbParam param, Settings newSettings) throws Exception {
    logger.info("OCR - BPKB " + param);

    String url = "ocr/:version/bpkb";
    String method = "POST";

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    Util.addFileToFormData(bodyBuilder, "image", param.getImage());
    if (param.getPage() >= 1 && param.getPage() <= 4) {
      bodyBuilder.addTextBody("page", Integer.toString(param.getPage()));
    }
    HttpEntity body = bodyBuilder.build();

    Request request = new Request.RequestBuilder(url, method)
        .body(body)
        .build();

    return Util.visionFetch(this.config.getConfig(newSettings), request);
  }

  public static class BpkbParam {
    private final String image;
    private int page;

    public BpkbParam(String imagePath) {
      this.image = imagePath;
    }

    public BpkbParam(String imagePath, int page) {
      this.image = imagePath;
      this.page = page;
    }

    public String getImage() {
      return this.image;
    }

    public int getPage() {
      return this.page;
    }

    @Override
    public String toString() {
      HashMap<String, String> map = new HashMap<>();
      map.put("image", this.image);
      map.put("page", Integer.toString(this.page));

      return Util.json(map, 2);
    }
  }

  public String passport(String image) throws Exception {
    return passport(image, this.config.getSettings());
  }

  public String passport(String image, Settings newSettings) throws Exception {
    logger.info("OCR - Passport " + Util.json("image", image));

    String url = "ocr/:version/passport";
    String method = "POST";

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    Util.addFileToFormData(bodyBuilder, "image", image);
    HttpEntity body = bodyBuilder.build();

    Request request = new Request.RequestBuilder(url, method)
        .body(body)
        .build();

    return Util.visionFetch(this.config.getConfig(newSettings), request);
  }

  public String plate(String image) throws Exception {
    return plate(image, this.config.getSettings());
  }

  public String plate(String image, Settings newSettings) throws Exception {
    logger.info("OCR - Plate " + Util.json("image", image));

    String url = "ocr/:version/plate";
    String method = "POST";

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    Util.addFileToFormData(bodyBuilder, "image", image);
    HttpEntity body = bodyBuilder.build();

    Request request = new Request.RequestBuilder(url, method)
        .body(body)
        .build();

    return Util.visionFetch(this.config.getConfig(newSettings), request);
  }

  public String generalDocument(String image) throws Exception {
    return generalDocument(image, this.config.getSettings());
  }

  public String generalDocument(String image, Settings newSettings) throws Exception {
    logger.info("OCR - General Document " + Util.json("image", image));

    String url = "ocr/:version/general-document";
    String method = "POST";

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    Util.addFileToFormData(bodyBuilder, "image", image);
    HttpEntity body = bodyBuilder.build();

    Request request = new Request.RequestBuilder(url, method)
        .body(body)
        .build();

    return Util.visionFetch(this.config.getConfig(newSettings), request);
  }

  public String invoice(String image) throws Exception {
    return invoice(image, this.config.getSettings());
  }

  public String invoice(String image, Settings newSettings) throws Exception {
    logger.info("OCR - Invoice " + Util.json("image", image));

    String url = "ocr/:version/invoice";
    String method = "POST";

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    Util.addFileToFormData(bodyBuilder, "image", image);
    HttpEntity body = bodyBuilder.build();

    Request request = new Request.RequestBuilder(url, method)
        .body(body)
        .build();

    return Util.visionFetch(this.config.getConfig(newSettings), request);
  }

  public String receipt(String image) throws Exception {
    return receipt(image, this.config.getSettings());
  }

  public String receipt(String image, Settings newSettings) throws Exception {
    logger.info("OCR - Receipt " + Util.json("image", image));

    String url = "ocr/:version/receipt";
    String method = "POST";

    MultipartEntityBuilder bodyBuilder = MultipartEntityBuilder.create();
    Util.addFileToFormData(bodyBuilder, "image", image);
    HttpEntity body = bodyBuilder.build();

    Request request = new Request.RequestBuilder(url, method)
        .body(body)
        .build();

    return Util.visionFetch(this.config.getConfig(newSettings), request);
  }
}
