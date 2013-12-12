package com.readytalk.swt.widgets;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

/**
 * An interface provided to allow PopOver shells to be added to arbitrarily drawn components.
 * Typically, these elements are drawn directly to parent components via a GC and, therefore,
 * cannot utilize the constructor for Bubble that takes a Control (or descendant).
 */
public interface CustomElementDataProvider {
  /**
   * Returns the element that's being painted by the GC.
   * For example, if you add a PaintListener to a Canvas, this method should return the Canvas element.
   * @return The element being painted by the GC
   */
  public Control getPaintedElement();

  /**
   * Returns a Point describing the item to apply the PopOver to, relative to the parent (painted) element.
   * @return The PopOver'd item's location relative to the parent.
   */
  public Point getLocation();

  /**
   * Returns a Point describing the size of the item to be PopOver'd.
   * The x value is the width of the item, the y value is the height.
   * @return The PopOver'd item's size.
   */
  public Point getSize();
}
