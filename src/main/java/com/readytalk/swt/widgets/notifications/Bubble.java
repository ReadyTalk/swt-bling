package com.readytalk.swt.widgets.notifications;

import com.readytalk.swt.helpers.AncestryHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

public class Bubble extends Widget {
  private static final RGB BACKGROUND_COLOR = new RGB(74, 74, 74);
  private static final RGB TEXT_COLOR = new RGB(204, 204, 204);
  private static final int TEXT_HEIGHT_PADDING = 5; //pixels
  private static final int TEXT_WIDTH_PADDING = 10; //pixels
  private static final int BORDER_THICKNESS = 1; //pixels

  private Listener listener;
  private String tooltipText;
  private Control parent;
  private Shell parentShell, tooltip;

  private Region tooltipRegion;
  private Rectangle rectangle;
  private Rectangle borderRectangle;
  
  private Color textColor;
  private Color backgroundColor;
  private Color borderColor;

  private Listener parentListener;

  public Bubble(Control parent, String tooltipText) {
    super(parent, SWT.NONE);

    this.parent = parent;
    this.tooltipText = tooltipText;
    parentShell = AncestryHelper.getShellFromControl(parent);

    // Remember to clean up after yourself onDispose.
    backgroundColor = new Color(getDisplay(), BACKGROUND_COLOR);
    textColor = new Color(getDisplay(), TEXT_COLOR);
    borderColor = textColor;

    tooltip = new Shell(parentShell, SWT.ON_TOP | SWT.NO_TRIM);
    tooltip.setBackground(backgroundColor);

    listener = new Listener() {
      public void handleEvent(Event event) {
        switch (event.type) {
          case SWT.Dispose:
            onDispose(event);
            break;
          case SWT.Paint:
            onPaint(event);
            break;
          case SWT.MouseDown:
            onMouseDown(event);
            break;
        }
      }
    };
    addListener(SWT.Dispose, listener);
    tooltip.addListener(SWT.Paint, listener);
    tooltip.addListener(SWT.MouseDown, listener);

    parentListener = new Listener() {
      public void handleEvent(Event event) {
        dispose();
      }
    };
    parent.addListener(SWT.Dispose, parentListener);
  }

  public void show() {
    tooltipRegion = new Region();
    Point location = parentShell.getDisplay().map(parentShell, null, parent.getLocation());
    Point textExtent = getTextSize(tooltipText);

    rectangle = calculateRectangleRegion(parent.getSize(), textExtent);
    borderRectangle = calculateBorderRectangle(rectangle);

    tooltipRegion.add(rectangle);

    tooltip.setRegion(tooltipRegion);
    tooltip.setLocation(location);
    tooltip.setVisible(true);
  }

  public void hide() {
    tooltip.setVisible(false);
  }

  public boolean isVisible() {
    return tooltip.isVisible();
  }

  private Rectangle calculateRectangleRegion(Point componentSize, Point textExtent) {
    return new Rectangle(componentSize.x / 2, componentSize.y,
            textExtent.x + TEXT_WIDTH_PADDING,
            textExtent.y + TEXT_HEIGHT_PADDING);
  }

  private Rectangle calculateBorderRectangle(Rectangle containingRectangle) {
    return new Rectangle(containingRectangle.x,
            containingRectangle.y,
            containingRectangle.width - BORDER_THICKNESS,
            containingRectangle.height - BORDER_THICKNESS);
  }

  public void checkSubclass() {
    //no-op
  }

  // TODO: we're not supposed to extend widget. We need to make sure that our dispose code is being called appropriately.
  private void onDispose(Event event) {
    parent.removeListener(SWT.Dispose, parentListener);
    removeListener(SWT.Dispose, listener);
    notifyListeners(SWT.Dispose, event);
    event.type = SWT.None;

    tooltip.dispose();
    tooltip = null;
    if (tooltipRegion != null) {
      tooltipRegion.dispose();
    }
  }

  private void onPaint(Event event) {
    GC gc = event.gc;

    gc.setForeground(borderColor);
    gc.setLineWidth(BORDER_THICKNESS);
    gc.drawRectangle(borderRectangle);

    gc.setForeground(textColor);
    gc.drawText(tooltipText, rectangle.x + (TEXT_WIDTH_PADDING / 2), rectangle.y + (TEXT_HEIGHT_PADDING / 2));
  }

  private void onMouseDown(Event event) {
    hide();
  }

  private Point getTextSize(String text) {
    GC gc = new GC(getDisplay());
    Point textExtent = gc.textExtent(text);
    gc.dispose();
    return textExtent;
  }
}