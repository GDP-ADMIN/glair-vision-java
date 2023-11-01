package glair.vision.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for working with JSON data and formatting.
 */
public class Json {
  /**
   * Formats a single key-value pair as a JSON property.
   *
   * @param key   The key.
   * @param value The value.
   * @return The formatted key-value string in JSON property format.
   */
  private static String formatJsonProperty(String key, String value) {
    return "\"" + key + "\": \"" + value + "\"";
  }

  /**
   * Formats a single key-value pair into a JSON string.
   *
   * @param key   The key.
   * @param value The value.
   * @return The formatted JSON object as a string.
   */
  public static String toJsonString(String key, String value) {
    return "{" + formatJsonProperty(key, value) + "}";
  }

  /**
   * Formats a map of key-value pairs into a JSON string.
   *
   * @param map The map containing key-value pairs.
   * @return The formatted JSON object as a string.
   */
  public static String toJsonString(HashMap<String, String> map) {
    StringBuilder stringBuilder = new StringBuilder();
    boolean first = true;

    stringBuilder.append("{");

    for (Map.Entry<String, String> entry : map.entrySet()) {
      if (!first) {
        stringBuilder.append(", ");
      }
      stringBuilder.append(formatJsonProperty(entry.getKey(), entry.getValue()));
      first = false;
    }

    stringBuilder.append("}");
    return stringBuilder.toString();
  }

  /**
   * Formats a map of key-value pairs into a JSON string with indentation.
   *
   * @param map    The map containing key-value pairs.
   * @param indent The number of spaces for indentation.
   * @return The formatted JSON object as a string.
   */
  public static String toJsonString(HashMap<String, String> map, int indent) {
    String tab = createTab(indent);
    StringBuilder stringBuilder = new StringBuilder("{\n");

    for (Map.Entry<String, String> entry : map.entrySet()) {
      stringBuilder
          .append(tab)
          .append(formatJsonProperty(entry.getKey(), entry.getValue()));
      if (!entry.equals(map.entrySet().iterator().next())) {
        stringBuilder.append(",");
      }
      stringBuilder.append("\n");
    }

    stringBuilder.append("}");
    return stringBuilder.toString();
  }

  private static String createTab(int indent) {
    StringBuilder indentation = new StringBuilder();
    for (int i = 0; i < indent; i++) {
      indentation.append(" ");
    }

    return indentation.toString();
  }
}
