package com.readytalk.swt.font;

import org.eclipse.swt.widgets.Display;

/**
 * A utility class for loading custom fonts into SWT
 */
public class FontLoader {
  private final Display display;

  /**
   * Creates an instance of FontLoader with the specified Display
   *
   * @param display
   */
  public FontLoader(Display display) {
    this.display = display;
  }

  /**
   * Load a font from a resource located at fontFileResourcePath into SWT
   *
   * @param fontFileResourcePath
   * @return true if the font was successfully loaded, otherwise false
   */
  public boolean loadFontResource(String fontFileResourcePath) {
    return display.loadFont(FontLoader.class.getClassLoader().getResource(fontFileResourcePath).getPath());
  }

  /**
   * Load a font from a file located at fontFilePath into SWT
   *
   * @param fontFilePath
   * @return true if the font was successfully loaded, otherwise false
   */
  public boolean loadFontFile(String fontFilePath) {
    return display.loadFont(fontFilePath);
  }
}
