package com.readytalk.swt.widgets.buttons.texticon.style;

import lombok.Getter;

/**
 * A container class to hold the styles for each state of a TextIconButton
 */
@Getter
public class ButtonStyles {
  private final ButtonStyle normal;
  private final ButtonStyle hover;
  private final ButtonStyle disabled;
  private final ButtonStyle selected;
  private final ButtonStyle selectedHovered;
  private final ButtonStyle clicked;

  public ButtonStyles(ButtonStyle normal) {
    this(normal, normal, normal, normal, normal, normal);
  }

  public ButtonStyles(ButtonStyle normal, ButtonStyle hover, ButtonStyle disabled, ButtonStyle selected,
      ButtonStyle selectedHovered, ButtonStyle clicked) {
    this.normal = normal;
    this.hover = hover;
    this.disabled = disabled;
    this.selected = selected;
    this.selectedHovered = selectedHovered;
    this.clicked = clicked;
  }

  /**
   * A builder for TextIconButton ButtonStyles to set specific styles, or if a particular style isn't set it will
   * default to the normal style
   */
  public static class ButtonStylesBuilder {
    private ButtonStyle normal;
    private ButtonStyle hover;
    private ButtonStyle disabled;
    private ButtonStyle selected;
    private ButtonStyle selectedHovered;
    private ButtonStyle clicked;

    public ButtonStylesBuilder setNormal(ButtonStyle normal) {
      this.normal = normal;
      return this;
    }

    public ButtonStylesBuilder setHover(ButtonStyle hover) {
      this.hover = hover;
      return this;
    }

    public ButtonStylesBuilder setDisabled(ButtonStyle disabled) {
      this.disabled = disabled;
      return this;
    }

    public ButtonStylesBuilder setSelected(ButtonStyle selected) {
      this.selected = selected;
      return this;
    }

    public ButtonStylesBuilder setSelectedHovered(ButtonStyle selectedHovered) {
      this.selectedHovered = selectedHovered;
      return this;
    }

    public ButtonStylesBuilder setClicked(ButtonStyle clicked) {
      this.clicked = clicked;
      return this;
    }

    public ButtonStyles build() {
      if (normal == null) {
        throw new IllegalArgumentException("Normal style is required");
      }

      // If a particular style is missing use the normal style in its place
      if (hover == null) {
        hover = normal;
      }

      if (disabled == null) {
        disabled = normal;
      }

      if (selected == null) {
        selected = normal;
      }

      if (selectedHovered == null) {
        selectedHovered = normal;
      }

      if (clicked == null) {
        clicked = normal;
      }

      return new ButtonStyles(normal, hover, disabled, selected, selectedHovered, clicked);
    }
  }
}
