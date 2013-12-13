package com.readytalk.swt.widgets.notifications;

import com.readytalk.swt.effects.FadeEffect;
import com.readytalk.swt.effects.FadeEffect.Fadeable;
import com.readytalk.swt.effects.InvalidEffectArgumentException;
import com.readytalk.swt.helpers.AncestryHelper;
import com.readytalk.swt.widgets.CustomElementDataProvider;
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

import java.util.logging.Logger;

public abstract class PopOverShell extends Widget implements Fadeable {
  private static final Logger LOG = Logger.getLogger(PopOverShell.class.getName());

  enum PopOverShellDisplayLocation { BELOW_PARENT, ABOVE_PARENT }
  enum PopOverShellPointCenteredOnParent { TOP_RIGHT_CORNER, TOP_LEFT_CORNER }

  private static final RGB BACKGROUND_COLOR = new RGB(74, 74, 74);
  private static final RGB BORDER_COLOR = new RGB(204, 204, 204);
  private static final int BORDER_THICKNESS = 1; //pixels
  private static final int FADE_OUT_TIME = 200; //milliseconds
  private static final int FULLY_VISIBLE_ALPHA = 255; //fully opaque
  private static final int FULLY_HIDDEN_ALPHA = 0; //fully transparent

  private Object fadeLock = new Object();

  private PoppedOverItem poppedOverItem;
  protected Control parentControl;
  private Shell parentShell;
  protected Shell popOverShell;
  private Listener popOverListener;

  private Rectangle borderRectangle;
  private Color backgroundColor;
  private Color borderColor;
  private Region popOverRegion;

  static final PopOverShellDisplayLocation DEFAULT_DISPLAY_LOCATION = PopOverShellDisplayLocation.BELOW_PARENT;
  static final PopOverShellPointCenteredOnParent DEFAULT_POINT_CENTERED = PopOverShellPointCenteredOnParent.TOP_LEFT_CORNER;

  PopOverShellDisplayLocation popOverShellDisplayLocation = DEFAULT_DISPLAY_LOCATION;
  PopOverShellPointCenteredOnParent popOverShellPointCenteredOnParent = DEFAULT_POINT_CENTERED;

  private boolean popOverShellIsFullyConfigured = false;
  private boolean fadeEffectInProgress = false;

  public PopOverShell(Control parentControl, CustomElementDataProvider customElementDataProvider) {
    super(parentControl, SWT.NONE);

    if (customElementDataProvider != null) {
      poppedOverItem = new PoppedOverItem(customElementDataProvider);
    } else {
      poppedOverItem = new PoppedOverItem(parentControl);
    }

    this.parentControl = parentControl;
    parentShell = AncestryHelper.getShellFromControl(poppedOverItem.getControl());

    backgroundColor = new Color(getDisplay(), BACKGROUND_COLOR);
    borderColor = new Color(getDisplay(), BORDER_COLOR);

    popOverShell = new Shell(parentShell, SWT.ON_TOP | SWT.NO_TRIM);
    popOverShell.setBackground(backgroundColor);

    attachListeners();
  }

  /**
   * Shows the PopOverShell in a suitable location relative to the parent component.
   */
  public void show() {
    popOverRegion = getAppropriatePopOverRegion();

    borderRectangle = calculateBorderRectangle(popOverRegion.getBounds());
    Point location = getShellDisplayLocation(parentShell, poppedOverItem, popOverShellDisplayLocation,
            popOverShellPointCenteredOnParent, popOverRegion.getBounds());

    while (!popOverShellIsFullyConfigured) {
      popOverShellIsFullyConfigured = configureBubbleIfWouldBeCutOff(parentShell.getDisplay().getClientArea(),
              location, popOverRegion.getBounds());
      location = getShellDisplayLocation(parentShell, getPoppedOverItem(), popOverShellDisplayLocation,
              popOverShellPointCenteredOnParent, popOverRegion.getBounds());
    }

    popOverShell.setRegion(popOverRegion);
    popOverShell.setSize(popOverRegion.getBounds().width, popOverRegion.getBounds().height);
    popOverShell.setLocation(location);
    popOverShell.setAlpha(FULLY_VISIBLE_ALPHA);
    popOverShell.setVisible(true);
  }

  PoppedOverItem getPoppedOverItem() {
    return poppedOverItem;
  }


  private Rectangle calculateBorderRectangle(Rectangle containingRectangle) {
    return new Rectangle(containingRectangle.x,
            containingRectangle.y,
            containingRectangle.width - BORDER_THICKNESS,
            containingRectangle.height - BORDER_THICKNESS);
  }

  private void attachListeners() {
    popOverListener = new Listener() {
      public void handleEvent(Event event) {
        switch (event.type) {
          case SWT.Dispose:
            onDispose(event);
            break;
          case SWT.Paint:
            onPaint(event);
            break;
        }
      }
    };

    addListener(SWT.Dispose, popOverListener);
    popOverShell.addListener(SWT.Paint, popOverListener);
  }

  private void onPaint(Event event) {
    GC gc = event.gc;

    gc.setForeground(borderColor);
    gc.setLineWidth(BORDER_THICKNESS);
    gc.drawRectangle(borderRectangle);
  }

