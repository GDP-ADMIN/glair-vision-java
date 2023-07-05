package glair.vision;

public class Vision {
  private final Config config;
  private final Ocr ocr;
  private final FaceBio faceBio;
  private final Identity identity;

  public Vision(Settings settings) {
    this.config = new Config(settings);
    this.ocr = new Ocr(this.config);
    this.faceBio = new FaceBio(this.config);
    this.identity = new Identity(this.config);

    System.setProperty("logback.configurationFile", "glair-logback.xml");
  }

  public Config config() {
    return this.config;
  }

  public Ocr ocr() {
    return this.ocr;
  }

  public FaceBio faceBio() {
    return this.faceBio;
  }

  public Identity identity() {
    return this.identity;
  }
}
