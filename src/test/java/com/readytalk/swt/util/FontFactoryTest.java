package com.readytalk.swt.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FontFactoryTest {
  private static final String FAKE_FONT_NAME = "Super Ultra Comic Sans Plus Plus Monospace";
  private static final String REAL_FONT_NAME = "Courier";
  private static final int SIZE = 19;
  private static final int STYLE = SWT.BOLD;

  @After
  public void cleanup() {
    FontFactory.disposeAll();
    FontFactory.setDefaults();
  }

  @Test
  public void getFont_specifyNameSizeStyle_returnFont() {
    final Font font = FontFactory.getFont(Display.getCurrent(), SIZE, STYLE, REAL_FONT_NAME);

    assertTrue(isFontDataMatch(font, REAL_FONT_NAME, SIZE, STYLE));
  }

  @Test
  public void getFont_specifySizeStyle_returnFont() {
    final Font font = FontFactory.getFont(Display.getCurrent(), SIZE, STYLE);

    assertTrue(isFontDataMatch(font, SIZE, STYLE));
  }

  @Test
  public void getFont_bogusFontName_returnFontWithSystemDefaultName() {
    final Font font = FontFactory.getFont(Display.getCurrent(), SIZE, STYLE, FAKE_FONT_NAME);

    final GC gc = new GC(Display.getCurrent());
    final Font systemFont = gc.getFont();
    gc.dispose();

    assertTrue(isFontDataMatch(font, systemFont.getFontData()[0].getName(), SIZE, STYLE));
  }

  @Test
  public void getBold_returnBoldStyleFont() {
    final Font font = FontFactory.getBold(SIZE);

    assertTrue(isFontDataMatch(font, SIZE, SWT.BOLD));
  }

  @Test
  public void getItalic_returnItalicStyleFont() {
    final Font font = FontFactory.getItalic(SIZE);

    assertTrue(isFontDataMatch(font, SIZE, SWT.ITALIC));
  }

  @Test
  public void getIconFont_returnIconFontWithDefaultSize() {
    final int iconFontSize = 42;
    FontFactory.setDefaultIconFontName(REAL_FONT_NAME);
    FontFactory.setDefaultIconFontSize(iconFontSize);

    final Font font = FontFactory.getIconFont();

    assertTrue(isFontDataMatch(font, REAL_FONT_NAME, iconFontSize, SWT.NORMAL));
  }

  @Test
  public void getIconFont_specifySize_returnIconFontWithSpecifiedSize() {
    FontFactory.setDefaultIconFontName(REAL_FONT_NAME);

    final Font font = FontFactory.getIconFont(SIZE);

    assertTrue(isFontDataMatch(font, REAL_FONT_NAME, SIZE, SWT.NORMAL));
  }

  @Test
  public void disposeAll_fontCacheNotEmpty_fontCacheEmpty() {
    FontFactory.getFont(Display.getCurrent(), SIZE, STYLE, REAL_FONT_NAME);

    assertFalse(FontFactory.fontMap.isEmpty());

    FontFactory.disposeAll();

    assertTrue(FontFactory.fontMap.isEmpty());
  }

  private boolean isFontDataMatch(Font font, String name, int size, int style) {
    final FontData fontData = font.getFontData()[0];
    return name.equals(fontData.getName()) && size == fontData.getHeight() && style == fontData.getStyle();
  }

  private boolean isFontDataMatch(Font font, int size, int style) {
    final FontData fontData = font.getFontData()[0];
    return size == fontData.getHeight() && style == fontData.getStyle();
  }
}
