package com.readytalk.swt.helpers;

public class DebugHelper {
  private static final String NEWLINE = System.getProperty("line.separator");

  public static String getStackTraceAsString(Throwable t) {
    StringBuilder buffer = new StringBuilder();

    for(StackTraceElement ste : t.getStackTrace()) {
      buffer.append(ste + NEWLINE);
    }

    return buffer.toString();
  }
}
