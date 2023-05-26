package glair.vision;

public class Vision {
  private final Config config;
  private final Ocr ocr;
  private final FaceBio faceBio;

  public Vision(Settings settings) {
    this.config = new Config(settings);
    this.ocr = new Ocr(this.config);
    this.faceBio = new FaceBio(this.config);
  }

  public Ocr ocr() {
    return this.ocr;
  }

  public FaceBio faceBio() {
    return this.faceBio;
  }
}
