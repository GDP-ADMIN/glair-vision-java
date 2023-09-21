package glair.vision.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * The `Env` class provides access to configuration properties loaded from a file.
 * It allows retrieving specific properties such as usernames, passwords, API keys,
 * and various document-related values.
 */
public class Env {
  /**
   * The path to the configuration properties file.
   */
  private final String filePath = "config.properties";
  /**
   * A `Properties` object to store the loaded properties.
   */
  private final Properties properties = new Properties();

  /**
   * Constructs an `Env` instance and loads configuration properties from the default
   * file.
   *
   * @throws Exception If an error occurs while loading the properties file.
   */
  public Env() throws Exception {
    try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
      properties.load(fileInputStream);
    }
  }

  /**
   * Constructs an `Env` instance with the option to print the absolute path of the
   * properties file.
   *
   * @param debug Set to `true` to print the absolute path of the properties file.
   * @throws Exception If an error occurs while loading the properties file.
   */
  public Env(boolean debug) throws Exception {
    this();

    if (debug) {
      File file = new File(filePath);
      System.out.println(file.getAbsolutePath());
    }
  }

  /**
   * Retrieves a specific property value from the loaded configuration properties.
   *
   * @param key The key of the property to retrieve.
   * @return The value of the specified property or null if the property is not found.
   */
  private String getProperty(String key) {
    return properties.getProperty(key);
  }

  /**
   * Retrieves the username from the configuration properties.
   *
   * @return The username.
   */
  public String getUsername() {
    return getProperty("username");
  }

  /**
   * Retrieves the password from the configuration properties.
   *
   * @return The password.
   */
  public String getPassword() {
    return getProperty("password");
  }

  /**
   * Retrieves the api key from the configuration properties.
   *
   * @return The api key.
   */
  public String getApiKey() {
    return getProperty("apiKey");
  }

  /**
   * Retrieves the KTP image path from the configuration properties.
   *
   * @return The KTP image path.
   */
  public String getKtp() {
    return getProperty("ktp");
  }

  /**
   * Retrieves the NPWP image path from the configuration properties.
   *
   * @return The NPWP image path.
   */
  public String getNpwp() {
    return getProperty("npwp");
  }

  /**
   * Retrieves the KK image path from the configuration properties.
   *
   * @return The KK image path.
   */
  public String getKk() {
    return getProperty("kk");
  }

  /**
   * Retrieves the STNK image path from the configuration properties.
   *
   * @return The STNK image path.
   */
  public String getStnk() {
    return getProperty("stnk");
  }

  /**
   * Retrieves the BPKB image path from the configuration properties.
   *
   * @return The BPKB image path.
   */
  public String getBpkb() {
    return getProperty("bpkb");
  }

  /**
   * Retrieves the Passport image path from the configuration properties.
   *
   * @return The Passport image path.
   */
  public String getPassport() {
    return getProperty("passport");
  }

  /**
   * Retrieves the License Plate image path from the configuration properties.
   *
   * @return The License Plate image path.
   */
  public String getLicensePlate() {
    return getProperty("licensePlate");
  }

  /**
   * Retrieves the General Document image path from the configuration properties.
   *
   * @return The General Document image path.
   */
  public String getGeneralDocument() {
    return getProperty("generalDocument");
  }

  /**
   * Retrieves the Invoice image path from the configuration properties.
   *
   * @return The Invoice image path.
   */
  public String getInvoice() {
    return getProperty("invoice");
  }

  /**
   * Retrieves the Receipt image path from the configuration properties.
   *
   * @return The Receipt image path.
   */
  public String getReceipt() {
    return getProperty("receipt");
  }

  public String getIdentityBasicVerification() {
    return getProperty("identityBasicVerification");
  }

  public String getIdentityFaceVerification() {
    return getProperty("identityFaceVerification");
  }
}
