package glair.vision.model.param;

import glair.vision.util.Json;

import java.util.HashMap;

/**
 * Represents parameters for a BPKB (Buku Pemilik Kendaraan Bermotor) operation.
 */
public class BpkbParam {
  private final String imagePath;
  private final int page;

  /**
   * Constructs a new BpkbParam object with the given image path and default page number.
   *
   * @param imagePath The path to the image.
   */
  public BpkbParam(String imagePath) {
    this(imagePath, 0);
  }

  /**
   * Constructs a new BpkbParam object with the given image path and page number.
   *
   * @param imagePath The path to the image.
   * @param page      The page number (1 - 4).
   */
  public BpkbParam(String imagePath, int page) {
    this.imagePath = imagePath;
    this.page = page;
  }

  /**
   * Get the path to the image.
   *
   * @return The image path.
   */
  public String getImagePath() {
    return imagePath;
  }

  /**
   * Get the page number.
   *
   * @return The page number.
   */
  public int getPage() {
    return page;
  }

  /**
   * Generate a JSON representation of the parameter object.
   *
   * @return The JSON string.
   */
  @Override
  public String toString() {
    HashMap<String, String> map = new HashMap<>();
    map.put("image", imagePath);
    map.put("page", Integer.toString(page));

    return Json.toJsonString(map, 2);
  }
}