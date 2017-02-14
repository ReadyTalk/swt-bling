package com.readytalk.swt.widgets.buttons.texticon;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

/**
 * An interface for custom drawn buttons to indicate to a ButtonRenderer that the button should be painted in a
 * particular state and to obtain sizing information about the contents of the button being painted.
 */
public interface ButtonRenderer {
  /**
   * Render the button in the normal (default) state
   *
   * @param gc
   * @param button
   */
  void drawNormal(GC gc, TextIconButton button);

  /**
   * Render the button in a state indicating the user's mouse is hovered over the button
   *
   * @param gc
   * @param button
   */
  void drawHovered(GC gc, TextIconButton button);

  /**
   * Render the button in a state indicating that the button is disabled and cannot be used
   *
   * @param gc
   * @param button
   */
  void drawDisabled(GC gc, TextIconButton button);

  /**
   * Render the button in a state indicating that a toggle button is currently toggled or selected
   *
   * @param gc
   * @param button
   */
  void drawSelected(GC gc, TextIconButton button);

  /**
   * Render the button in a state indicating the user's mouse button has been pressed over the button
   *
   * @param gc
   * @param button
   */
  void drawSelectedHovered(GC gc, TextIconButton button);

  /**
   * Render the button in a state indicating a user's mouse button has been pressed over the button
   *
   * @param gc
   * @param parent
   */
  void drawClicked(GC gc, TextIconButton parent);

  /**
   * Given the current content of the button returns the preferred size
   *
   * @param button
   * @return
   */
  Point computeSize(TextIconButton button);
}
