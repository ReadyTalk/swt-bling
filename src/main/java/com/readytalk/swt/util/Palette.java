package com.readytalk.swt.util;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 * The purpose of this class is to capture a set of background colors, fonts, what have you
 * and apply it to multiple components.
 */
public class Palette {
  private Color backgroundColor;
  private Color[] backgroundGradientColors;
  private int[] backgroundGradientPercents;
  private boolean backgroundGradientVertical;
  private Image backgroundImage;

  private Color foregroundColor;

  private Color overlayColor;

  private Font font;

  public void setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public void setBackgroundGradient(Color[] backgroundGradientColors, int[] backgroundGradientPercents) {
    this.backgroundGradientColors = backgroundGradientColors;
    this.backgroundGradientPercents = backgroundGradientPercents;
  }

  public void setBackgroundGradient(Color[] backgroundGradientColors, int[] backgroundGradientPercents, boolean backgroundGradientVertical) {
    this.backgroundGradientColors = backgroundGradientColors;
    this.backgroundGradientPercents = backgroundGradientPercents;
    this.backgroundGradientVertical = backgroundGradientVertical;
  }

  public void setBackgroundImage(Image backgroundImage) {
    this.backgroundImage = backgroundImage;
  }

  public void setForegroundColor(Color foregroundColor) {
    this.foregroundColor = foregroundColor;
  }

  public void setFont(Font font) {
    this.font = font;
  }

  public void setOverlayColor(Color overlayColor) {
    this.overlayColor = overlayColor;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public boolean isGradientBackgroundAvailable() {
    return backgroundGradientColors != null && backgroundGradientPercents != null;
  }

  public Color[] getBackgroundGradientColors() {
    return backgroundGradientColors;
  }

  public int[] getBackgroundGradientPercents() {
    return backgroundGradientPercents;
  }

  public boolean isBackgroundGradientVertical() {
    return backgroundGradientVertical;
  }

  public Image getBackgroundImage() {
    return backgroundImage;
  }

  public Color getForegroundColor() {
    return foregroundColor;
  }

  public Font getFont() {
    return font;
  }

  public Color getOverlayColor() {
    // if we don't specify an overlay, the foreground color is probably okay
    if(overlayColor == null) {
      return foregroundColor;
    }
    return overlayColor;
  }
}
