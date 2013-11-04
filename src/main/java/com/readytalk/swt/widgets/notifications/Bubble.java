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

  public enum BubbleDisplayLocation { BELOW_PARENT, ABOVE_PARENT }
  public enum BubblePointCenteredOnParent { TOP_RIGHT_CORNER, TOP_LEFT_CORNER }

  protected BubbleDisplayLocation bubbleDisplayLocation = BubbleDisplayLocation.BELOW_PARENT;
  protected BubblePointCenteredOnParent bubbleTopLeftCornerLocation = BubblePointCenteredOnParent.TOP_LEFT_CORNER;

  private Listener listener;
  private String tooltipText;
  private Control parentControl;
  private Shell parentShell, tooltip;

  private Region tooltipRegion;
  private Rectangle containingRectangle;
  private Rectangle borderRectangle;
  
  private Color textColor;
  private Color backgroundColor;
  private Color borderColor;

  private Listener parentListener;

  public Bubble(Control parentControl, String tooltipText) {
    super(parentControl, SWT.NONE);

    this.parentControl = parentControl;
    this.tooltipText = tooltipText;
    parentShell = AncestryHelper.getShellFromControl(parentControl);

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
    parentControl.addListener(SWT.Dispose, parentListener);
  }

  public void show() {
    Point textExtent = getTextSize(tooltipText);
    containingRectangle = calculateContainingRectangleRegion(textExtent);
    borderRectangle = calculateBorderRectangle(containingRectangle);

    tooltipRegion = new Region();
    Point location = getShellDisplayLocation(parentShell, parentControl, bubbleDisplayLocation,
            bubbleTopLeftCornerLocation, containingRectangle);

    tooltipRegion.add(containingRectangle);

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

  private Point getShellDisplayLocation(Shell parentShell, Control parentControl, BubbleDisplayLocation aboveOrBelow,
                                        BubblePointCenteredOnParent topLeftCorner, Rectangle bubbleRectangle) {
    Point parentControlSize = parentControl.getSize();
    Point parentLocationRelativeToDisplay = parentShell.getDisplay().map(parentShell, null, parentControl.getLocation());
    Point appropriateDisplayLocation = new Point(0, 0);

    switch (aboveOrBelow) {
      case ABOVE_PARENT:
        appropriateDisplayLocation.y = parentLocationRelativeToDisplay.y - bubbleRectangle.height;
        break;
      case BELOW_PARENT:
        appropriateDisplayLocation.y = parentLocationRelativeToDisplay.y + parentControlSize.y;
        break;
    }

    switch(topLeftCorner) {
      case TOP_LEFT_CORNER:
        appropriateDisplayLocation.x = parentLocationRelativeToDisplay.x + (parentControlSize.x / 2);
        break;
      case TOP_RIGHT_CORNER:
        appropriateDisplayLocation.x = parentLocationRelativeToDisplay.x - bubbleRectangle.width + (parentControlSize.x / 2);
        break;
    }

    return appropriateDisplayLocation;
  }

  private Rectangle calculateContainingRectangleRegion(Point textExtent) {
    return new Rectangle(0, 0,
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
    parentControl.removeListener(SWT.Dispose, parentListener);
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
    gc.drawText(tooltipText, containingRectangle.x + (TEXT_WIDTH_PADDING / 2), containingRectangle.y + (TEXT_HEIGHT_PADDING / 2));
  }

  private void onMouseDown(Event event) {
    hide();
  }

  private Point getTextSize(String text) {
    GC gc = new GC(getDisplay());
    Point textExtent = gc.textExtent(text, SWT.DRAW_DELIMITER);
    gc.dispose();
    return textExtent;
  }
}