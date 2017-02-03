package com.readytalk.swt.color;

import lombok.extern.java.Log;
import org.eclipse.swt.graphics.RGB;

/**
 * A set of utilities for converting colors between representations and modifying various aspects of a color.
 */
@Log
public enum ColorUtility {;
  private static final int COLOR_SHIFT_INTERVAL = 0x1F;
  private static final int SATURATION_SHIFT_INTERVAL = 20;

  private static final RGB BLACK_RGB = new RGB(0, 0, 0);
  private static final RGB WHITE_RGB = new RGB(255, 255, 255);

  /**
   * Given a color value in the format "#RRGGBB", returns an RGB object with the parsed values for red, green, and
   * blue colors.
   *
   * @param hexColor
   * @return
   */
  public static RGB hexToRgb(String hexColor) {
    // Strip off the hash, if present
    String strippedHexColor = hexColor.substring(hexColor.indexOf("#") + 1);

    if (strippedHexColor.length() == 3) {
      // Shorthand hex representation, ex: #123 = #112233
      StringBuilder builder = new StringBuilder(6);

      int index = 0;
      for (char c : strippedHexColor.toCharArray()) {
        builder.insert(index++, c);
        builder.insert(index++, c);
      }

      strippedHexColor = builder.toString();
    }

    // Pad the color value with zeroes to allow for shorter color hex value specification
    while (strippedHexColor.length() < 6) {
      strippedHexColor = strippedHexColor + "0";
    }

    int colorValue = Integer.parseInt(strippedHexColor, 16);
    int red = (colorValue >> 16) & 255;
    int green = (colorValue >> 8) & 255;
    int blue = colorValue & 255;

    return new RGB(red, green, blue);
  }

  /**
   * Convert from RGB to HSB
   * <p>
   * Adapted from https://en.wikipedia.org/wiki/HSL_and_HSV
   *
   * @param rgb
   * @return
   */
  public static HSB rgbToHsb(RGB rgb) {
    final float red = rgb.red / 255.0f;
    final float green = rgb.green / 255.0f;
    final float blue = rgb.blue / 255.0f;

    final float max = maximum(red, green, blue);
    final float min = minimum(red, green, blue);
    final float chroma = max - min;

    final float brightness = max;

    float hue = 0;
    float saturation = 0;

    // If the min and max are equal then this is a gray color and there's no chroma and only brightness is set
    if (chroma != 0) {
      if (max == red) {
        hue = 60 * (((green - blue) / chroma) % 6);
      } else if (max == green) {
        hue = 60 * (((blue - red) / chroma) + 2);
      } else if (max == blue) {
        hue = 60 * (((red - green) / chroma) + 4);
      }

      // The mod operator returns a negative value for a negative dividend so we correct negative angle values
      // for hue to positive for consistency
      if (hue < 0) {
        hue += 360;
      }

      saturation = chroma / max;
    }

    return new HSB(hue, saturation, brightness);
  }

  /**
   * Convert from HSB to RGB
   * <p>
   * Adapted from https://en.wikipedia.org/wiki/HSL_and_HSV
   *
   * @param hsb
   * @return
   */
  public static RGB hsbToRgb(HSB hsb) {
    final float chroma = hsb.brightness * hsb.saturation;
    final float brightnessMatch = hsb.brightness - chroma;

    final float h = hsb.hue / 60;
    final float x = chroma * (1 - Math.abs(h % 2 - 1));

    float red = 0;
    float green = 0;
    float blue = 0;

    switch ((int) h) {
    case 0:
      red = chroma;
      green = x;
      blue = 0;
      break;
    case 1:
      red = x;
      green = chroma;
      blue = 0;
      break;
    case 2:
      red = 0;
      green = chroma;
      blue = x;
      break;
    case 3:
      red = 0;
      green = x;
      blue = chroma;
      break;
    case 4:
      red = x;
      green = 0;
      blue = chroma;
      break;
    case 5:
      red = chroma;
      green = 0;
      blue = x;
      break;
    }

    red = Math.round((red + brightnessMatch) * 255.0f);
    green = Math.round((green + brightnessMatch) * 255.0f);
    blue = Math.round((blue + brightnessMatch) * 255.0f);

    return new RGB((int) red, (int) green, (int) blue);
  }

