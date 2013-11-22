package com.readytalk.swt.helpers;

/**
 * Helpers useful in various debugging scenarios.
 */
public class DebugHelper {
  private static final String NEWLINE = System.getProperty("line.separator");

  /**
   * Get the stack trace from a Throwable exception as a String. Useful in printing log statements.
   * @param t The <code>Throwable</code> that you want to extract the stacktrace from
   * @return The extracted stacktrace
   */
  public static String getStackTraceAsString(Throwable t) {
    StringBuilder buffer = new StringBuilder();

    for(StackTraceElement ste : t.getStackTrace()) {
      buffer.append(ste + NEWLINE);
    }

    return buffer.toString();
  }
}
