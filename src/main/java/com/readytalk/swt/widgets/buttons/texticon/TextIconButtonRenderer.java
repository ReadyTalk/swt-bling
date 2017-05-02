package com.readytalk.swt.widgets.buttons.texticon;

import com.readytalk.swt.color.ColorEffect;
import com.readytalk.swt.util.FontFactory;
import com.readytalk.swt.widgets.buttons.texticon.style.ButtonColorEffect;
import com.readytalk.swt.widgets.buttons.texticon.style.ButtonEffect;
import com.readytalk.swt.widgets.buttons.texticon.style.ButtonStyle;
import lombok.extern.java.Log;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import java.util.Set;
import java.util.logging.Level;

/**
 * An implementation of ButtonRenderer responsible for drawing the contents of a TextIconButton in various states
 */
@Log
public class TextIconButtonRenderer implements ButtonRenderer {
  private static final int SHADOW_OFFSET = 1;

  protected TextIconButton button;
  protected ButtonStyle style;
  protected GC gc;

  private Point size;
  private Point textSize;
  private Point textIconSize;

  @Override
  public void drawNormal(GC gc, TextIconButton button) {
    style = button.getButtonStyles().getNormal();
    this.gc = gc;
    this.button = button;
    draw();
  }

  @Override
  public void drawHovered(GC gc, TextIconButton button) {
    style = button.getButtonStyles().getHover();
    this.gc = gc;
    this.button = button;
    draw();
  }

  @Override
  public void drawDisabled(GC gc, TextIconButton button) {
    style = button.getButtonStyles().getDisabled();
    this.gc = gc;
    this.button = button;
    draw();
  }

  @Override
  public void drawSelected(GC gc, TextIconButton button) {
    style = button.getButtonStyles().getSelected();
    this.gc = gc;
    this.button = button;
    draw();
  }

  @Override
  public void drawSelectedHovered(GC gc, TextIconButton button) {
    style = button.getButtonStyles().getSelectedHovered();
    this.gc = gc;
    this.button = button;
    draw();
  }

  @Override
  public void drawClicked(GC gc, TextIconButton button) {
    style = button.getButtonStyles().getClicked();
    this.gc = gc;
    this.button = button;
    draw();
  }

  protected void draw() {
    try {
      gc.setAdvanced(true);
      gc.setAntialias(SWT.ON);
      gc.setTextAntialias(SWT.ON);
      gc.setInterpolation(SWT.HIGH);
    } catch (SWTException e) {
      // TODO: log this once
      log.log(Level.WARNING, "Unable to set advanced drawing", e);
    }

    updateSizes();

    drawBackground();
    drawBorder();
    drawIconText();
    drawText();
  }

  private void updateSizes() {
    final Rectangle clientArea = button.getClientArea();

    textSize = computeTextSize();
    textIconSize = computeTextIconSize();

    // If we don't have a usable client area from the parent then use the computed minimum size
    size = (clientArea.width > 0 && clientArea.height > 0) ?
        new Point(clientArea.width, clientArea.height) :
        new Point(computeMinimumWidth(textSize.x, textIconSize.x), computeMinimumHeight(textSize.y, textIconSize.y));
  }

  protected void drawBackground() {
    gc.setBackground(button.getBackground());
    gc.fillRectangle(0, 0, size.x, size.y);
    gc.setBackground(getBackgroundColor(style.getBackgroundColor(), style.getButtonColorEffect()));
    gc.fillRoundRectangle(0, 0, size.x, size.y, style.getRadius(), style.getRadius());
  }

  protected void drawBorder() {
    if (isBorderStyle()) {
      gc.setForeground(getForegroundColor(style.getBorderColor(), style.getButtonColorEffect()));
      gc.setLineWidth(style.getBorderWidth());

      // The border coordinates are the center of the border width so we divide in half to get the center
      gc.drawRoundRectangle(style.getBorderWidth() / 2, style.getBorderWidth() / 2,
          size.x - Math.max(style.getBorderWidth() / 2, style.getBorderWidth()),
          size.y - Math.max(style.getBorderWidth() / 2, style.getBorderWidth()), style.getRadius(), style.getRadius());
    }
  }

