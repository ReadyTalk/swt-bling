package com.readytalk.swt.color;

import org.eclipse.swt.graphics.RGB;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ColorUtilityTest {
  private static final double DELTA = .009;

  private static final Map<RGB, HSB> COLOR_MAP = new HashMap<RGB, HSB>() {{
    put(new RGB(0, 0, 0), new HSB(0, 0, 0));
    put(new RGB(255, 255, 255), new HSB(0, 0, 1));
    put(new RGB(128, 128, 128), new HSB(0, 0, .5f));
    put(new RGB(255, 0, 0), new HSB(0, 1, 1));
    put(new RGB(0, 255, 0), new HSB(120, 1, 1));
    put(new RGB(0, 0, 255), new HSB(240, 1, 1));
    put(new RGB(255, 255, 0), new HSB(60, 1, 1));
    put(new RGB(255, 0, 255), new HSB(300, 1, 1));
    put(new RGB(192, 192, 192), new HSB(0, 0, .753f));
    put(new RGB(0, 0, 128), new HSB(240, 1, .5f));
  }};

  @Test
  public void hexToRgb_hexColor_returnRgb() {
    RGB rgb = ColorUtility.hexToRgb("#ffffff");

    assertEquals(255, rgb.red);
    assertEquals(255, rgb.green);
    assertEquals(255, rgb.blue);
  }

  @Test
  public void hexToRgb_missingHash_returnRgb() {
    RGB rgb = ColorUtility.hexToRgb("ffffff");

    assertEquals(255, rgb.red);
    assertEquals(255, rgb.green);
    assertEquals(255, rgb.blue);
  }

  @Test
  public void hexToRgb_sevenCharacterHex_returnRgb() {
    RGB rgb = ColorUtility.hexToRgb("#fffffff");

    assertEquals(255, rgb.red);
    assertEquals(255, rgb.green);
    assertEquals(255, rgb.blue);
  }

  @Test
  public void hexToRgb_redishHex_returnRgb() {
    RGB rgb = ColorUtility.hexToRgb("#cc0000");

    assertEquals(204, rgb.red);
    assertEquals(0, rgb.green);
    assertEquals(0, rgb.blue);
  }

  @Test
  public void hexToRgb_shortHandHex_returnRgb() {
    RGB rgb = ColorUtility.hexToRgb("#ccc");

    assertEquals(204, rgb.red);
    assertEquals(204, rgb.green);
    assertEquals(204, rgb.blue);
  }

  @Test
  public void hexToRgb_shortHandHexWhite_returnRgb() {
    RGB rgb = ColorUtility.hexToRgb("#fff");

    assertEquals(255, rgb.red);
    assertEquals(255, rgb.green);
    assertEquals(255, rgb.blue);
  }

  @Test
  public void hexToRgb_redishHexShort_returnPaddedRgb() {
    RGB rgb = ColorUtility.hexToRgb("#cc");

    assertEquals(204, rgb.red);
    assertEquals(0, rgb.green);
    assertEquals(0, rgb.blue);
  }

  @Test
  public void hexToRgb_emptyString_returnPaddedRgb() {
    RGB rgb = ColorUtility.hexToRgb("");

    assertEquals(0, rgb.red);
    assertEquals(0, rgb.green);
    assertEquals(0, rgb.blue);
  }

  @Test(expected = NumberFormatException.class)
  public void hexToRgb_invalidHex_throwException() {
    ColorUtility.hexToRgb("Blah blah blah");
  }

  @Test(expected = NumberFormatException.class)
  public void hexToRgb_invalidHex2_throwException() {
    ColorUtility.hexToRgb("#gggggg");
  }

  @Test(expected = NullPointerException.class)
  public void hexToRgb_null_throwException() {
    ColorUtility.hexToRgb(null);
  }

  @Test
  public void rgbToHsb() {
    HSB expectedHsb, hsb;

    for (Map.Entry<RGB, HSB> entry : COLOR_MAP.entrySet()) {
      expectedHsb = entry.getValue();
      hsb = ColorUtility.rgbToHsb(entry.getKey());

      assertEquals(expectedHsb.hue, hsb.hue, DELTA);
      assertEquals(expectedHsb.saturation, hsb.saturation, DELTA);
      assertEquals(expectedHsb.brightness, hsb.brightness, DELTA);
    }
  }

  @Test
  public void hsbToRgb() {
    RGB expectedRgb, rgb;

    for (Map.Entry<RGB, HSB> entry : COLOR_MAP.entrySet()) {
      expectedRgb = entry.getKey();
      rgb = ColorUtility.hsbToRgb(entry.getValue());

      assertEquals(expectedRgb, rgb);
    }
  }
}
