package com.readytalk.swt.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FontFactoryTest {
  private static final String FAKE_FONT_NAME = "Super Ultra Comic Sans Plus Plus Monospace";
  private static final String REAL_FONT_NAME = "Courier";
  private static final int SIZE = 19;
  private static final int STYLE = SWT.BOLD;

  private Shell shell;
  private Display display;

  @Before
  public void setup() throws Exception {
    if (Display.getCurrent() != null) Display.getCurrent().dispose();
    DeviceData data = new DeviceData();
    data.tracking = true;
    display = new Display(data);
    shell = new Shell(display);
  }

  @After
  public void tearDown() throws Exception {
    if (display != null) {
      display.dispose();
    }
    FontFactory.disposeAll();
    FontFactory.setDefaults();
  }

  @Test
  public void getFont_CreatesFont_OneObjectAddedToDeviceDataObjects() {
    int numberOfItemsBefore = shell.getDisplay().getDeviceData().objects.length;
    FontFactory.getFont(shell.getDisplay(), 12, SWT.NORMAL);
    Assert.assertEquals(numberOfItemsBefore+1, shell.getDisplay().getDeviceData().objects.length);
  }

  @Test
  public void getFont_CreateAndDisposeFont_NoAdditionalObjectInoDeviceDataObjects() {
    int numberOfItemsBefore = shell.getDisplay().getDeviceData().objects.length;
    FontFactory.getFont(shell.getDisplay());
    FontFactory.disposeAll();
    Assert.assertEquals(shell.getDisplay().getDeviceData().objects.length, numberOfItemsBefore);
  }

  @Test
  public void getFont_CreateTwoFonts_OnlyOneAdditionalObjectInoDeviceDataObjects() {
    FontFactory.getFont(shell.getDisplay());
    int numberOfItemsBefore = shell.getDisplay().getDeviceData().objects.length;
    FontFactory.getFont(shell.getDisplay());
    Assert.assertEquals(shell.getDisplay().getDeviceData().objects.length, numberOfItemsBefore);
  }

  @Test
  public void getFont_CreateTwoFonts_GetTheSameFontObjectBackFromFontMap() {
    Font a = FontFactory.getFont(shell.getDisplay());
    Font b =FontFactory.getFont(shell.getDisplay());
    Assert.assertEquals(System.identityHashCode(a), System.identityHashCode(b));
  }

  @Test
  public void getFont_CreateThreeDifferentFonts_FontMapSizeIs3() {
    FontFactory.getFont(shell.getDisplay(), 13);
    FontFactory.getFont(shell.getDisplay(), 14);
    FontFactory.getFont(shell.getDisplay(), 15);
    Assert.assertEquals(3, FontFactory.fontMap.size());
  }

  @Test
  public void getFont_CreateAndDisposeFont_FontMapSizeIs0() {
    FontFactory.getFont(shell.getDisplay(), 23);
    FontFactory.disposeAll();
    Assert.assertEquals(0, FontFactory.fontMap.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void getFont_CreateFontViaNegativeSize_ThrowsIllegalArgumentException() {
    FontFactory.getFont(shell.getDisplay(), -23);
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
