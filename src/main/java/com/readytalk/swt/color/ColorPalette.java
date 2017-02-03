package com.readytalk.swt.color;

import com.google.common.annotations.VisibleForTesting;
import com.readytalk.swt.util.ColorFactory;
import lombok.extern.java.Log;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;

/**
 * An abstraction on top of the ColorFactory to provide a small set of colors to allow for a consistent client color
 * scheme and caching of colors with effects.
 */
@Log
public class ColorPalette {
  @VisibleForTesting
  final Map<String, RGB> colorMap = new HashMap<String, RGB>();

  /**
   * Clear the currently loaded map of colors
   */
  public void clear() {
    colorMap.clear();
  }

  /**
   * Loads the ColorPalette resource bundle and reads in the color labels and values into a map.
   * <p>
   * The hex color values from the ColorPalette properties file are converted to SWT RGB objects as the values are
   * read into the map. Any invalid color values will be logged and skipped over.
   */
  public void loadColorsFromProperties(String propertiesName) {
    loadColorsFromProperties(ResourceBundle.getBundle(propertiesName));
  }

  @VisibleForTesting
  void loadColorsFromProperties(ResourceBundle colorPaletteBundle) {
    String colorLabel, colorHexValue;

    for (Enumeration<String> keys = colorPaletteBundle.getKeys(); keys.hasMoreElements(); ) {
      colorLabel = keys.nextElement();
      colorHexValue = colorPaletteBundle.getString(colorLabel);

      loadColor(colorLabel, colorHexValue);
    }
  }

  /**
   * Manually load a color mapping into the palette. If a mapping already exists for the label it will be overwritten.
   *
   * @param colorLabel
   * @param colorHexValue
   */
  public void loadColor(String colorLabel, String colorHexValue) {
    try {
      colorMap.put(colorLabel, ColorUtility.hexToRgb(colorHexValue));
    } catch (NumberFormatException e) {
      log.log(Level.WARNING, "Unable to read color value for " + colorLabel, e);
    }
  }

  /**
   * Manually set a color mapping
   *
   * @param colorLabel
   * @param rgb
   */
  public void setColor(String colorLabel, RGB rgb) {
    // Clear any corresponding cached colors with effects applied from the base color
    List<String> colorsToRemove = new LinkedList<String>();
    for (String key : colorMap.keySet()) {
      if (key.startsWith(colorLabel + "_")) {
        colorsToRemove.add(key);
      }

    }

    for (String colorToRemove : colorsToRemove) {
      colorMap.remove(colorToRemove);
    }

    colorMap.put(colorLabel, rgb);
  }

  /**
   * Returns an SWT Color object created with the RGB values specified for the color label in the current palette.
   *
   * @param colorLabelFromPalette
   * @return
   */
  public Color getColor(String colorLabelFromPalette) {
    final RGB rgb = colorMap.get(colorLabelFromPalette);
    return ColorFactory.getColor(rgb.red, rgb.green, rgb.blue);
  }

  /**
   * Returns an SWT color object created with
   *
   * @param colorLabel
   * @param colorEffect
   * @return
   */
  public Color getColorWithEffect(String colorLabel, ColorEffect colorEffect) {
    RGB rgb = colorMap.get(colorLabel + "_" + colorEffect);

    // First check for a cached color with the effect already applied
    if (rgb == null) {
      rgb = ColorUtility.applyEffect(colorEffect, colorMap.get(colorLabel));
      colorMap.put(colorLabel + "_" + colorEffect, rgb);
    }

    return ColorFactory.getColor(rgb.red, rgb.green, rgb.blue);
  }
}
