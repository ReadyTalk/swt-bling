package com.readytalk.swt.util;

import com.google.common.annotations.VisibleForTesting;
import lombok.extern.java.Log;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * FontFactory is meant as a central repository for getting colors so that we don't allocate
 * duplicates everywhere; this can greatly decrease the number of fonts we allocate.
 */
@Log
public enum FontFactory {;
  private static final String WINDOWS = "Windows";
  private static final String SANS_SERIF = "Sans-Serif";
  private static final String MAC = "Mac";
  private static final String LUCIDA_GRANDE = "Lucida-Grande";
  private static final String VERA = "Vera";
  private static final String OS_NAME = "os.name";
  private static final int DEFAULT_STYLE = SWT.NORMAL;
  private static final int FONT_DPI = 72;

  private static String defaultName;
  private static String defaultIconName;
  private static int defaultSize;
  private static int defaultIconSize;

  static final Map<FontData, Font> fontMap = new HashMap<FontData, Font>();

  static {
    setDefaults();
  }

  @VisibleForTesting
  static void setDefaults() {
    String osName = System.getProperty(OS_NAME);
    if (osName == null || osName.length() == 0 || osName.startsWith(WINDOWS)) {
      defaultName = SANS_SERIF;
    } else if (osName.startsWith(MAC)) {
      defaultName = LUCIDA_GRANDE;
    } else {
      defaultName = VERA;
    }

    defaultIconName = "FontAwesome";

    defaultSize = 10;
    defaultIconSize = 25;
  }

  /**
   * Sets the font name to be used by default when getting a font when no name is provided
   *
   * @param name
   */
  public static void setDefaultFontName(String name) {
    defaultName = name;
  }

  /**
   * Sets the icon font name to be used by default when getting an icon font
   *
   * @param name
   */
  public static void setDefaultIconFontName(String name) {
    defaultIconName = name;
  }

  /**
   * Sets the font size to be used by default when getting a font when no size is provided
   *
   * @param size
   */
  public static void setDefaultFontSize(int size) {
    defaultSize = size;
  }

  /**
   * Sets the icon font size to be used by default when getting an icon font when no size is provided
   *
   * @param size
   */
  public static void setDefaultIconFontSize(int size) {
    defaultIconSize = size;
  }

  /**
   * Returns an SWT Font to be used for scalable font based icons at the default size and style
   *
   * @return
   */
  public static Font getIconFont() {
    return getIconFont(defaultIconSize);
  }

  /**
   * Returns an SWT Font to be used for scalable font based icons at the specified size and default style
   *
   * @param size
   * @return
   */
  public static Font getIconFont(int size) {
    return getFont(Display.getCurrent(), size, DEFAULT_STYLE, defaultIconName) ;
  }

  /**
   * Returns an SWT Font with bold style and default size and font name
   *
   * @return
   */
  public static Font getBold() {
    return getFont(defaultSize, SWT.BOLD);
  }

  /**
   * Returns an SWT Font with the specified size, bold style, and default font name
   *
   * @param size
   * @return
   */
  public static Font getBold(int size) {
    return getFont(size, SWT.BOLD);
  }

  /**
   * Returns an SWT Font with italic style and default size and font name
   *
   * @return
   */
  public static Font getItalic() {
    return getFont(defaultSize, SWT.ITALIC);
  }

  /**
   * Returns an SWT Font with the specified size, italic style, and default font name
   *
   * @param size
   * @return
   */
  public static Font getItalic(int size) {
    return getFont(size, SWT.ITALIC);
  }

  /**
   * Gives you a font with the default size, normal style, and default font. Clients
   * should not dispose the font returned by this function call, as the owner is
   * FontFactory.
   *
   * @return Font
   */
  public static Font getFont() {
    return getFont(Display.getCurrent(), defaultSize, DEFAULT_STYLE, defaultName);
  }

  /**
   * Gives you a font with the default size, normal style, and default font. Clients
   * should not dispose the font returned by this function call, as the owner is
   * FontFactory.
   *
   * @param dev Device object off of which to create the font.
   * @return Font
   */
  public static Font getFont(final Device dev) {
    return getFont(dev, defaultSize, DEFAULT_STYLE, defaultName);
  }

  /**
   * Gives you a font with the specified size, normal style, and default font. Clients
   * should not dispose the font returned by this function call, as the owner is
   * FontFactory.
   *
   * @param size font point size
   * @return Font
   */
  public static Font getFont(final int size) {
    return getFont(Display.getCurrent(), size, DEFAULT_STYLE, defaultName);
  }

  /**
   * Gives you a font with the specified size, normal style, and default font. Clients
   * should not dispose the font returned by this function call, as the owner is
   * FontFactory.
   *
   * @param dev Device object off of which to create the font.
   * @param size font point size
   * @return Font
   */
  public static Font getFont(final Device dev, final int size) {
    return getFont(dev, size, DEFAULT_STYLE, defaultName);
  }

  /**
   * Gives you a font with the specified size, specified style, and default font. Clients
   * should not dispose the font returned by this function call, as the owner is
   * FontFactory.
   *
   * @param dev Device object off of which to create the font.
   *
   * @return Font
   */
  public static Font getFont(final Device dev, final int size, final int style) {
    return getFont(dev, size, style, defaultName);
  }

  /**
   * Gives you the default font with the specified size and style. Clients
   * should not dispose the font returned by this function call, as the owner is
   * FontFactory.
   *
   * @return Font
   */
  public static Font getFont(final int size, final int style) {
    return getFont(Display.getCurrent(), size, style);
  }

  // converts a "size" metric to the nearest system-dependent
  // "point" metric - based on DPIs
  private static int fontPoint(final Device dev, final int size) {
    int systemDPI = dev.getDPI().y;
    double ratio = (double) FONT_DPI / systemDPI;
    return (int) (ratio * size);
  }

  /**
   * Gives you a font with the specified size, specified style, and specified font. Clients
   * should not dispose the font returned by this function call, as the owner is
   * FontFactory.
   *
   * @param dev Device object off of which to create the font.
   * @return Font
   */
  public static Font getFont(final Device dev, final int size, final int style, final String name) {
    Font font = null;
    int renderSize = fontPoint(dev, size);
    final FontData data = new FontData(name, renderSize, style);

    if (fontMap.containsKey(data)) {
      font = fontMap.get(data);
    }

    if (font == null || font.isDisposed()) {
      try {
        // The requested font was not in the cache so attempt to create it
        font = new Font(dev, data);
      } catch (Exception e) {
        try {
          // There was a problem creating the requested font, so attempt to fall back to the default name and style
          log.log(Level.WARNING, "Unable to create requested font", e);
          font = new Font(dev, defaultName, renderSize, DEFAULT_STYLE);
        } catch (Exception f) {
          // There was still a problem so fall back to the default name, style, and size
          log.log(Level.WARNING, "Still unable to create requested font", e);
          font = new Font(dev, defaultName, defaultSize, DEFAULT_STYLE);
        }
      }

      fontMap.put(data, font);
    }

    return font;
  }

  /**
   * Disposes all the fonts and clears the internal storage of fonts. Does not do ref counting,
   * so use this with care.
   */
  public static void disposeAll() {
    for (Font font : fontMap.values()) {
      if (font != null && !font.isDisposed()) {
        font.dispose();
      }
    }

    fontMap.clear();
  }
}
