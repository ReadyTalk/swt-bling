package com.readytalk.swt.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

import java.util.HashMap;
import java.util.Map;

/**
 * FontFactory is meant as a central repository for getting colors so that we don't allocate
 * duplicates everywhere; this can greatly decrease the number of fonts we allocate.
 */
public class FontFactory {

  public static final String WINDOWS = "Windows";
  public static final String SANS_SERIF = "Sans-Serif";
  public static final String MAC = "Mac";
  public static final String LUCIDA_GRANDE = "Lucida-Grande";
  public static final String VERA = "Vera";
  public static final String OS_NAME = "os.name";

  private static final int FONT_DPI = 72;
  private static final int DEFAULT_SIZE = 10;


  private static String defaultName;
  private static Map<FontData, Font> fontMap;

  static {
    fontMap = new HashMap<FontData, Font>();

    String osName = System.getProperty(OS_NAME);
    if (osName == null || osName.length() == 0 || osName.startsWith(WINDOWS)) {
      defaultName = SANS_SERIF;
    } else if (osName.startsWith(MAC)) {
      defaultName = LUCIDA_GRANDE;
    } else {
      defaultName = VERA;
    }
  }


  // converts a "size" metric to the nearest system-dependent
  // "point" metric - based on DPIs
  private static int fontPoint(Device dev, int size) {
    int systemDPI = dev.getDPI().y;
    double ratio = (double)FONT_DPI / systemDPI;
    return (int)(ratio * size);
  }

  /**
   * Gives you a font with the default size, normal style, and default font. Clients
   * should not dispose the font returned by this function call, as the owner is
   * FontFactory.
   * @param dev Device object off of which to create the font.
   * @return
   */
  public static Font getFont(Device dev) {
    return getFont(dev, DEFAULT_SIZE, SWT.NORMAL, defaultName);
  }

  /**
   * Gives you a font with the specified size, normal style, and default font. Clients
   * should not dispose the font returned by this function call, as the owner is
   * FontFactory.
   * @param dev Device object off of which to create the font.
   * @return
   */
  public static Font getFont(Device dev, int size) {
    return getFont(dev, size, SWT.NORMAL, defaultName);
  }

  /**
   * Gives you a font with the specified size, specified style, and default font. Clients
   * should not dispose the font returned by this function call, as the owner is
   * FontFactory.
   *
   * @param dev Device object off of which to create the font.
   *
   * @return
   */
  public static Font getFont(Device dev, int size, int style) {
    return getFont(dev, size, style, defaultName);
  }

  /**
   * Gives you the default font with the specified size and style. Clients
   * should not dispose the font returned by this function call, as the owner is
   * FontFactory.
   *
   * @return
   */
  public static Font getFont(int size, int style) {
    return getFont(Display.getCurrent(), size, style);
  }

  /**
   * Gives you a font with the specified size, specified style, and specified font. Clients
   * should not dispose the font returned by this function call, as the owner is
   * FontFactory.
   * @param dev Device object off of which to create the font.
   * @return
   */
  public static Font getFont(Device dev, int size, int style,
                             String name) {
    Font font;
    FontData data = new FontData(name, size, style);
    if (fontMap.containsKey(data)) {
      return fontMap.get(data);
    }

    int renderSize = fontPoint(dev, size);

    try {
      font = new Font(dev, name, renderSize, style);
    } catch (Exception e) {
      try {
        font = new Font(dev, defaultName, renderSize, SWT.NORMAL);
      } catch (Exception f) {
        font = new Font(dev, defaultName, fontPoint(dev, DEFAULT_SIZE), SWT.NORMAL);
      }
    }

    fontMap.put(data, font);
    return font;
  }

  /**
   * Disposes all the fonts and clears the internal storage of fonts. Does not do ref counting,
   * so use this with care.
   */
  public static void disposeAll() {
    for (Font font: fontMap.values()) {
      font.dispose();
    }
    fontMap.clear();
  }
}

