package com.readytalk.swt.widgets.notifications;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

/**
 * An abstract class provided to allow Bubbles to be added to arbitrarily drawn components.
 * Typically, these elements are drawn directly to parent components via a GC and, therefore,
 * cannot utilize the constructor for Bubble that takes a Control (or descendant).
 */
public abstract class Bubblable {
  /**
   * Returns the element that's being painted by the GC.
   * For example, if you add a PaintListener to a Canvas, this method should return the Canvas element.
   * @return The element being painted by the GC
   */
  abstract public Control getPaintedElement();

  /**
   * Returns a Point describing the item to be Bubble'd relative to the parent (painted) element.
   * @return The Bubble'd item's location relative to the parent.
   */
  abstract public Point getLocation();

  /**
   * Returns a Point describing the size of the item to be Bubble'd.
   * The x value is the width of the item, the y value is the height.
   * @return The Bubble'd item's size.
   */
  abstract public Point getSize();

  /**
   * Returns the area on the parent composite where a mouseover should summon the Bubble.
   * @return The area where the Bubble should appear relative to the parent (painted) element.
   */
  public Rectangle getRectangleArea() {
    Point location = getLocation();
    Point size = getSize();

    return new Rectangle(location.x, location.y, size.x, size.y);
  }
}
