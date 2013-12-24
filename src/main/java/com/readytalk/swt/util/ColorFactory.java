package com.readytalk.swt.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * This is meant as a central repository for getting colors so that we don't allocate
 * duplicates everywhere, which greatly decreases the number of colors we allocate.
 * This currently doesn't do any reference counting or anything else, which we should
 * probably implement in the future.
 */
public class ColorFactory {

  static Map<RGB, Color> colorMap;

  static {
    colorMap = new HashMap<RGB, Color>();
  }

  // this should use the current display
  public static Color getColor(int red, int green, int blue) {
    return getColor(Display.getCurrent(), red, green, blue);
  }

  /**
   * Get a pre-generated Color object based on the passed in parameters.
   * This Color object may be shared, so it SHOULD NOT be disposed except through this
   * class' disposeAll() method. The owner of the Color object is ColorFactory, not the
   * caller of this method.
   * @param device Device object needed to create the color
   * @param red
   * @param green
   * @param blue
   * @return A possibly shared Color object with the specified components
   */
  public static Color getColor(Device device, int red, int green, int blue) {
    RGB rgb = new RGB(red, green, blue);
    return getColor(device, rgb);
  }

  /**
   * Get a pre-generated Color object based on the passed in parameters.
   * This Color object may be shared, so it SHOULD NOT be disposed except through this
   * class' disposeAll() method. The owner of the Color object is ColorFactory, not the
   * caller of this method.
   * @param device Device object needed to create the color
   * @param rgb
   * @return A possibly shared Color object with the specified rgb values
   */
  public static Color getColor(Device device, RGB rgb) {
    Color color = colorMap.get(rgb);
    if (color == null || color.isDisposed()) {
      color = buildColor(device, rgb);
      colorMap.put(rgb, color);
    }
    return color;
  }

  static Color buildColor(Device device, RGB rgb) {
    return buildColor(device, rgb);
  }

  /**
   * Disposes all the colors and clears the internal storage of colors. Does not do ref counting,
   * so use this with care.
   */
  public static void disposeAll() {
    for (Color color: colorMap.values()) {
      color.dispose();
    }
    colorMap.clear();
  }
}
