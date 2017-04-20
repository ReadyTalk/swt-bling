package com.readytalk.swt.widgets.buttons.texticon.style;

import com.google.common.collect.Sets;
import lombok.Getter;
import org.eclipse.swt.graphics.Font;

import java.util.Set;

/**
 * Encapsulates the style parameters for a TextIconButtonRenderer
 */
@Getter
public class ButtonStyle {
  private static final int CORNER_RADIUS = 8;
  private static final int MARGIN = 10;
  private static final int SPACING = 5;
  private static final int BORDER_WIDTH = 2;

  private final Font font;
  private final Font iconFont;
  private final String backgroundColor;
  private final String borderColor;
  private final String fontColor;
  private final String iconFontColor;
  private final int radius;
  private final int margin;
  private final int spacing;
  private final int borderWidth;
  private final ButtonColorEffect buttonColorEffect;
  private final Set<ButtonEffect> buttonEffects;

  private ButtonStyle(Font font, Font iconFont, String backgroundColor, String borderColor, String fontColor,
      String iconFontColor, int radius, int margin, int spacing, int borderWidth, ButtonColorEffect buttonColorEffect,
      Set<ButtonEffect> buttonEffects) {
    // This constructor is private to hide the shame of this ridiculous list of parameters and the builder should be
    // used instead
    this.font = font;
    this.iconFont = iconFont;
    this.backgroundColor = backgroundColor;
    this.borderColor = borderColor;
    this.fontColor = fontColor;
    this.iconFontColor = iconFontColor;
    this.radius = radius;
    this.margin = margin;
    this.spacing = spacing;
    this.borderWidth = borderWidth;
    this.buttonColorEffect = buttonColorEffect;
    this.buttonEffects = Sets.immutableEnumSet(buttonEffects);
  }

  /**
   * A builder for a single ButtonStyle
   */
  public static class ButtonStyleBuilder {
    private Font font;
    private Font iconFont;
    private String backgroundColor;
    private String borderColor;
    private String fontColor;
    private String iconFontColor;
    private int radius = CORNER_RADIUS;
    private int margin = MARGIN;
    private int spacing = SPACING;
    private int borderWidth = BORDER_WIDTH;
    private ButtonColorEffect buttonColorEffect = ButtonColorEffect.NONE;
    private Set<ButtonEffect> buttonEffects = Sets.newHashSet();

    /**
     * Set a style upon which to base a new build for a style in order to easily tweak only a few parameters
     * @param buttonStyle
     * @return
     */
    public ButtonStyleBuilder setBaseStyle(ButtonStyle buttonStyle) {
      setFont(buttonStyle.getFont());
      setIconFont(buttonStyle.getIconFont());
      setBackgroundColor(buttonStyle.getBackgroundColor());
      setBorderColor(buttonStyle.getBorderColor());
      setFontColor(buttonStyle.getFontColor());
      setIconFontColor(buttonStyle.getIconFontColor());
      setRadius(buttonStyle.getRadius());
      setMargin(buttonStyle.getMargin());
      setSpacing(buttonStyle.getSpacing());
      setBorderWidth(buttonStyle.getBorderWidth());
      setButtonColorEffect(buttonStyle.getButtonColorEffect());
      setButtonEffects(buttonStyle.getButtonEffects());
      return this;
    }

    public ButtonStyleBuilder setFont(Font font) {
      this.font = font;
      return this;
    }

    public ButtonStyleBuilder setIconFont(Font iconFont) {
      this.iconFont = iconFont;
      return this;
    }

    public ButtonStyleBuilder setBackgroundColor(String backgroundColor) {
      this.backgroundColor = backgroundColor;
      return this;
    }

    public ButtonStyleBuilder setBorderColor(String borderColor) {
      this.borderColor = borderColor;
      return this;
    }

    public ButtonStyleBuilder setFontColor(String fontColor) {
      this.fontColor = fontColor;
      return this;
    }

    public ButtonStyleBuilder setIconFontColor(String iconFontColor) {
      this.iconFontColor = iconFontColor;
      return this;
    }

    public ButtonStyleBuilder setRadius(int radius) {
      this.radius = radius;
      return this;
    }

    public ButtonStyleBuilder setMargin(int margin) {
      this.margin = margin;
      return this;
    }

    public ButtonStyleBuilder setSpacing(int spacing) {
      this.spacing = spacing;
      return this;
    }

    public ButtonStyleBuilder setBorderWidth(int borderWidth) {
      this.borderWidth = borderWidth;
      return this;
    }

    public ButtonStyleBuilder setButtonColorEffect(ButtonColorEffect buttonColorEffect) {
      this.buttonColorEffect = buttonColorEffect;
      return this;
    }

    public ButtonStyleBuilder setButtonEffects(Set<ButtonEffect> buttonEffects) {
      // Additional effects are added to the member to account for the case where effects are initially set from
      // a base style and additional effects are added

      this.buttonEffects = Sets.union(this.buttonEffects, buttonEffects);
      return this;
    }

    public ButtonStyle build() {
      // TODO: check for required values
      // TODO: need to ensure only one of ButtonEffect.ROW or ButtonEffect.COLUMN is specified

      return new ButtonStyle(font, iconFont, backgroundColor, borderColor, fontColor, iconFontColor, radius, margin,
          spacing, borderWidth, buttonColorEffect, buttonEffects);
    }
  }
}