  /**
   * Draw text icons at a location based on the style of the button
   */
  protected void drawIconText() {
    final int spacing = textSize.y > 0 ? style.getSpacing() : 0;

    Font font;
    Rectangle bounds;

    // First we draw the drop shadows for each TextIcon layer, if necessary
    if (isIconDropShadowStyle(style.getButtonEffects())) {
      for (TextIcon icon : button.getTextIcons()) {
        font = FontFactory.getIconFont(icon.getFontSize());
        bounds = getIconTextBounds(icon, size.x, size.y, textSize, spacing);

        drawTextAtLocation(gc, icon.getText(), font, getForegroundColor(icon.getColorLabel(), ButtonColorEffect.DARKEN),
            bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET);
      }
    }

    // TextIcons can be layered so we loop over them and draw them in place
    for (TextIcon icon : button.getTextIcons()) {
      font = FontFactory.getIconFont(icon.getFontSize());
      bounds = getIconTextBounds(icon, size.x, size.y, textSize, spacing);

      drawTextAtLocation(gc, icon.getText(), font,
          getForegroundColor(icon.getColorLabel(), style.getButtonColorEffect()), bounds.x, bounds.y);
    }
  }

  private Rectangle getIconTextBounds(TextIcon icon, int width, int height, Point textSize, int spacing) {
    // TODO: cache these locations if not changing
    final String text = icon.getText();
    final Font font = FontFactory.getIconFont(icon.getFontSize());
    final Point iconTextSize = getTextExtent(button, text, font);
    int x, y;

    if (isRowStyle()) {
      // Centered vertically, centered horizontally with the button text and spacing (ignores margin/border/etc.)
      y = height / 2 - iconTextSize.y / 2;
      int contentWidth = textSize.x + iconTextSize.x + spacing;
      x = width / 2 - contentWidth / 2;
    } else {
      // Centered horizontally, centered vertically with the button text and spacing (ignores margin/border/etc.)
      x = width / 2 - iconTextSize.x / 2;
      int contentHeight = textSize.y + iconTextSize.y + spacing;
      y = height / 2 - contentHeight / 2;
    }

    return new Rectangle(x + icon.getXOffset(), y + icon.getYOffset(), iconTextSize.x, iconTextSize.y);
  }

  /**
   * Draw text centered horizontally and centered vertically equal with the icon text but below
   */
  protected void drawText() {
    if (button.getText() != null) {
      final int spacing = textIconSize.y > 0 ? style.getSpacing() : 0;
      final Point contentSize = new Point(textSize.x + textIconSize.x + spacing, textSize.y + textIconSize.y + spacing);

      final Rectangle bounds = getTextBounds(size.x, size.y, contentSize.x, contentSize.y, textSize, textIconSize,
          spacing);

      if (isTextDropShadowStyle(style.getButtonEffects())) {
        drawTextAtLocation(gc, button.getText(), style.getFont(),
            getForegroundColor(style.getFontColor(), ButtonColorEffect.DESATURATE), bounds.x + SHADOW_OFFSET,
            bounds.y + SHADOW_OFFSET);
      }

      drawTextAtLocation(gc, button.getText(), style.getFont(),
          getForegroundColor(style.getFontColor(), style.getButtonColorEffect()), bounds.x, bounds.y);
    }
  }

  private Rectangle getTextBounds(int width, int height, int contentWidth, int contentHeight, Point textSize,
      Point iconTextSize, int spacing) {
    // TODO: cache these locations if not changing
    int x, y;

    if (isRowStyle()) {
      // Centered vertically, centered horizontally with the icon text and spacing
      x = width / 2 - contentWidth / 2 + iconTextSize.x + spacing;
      y = height / 2 - textSize.y / 2;
    } else {
      // Centered horizontally, centered vertically with the icon text and spacing
      x = width / 2 - textSize.x / 2;
      y = height / 2 - contentHeight / 2 + iconTextSize.y + spacing;
    }

    return new Rectangle(x, y, textSize.x, textSize.y);
  }

  protected static void drawTextAtLocation(GC gc, String text, Font font, Color color, int x, int y) {
    gc.setFont(font);
    gc.setForeground(color);
    gc.drawText(text, x, y, SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER);
  }

  /**
   * Returns the preferred size of the receiver
   *
   * @param button
   * @return
   */
  @Override
  public Point computeSize(TextIconButton button) {
    this.button = button;
    this.style = button.getCurrentStyle();

    updateSizes();

    return size;
  }

  private int computeMinimumHeight(int textHeight, int iconTextHeight) {
    int spacing = (textHeight > 0 && iconTextHeight > 0) ? style.getSpacing() : 0;
    int baseHeight = 2 * style.getMargin() + 2 * style.getBorderWidth() + spacing;
    int contentHeight = isRowStyle() ? Math.max(textHeight, iconTextHeight) : textHeight + iconTextHeight;
    return baseHeight + contentHeight;
  }

