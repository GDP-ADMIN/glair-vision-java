package glair.vision.model.param;

import glair.vision.util.Json;
import glair.vision.util.Util;

import java.util.HashMap;

/**
 * Represents parameters for performing identity verification with face image.
 */
public class IdentityFaceVerificationParam {
  private final String nik;
  private final String name;
  private final String dateOfBirth;
  private final String faceImagePath;

  /**
   * Constructs an instance of IdentityFaceVerificationParam.
   *
   * @param builder The builder instance used to construct the parameters.
   */
  private IdentityFaceVerificationParam(Builder builder) {
    this.nik = builder.nik;
    this.name = builder.name;
    this.dateOfBirth = builder.dateOfBirth;
    this.faceImagePath = builder.faceImagePath;
  }

  /**
   * Gets the NIK (Nomor Induk Kependudukan).
   *
   * @return The NIK.
   */
  public String getNik() {
    return nik;
  }

  /**
   * Gets the name of the individual.
   *
   * @return The name.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the date of birth of the individual.
   *
   * @return The date of birth.
   */
  public String getDateOfBirth() {
    return dateOfBirth;
  }

  /**
   * Gets the face image path data.
   *
   * @return The face image path data.
   */
  public String getFaceImagePath() {
    return faceImagePath;
  }

  /**
   * Generates a JSON representation of the parameters.
   *
   * @return The JSON representation.
   */
  @Override
  public String toString() {
    HashMap<String, String> map = new HashMap<>();
    map.put("nik", nik);
    map.put("name", name);
    map.put("date_of_birth", dateOfBirth);
    map.put("face_image_path", faceImagePath);

    return Json.toJsonString(map, 2);
  }

  /**
   * Builder class for constructing IdentityFaceVerificationParam object.
   */
  public static class Builder {
    private String nik;
    private String name;
    private String dateOfBirth;
    private String faceImagePath;

    /**
     * Sets the NIK (Nomor Induk Kependudukan).
     *
     * @param nik The NIK to set.
     * @return The Builder object.
     */
    public Builder nik(String nik) {
      this.nik = nik;
      return this;
    }

    /**
     * Sets the name of the individual.
     *
     * @param name The name (spaces allowed).
     * @return The Builder object.
     */
    public Builder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * Sets the date of birth of the individual.
     *
     * @param dateOfBirth The date of birth in the format "dd-mm-yyyy".
     * @return The Builder object.
     */
    public Builder dateOfBirth(String dateOfBirth) {
      this.dateOfBirth = dateOfBirth;
      return this;
    }

    /**
     * Sets the face image path data.
     *
     * @param faceImagePath The face image path data to set.
     * @return The Builder object.
     */
    public Builder faceImagePath(String faceImagePath) {
      this.faceImagePath = faceImagePath;
      return this;
    }

    /**
     * Build an IdentityVerificationParam object.
     *
     * @return The constructed object.
     * @throws Exception If required fields are missing.
     */
    public IdentityFaceVerificationParam build() throws Exception {
      Util.require("NIK", nik);
      Util.require("Name", name);
      Util.require("Date of Birth", dateOfBirth);
      Util.require("Face Image Path", faceImagePath);

      return new IdentityFaceVerificationParam(this);
    }
  }
}
