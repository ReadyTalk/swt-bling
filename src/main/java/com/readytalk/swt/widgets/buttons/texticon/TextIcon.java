package com.readytalk.swt.widgets.buttons.texticon;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An abstraction for a scalable font icon using a library such as FontAwesome
 */
@ToString
@EqualsAndHashCode
@Getter
public class TextIcon {
  private final String text;
  private final int fontSize;
  private final String colorLabel;
  private final int xOffset;
  private final int yOffset;

  /**
   * Returns an instance of TextIcon with the x and y offset set to 0
   *
   * @param text
   * @param fontSize
   * @param colorLabel
   */
  public TextIcon(String text, int fontSize, String colorLabel) {
    this(text, fontSize, colorLabel, 0, 0);
  }

  /**
   * Returns an instance of TextIcon with the x and y offset set to the specified values
   *
   * @param text
   * @param fontSize
   * @param colorLabel
   * @param xOffset
   * @param yOffset
   */
  public TextIcon(String text, int fontSize, String colorLabel, int xOffset, int yOffset) {
    this.text = text;
    this.fontSize = fontSize;
    this.colorLabel = colorLabel;
    this.xOffset = xOffset;
    this.yOffset = yOffset;
  }
}