  private int computeMinimumWidth(int textWidth, int iconTextWidth) {
    int spacing = (textWidth > 0 && iconTextWidth > 0) ? style.getSpacing() : 0;
    int baseWidth = 2 * style.getBorderWidth() + 2 * style.getMargin();
    int contentWidth = isRowStyle() ? textWidth + iconTextWidth + spacing : Math.max(textWidth, iconTextWidth);
    return baseWidth + contentWidth;
  }

  private Point computeTextSize() {
    String text = button.getText();
    return (text != null) ? getTextExtent(button, text, style.getFont()) : new Point(0, 0);
  }

  /**
   * Returns the estimated size of the painted text icon.
   * <p>
   * Note: the current assumption is that the icon is centered and not offset
   *
   * @return
   */
  private Point computeTextIconSize() {
    Point max = new Point(0, 0);
    Point current;

    for (TextIcon icon : button.getTextIcons()) {
      current = getTextExtent(button, icon.getText(), FontFactory.getIconFont(icon.getFontSize()));

      if (current.x > max.x) {
        max.x = current.x;
      }

      if (current.y > max.y) {
        max.y = current.y;
      }
    }

    return max;
  }

  /**
   * Returns the dimensions of the specified text in the given font
   *
   * @param button
   * @param text
   * @param font
   * @return
   */
  protected Point getTextExtent(TextIconButton button, String text, Font font) {
    final GC gc = new GC(button);
    gc.setFont(font);

    final Point size = gc.textExtent(text, SWT.DRAW_DELIMITER);
    gc.dispose();

    return size;
  }

  private boolean isBorderStyle() {
    return style.getButtonEffects().contains(ButtonEffect.BORDER);
  }

  private boolean isRowStyle() {
    // The row button style is the default if none is specified
    return style.getButtonEffects().contains(ButtonEffect.ROW) || !style.getButtonEffects()
        .contains(ButtonEffect.COLUMN);
  }

  private boolean isColumnStyle() {
    return style.getButtonEffects().contains(ButtonEffect.COLUMN);
  }

  private boolean isTextDropShadowStyle(Set<ButtonEffect> buttonEffects) {
    return buttonEffects.contains(ButtonEffect.TEXT_DROP_SHADOW);
  }

  private boolean isIconDropShadowStyle(Set<ButtonEffect> buttonEffects) {
    return buttonEffects.contains(ButtonEffect.ICON_DROP_SHADOW);
  }

  private Color getBackgroundColor(String colorLabel, ButtonColorEffect buttonEffect) {
    ColorEffect colorEffect = ColorEffect.NONE;

    switch (buttonEffect) {
    case BRIGHTEN:
    case BRIGHTEN_BACKGROUND:
      colorEffect = ColorEffect.BRIGHTEN;
      break;
    case DARKEN:
    case DARKEN_BACKGROUND:
    case BRIGHTEN_FOREGROUND_DARKEN_BACKGROUND:
      colorEffect = ColorEffect.DARKEN;
      break;
    case DESATURATE:
    case DESATURATE_BACKGROUND:
      colorEffect = ColorEffect.DESATURATE;
      break;
    case SHIFT:
    case SHIFT_BACKGROUND:
      colorEffect = ColorEffect.SHIFT;
      break;
    case NONE:
    case BRIGHTEN_FOREGROUND:
    case DARKEN_FOREGROUND:
    case DESATURATE_FOREGROUND:
      // No background effect
      break;
    default:
      log.warning("Unknown button effect: " + buttonEffect);
    }

    return button.getColorPalette().getColorWithEffect(colorLabel, colorEffect);
  }

  private Color getForegroundColor(String colorLabel, ButtonColorEffect buttonEffect) {
    ColorEffect colorEffect = ColorEffect.NONE;

    switch (buttonEffect) {
    case BRIGHTEN:
    case BRIGHTEN_FOREGROUND:
    case BRIGHTEN_FOREGROUND_DARKEN_BACKGROUND:
      colorEffect = ColorEffect.BRIGHTEN;
      break;
    case DARKEN:
    case DARKEN_FOREGROUND:
      colorEffect = ColorEffect.DARKEN;
      break;
    case DESATURATE:
    case DESATURATE_FOREGROUND:
      colorEffect = ColorEffect.DESATURATE;
      break;
    case SHIFT:
    case SHIFT_FOREGROUND:
      colorEffect = ColorEffect.SHIFT;
      break;
    case NONE:
    case BRIGHTEN_BACKGROUND:
    case DARKEN_BACKGROUND:
      // No foreground effect
      break;
    default:
      log.warning("Unknown button effect: " + buttonEffect);
    }

    return button.getColorPalette().getColorWithEffect(colorLabel, colorEffect);
  }
}