  private void onDispose(Event event) {
    parentControl.removeListener(SWT.Dispose, popOverListener);
    removeListener(SWT.Dispose, popOverListener);
    notifyListeners(SWT.Dispose, event);
    event.type = SWT.None;

    backgroundColor.dispose();
    borderColor.dispose();
    popOverShell.dispose();
    popOverShell = null;

    if (popOverRegion != null) {
      popOverRegion.dispose();
    }
    popOverRegion = null;
  }

  boolean configureBubbleIfWouldBeCutOff(Rectangle displayBounds, Point locationRelativeToDisplay, Rectangle containingRectangle) {
    if (configureBubbleIfBottomCutOff(displayBounds, locationRelativeToDisplay, containingRectangle)) {
      return false;
    } else if  (configureBubbleIfRightmostTextCutOff(displayBounds, locationRelativeToDisplay, containingRectangle)) {
      return false;
    }

    return true;
  }

  boolean configureBubbleIfBottomCutOff(Rectangle displayBounds, Point locationRelativeToDisplay, Rectangle containingRectangle) {
    Point lowestYPosition = new Point(locationRelativeToDisplay.x, locationRelativeToDisplay.y + containingRectangle.height);

    if (!displayBounds.contains(lowestYPosition)) {
      popOverShellDisplayLocation = PopOverShellDisplayLocation.ABOVE_PARENT;
      return true;
    } else {
      return false;
    }
  }

  boolean configureBubbleIfRightmostTextCutOff(Rectangle displayBounds, Point locationRelativeToDisplay, Rectangle containingRectangle) {
    Point farthestXPosition = new Point(locationRelativeToDisplay.x + containingRectangle.width, locationRelativeToDisplay.y);

    if (!displayBounds.contains(farthestXPosition)) {
      popOverShellPointCenteredOnParent = PopOverShellPointCenteredOnParent.TOP_RIGHT_CORNER;
      return true;
    } else {
      return false;
    }
  }

  private Point getShellDisplayLocation(Shell parentShell, PoppedOverItem poppedOverItem, PopOverShellDisplayLocation aboveOrBelow,
                                        PopOverShellPointCenteredOnParent popOverShellPointCenteredOnParent, Rectangle bubbleRectangle) {
    Point bubbledItemSize = poppedOverItem.getSize();
    Point parentLocationRelativeToDisplay = parentShell.getDisplay().map(parentShell, null, poppedOverItem.getLocation());
    Point appropriateDisplayLocation = new Point(0, 0);

    switch (aboveOrBelow) {
      case ABOVE_PARENT:
        appropriateDisplayLocation.y = parentLocationRelativeToDisplay.y - bubbleRectangle.height;
        break;
      case BELOW_PARENT:
        appropriateDisplayLocation.y = parentLocationRelativeToDisplay.y + bubbledItemSize.y;
        break;
    }

    switch(popOverShellPointCenteredOnParent) {
      case TOP_LEFT_CORNER:
        appropriateDisplayLocation.x = parentLocationRelativeToDisplay.x + (bubbledItemSize.x / 2);
        break;
      case TOP_RIGHT_CORNER:
        appropriateDisplayLocation.x = parentLocationRelativeToDisplay.x - bubbleRectangle.width + (bubbledItemSize.x / 2);
        break;
    }

    return appropriateDisplayLocation;
  }

  /**
   * Fades the Bubble off the screen.
   */
  public void fadeOut() {
    if (fadeEffectInProgress) {
      return;
    }

    try {
      FadeEffect fade = new FadeEffect.FadeEffectBuilder().
              setFadeable(this).
              setFadeCallback(new BubbleFadeCallback()).
              setFadeTimeInMilliseconds(FADE_OUT_TIME).
              setCurrentAlpha(FULLY_VISIBLE_ALPHA).
              setTargetAlpha(FULLY_HIDDEN_ALPHA).build();

      fade.startEffect();
      fadeEffectInProgress = true;
    } catch (InvalidEffectArgumentException e) {
      LOG.warning("Invalid argument provided to FadeEffect.");
    }
  }

  /**
   * Returns whether the Bubble is currently fading from the screen.
   * Calls to <code>Bubble.isVisible()</code> will return true while the Bubble is dismissing.
   * @return Whether or not the Bubble is currently fading from the screen
   */
  public boolean getIsFadeEffectInProgress() {
    return fadeEffectInProgress;
  }

  /**
   * Implemented as part of Fadeable. <br/>
   * Users should not interact directly invoke this method.
   */
  public boolean fadeComplete(int targetAlpha) {
    synchronized (fadeLock) {
      if (popOverShell.getAlpha() == targetAlpha) {
        return true;
      } else {
        return false;
      }
    }
  }

  /**
   * Implemented as part of Fadeable. <br/>
   * Users should not interact directly invoke this method.
   */
  public void fade(int alpha) {
    synchronized (fadeLock) {
      popOverShell.setAlpha(alpha);
    }
  }

  void hide() {
    popOverShell.setVisible(false);
    resetState();
    resetWidget();
  }

  private void resetState() {
    popOverShellDisplayLocation = DEFAULT_DISPLAY_LOCATION;
    popOverShellPointCenteredOnParent = DEFAULT_POINT_CENTERED;
    popOverShellIsFullyConfigured = false;
    fadeEffectInProgress = false;
  }

  private class BubbleFadeCallback implements FadeEffect.FadeCallback {
    public void fadeComplete() {
      hide();
    }
  }

  abstract Region getAppropriatePopOverRegion();
  abstract void resetWidget();
}