  private static float minimum(float x, float y, float z) {
    float min = Math.min(x, y);
    return Math.min(min, z);
  }

  private static float maximum(float x, float y, float z) {
    float max = Math.max(x, y);
    return Math.max(max, z);
  }

  /**
   * Returns a new RGB with the specified ColorEffect applied
   *
   * @param colorEffect
   * @param rgb
   * @return
   */
  public static RGB applyEffect(ColorEffect colorEffect, RGB rgb) {
    RGB modifiedRGB = rgb;

    switch (colorEffect) {
    case NONE:
      break;
    case SATURATE:
      modifiedRGB = saturate(rgb);
      break;
    case DESATURATE:
      modifiedRGB = desaturate(rgb);
      break;
    case BRIGHTEN:
      modifiedRGB = brighten(rgb);
      break;
    case DARKEN:
      modifiedRGB = darken(rgb);
      break;
    case SHIFT:
      modifiedRGB = shift(rgb);
      break;
    default:
      log.warning("Unknown ColorEffect: " + colorEffect);
      break;
    }

    return modifiedRGB;
  }

  public static RGB saturate(RGB rgb) {
    return adjustSaturation(rgb, SATURATION_SHIFT_INTERVAL);
  }

  public static RGB desaturate(RGB rgb) {
    return adjustSaturation(rgb, -SATURATION_SHIFT_INTERVAL);
  }

  public static RGB brighten(RGB rgb) {
    return adjust(rgb, COLOR_SHIFT_INTERVAL);
  }

  public static RGB darken(RGB rgb) {
    return adjust(rgb, -COLOR_SHIFT_INTERVAL);
  }

  /**
   * Returns a new color with a brighten effect applied if isDarkColor returns true for the specified color or a
   * darken effect otherwise.
   *
   * @param rgb
   * @return
   */
  public static RGB shift(RGB rgb) {
    int colorDistance, amount;

    if (isDarkColor(rgb)) {
      colorDistance = (int) colorDistance(rgb, BLACK_RGB);
      amount = -1 * (COLOR_SHIFT_INTERVAL + 0xF * (colorDistance / 222));
    } else {
      colorDistance = (int) colorDistance(rgb, WHITE_RGB);
      amount = COLOR_SHIFT_INTERVAL + 0xF * (colorDistance / 222);
    }

    return adjust(rgb, amount);
  }

  /**
   * Adds the specified amount to each of red, blue, and green fields of the RGB color
   *
   * @param rgb
   * @param amount
   * @return
   */
  public static RGB adjust(RGB rgb, int amount) {
    // TODO: need to check for clipping and reduce the amount
    int r = Math.min(Math.max(rgb.red + amount, 0), 255);
    int g = Math.min(Math.max(rgb.green + amount, 0), 255);
    int b = Math.min(Math.max(rgb.blue + amount, 0), 255);

    return new RGB(r, g, b);
  }

  public static RGB adjustSaturation(RGB rgb, int amount) {
    final HSB hsb = rgbToHsb(rgb);
    // TODO: if the color is gray then we need to adjust brightness rather than saturation
    final HSB adjustedHsb = new HSB(hsb.hue, Math.min(Math.max(0f, hsb.saturation + amount), 1f), hsb.brightness);

    return hsbToRgb(adjustedHsb);
  }

  /**
   * Returns true if the distance to true black is less than the distance to true white.
   *
   * @param rgb
   * @return
   */
  public static boolean isDarkColor(RGB rgb) {
    final double whiteDistance = colorDistance(rgb, WHITE_RGB);
    final double blackDistance = colorDistance(rgb, BLACK_RGB);

    return blackDistance < whiteDistance;
  }

  /**
   * Returns the distance between the two specified colors.
   *
   * @param rgb1
   * @param rgb2
   * @return
   */
  public static double colorDistance(RGB rgb1, RGB rgb2) {
    final int a = rgb2.red - rgb1.red;
    final int b = rgb2.green - rgb1.green;
    final int c = rgb2.blue - rgb1.blue;

    return Math.sqrt(a * a + b * b + c * c);
  }

  /**
   * Returns true if the specified color is a shade of gray.
   *
   * @param rgb
   * @return
   */
  public static boolean isGray(RGB rgb) {
    // TODO: need to add some tolerance here
    return (rgb.red == rgb.green && rgb.green == rgb.blue);
  }
}
