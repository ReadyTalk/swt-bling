package com.readytalk.swt.widgets.notifications;

import com.readytalk.swt.effects.FadeEffect;
import com.readytalk.swt.effects.FadeEffect.Fadeable;
import com.readytalk.swt.effects.InvalidEffectArgumentException;
import com.readytalk.swt.helpers.AncestryHelper;
import com.readytalk.swt.widgets.CustomElementDataProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

import java.util.logging.Logger;

/**
 * PopOverShell provides a simple interface for popping a Shell on top of any Object that subclasses
 * <code>Control</code> or implements <code>CustomElementDataProvider</code>
 */
public abstract class PopOverShell extends Widget implements Fadeable {
  private static final Logger LOG = Logger.getLogger(PopOverShell.class.getName());

  enum PopOverShellDisplayLocation { BELOW_PARENT, ABOVE_PARENT }
  enum PopOverShellPointCenteredOnParent { TOP_RIGHT_CORNER, TOP_LEFT_CORNER }

  private static final RGB BACKGROUND_COLOR = new RGB(74, 74, 74);
  private static final int FADE_OUT_TIME = 200; //milliseconds
  private static final int FULLY_VISIBLE_ALPHA = 255; //fully opaque
  private static final int FULLY_HIDDEN_ALPHA = 0; //fully transparent

  static final PopOverShellDisplayLocation DEFAULT_DISPLAY_LOCATION = PopOverShellDisplayLocation.BELOW_PARENT;
  static final PopOverShellPointCenteredOnParent DEFAULT_POINT_CENTERED = PopOverShellPointCenteredOnParent.TOP_LEFT_CORNER;

  PopOverShellDisplayLocation popOverShellDisplayLocation = DEFAULT_DISPLAY_LOCATION;
  PopOverShellPointCenteredOnParent popOverShellPointCenteredOnParent = DEFAULT_POINT_CENTERED;
  boolean stillOffScreenAfterConfiguration = false;

  private Object fadeLock = new Object();

  protected Control parentControl;
  protected Shell popOverShell;

  private Shell parentShell;
  private PoppedOverItem poppedOverItem;
  private Listener popOverListener;
  private Listener parentListener;

  private Color backgroundColor;

  private Region popOverRegion;

  private boolean popOverShellIsFullyConfigured = false;
  private boolean fadeEffectInProgress = false;

  /**
   * Provides the backbone for Custom Widgets that need a <code>Shell</code> popped over a <code>Control</code> or
   * <code>CustomElementDataProvider</code>. If you're using a <code>CustomElementDataProvider</code>, pass the
   * <code>CustomElementDataProvider.getPaintedElement()</code> as the parentControl.
   * @param parentControl The control you want the PopOverShell to appear above. In the case of
   *                      <code>CustomElementDataProvider</code>, pass
   *                      <code>CustomElementDataProvider.getPaintedElement()</code>.
   * @param customElementDataProvider The <code>CustomElementDataProvider</code> you want the PopOverShell to appear
   *                                  above (or null if you're using a Control)
   */
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

    popOverShell = new Shell(parentShell, SWT.ON_TOP | SWT.NO_TRIM);
    popOverShell.setBackground(backgroundColor);
    popOverShell.setLayout(new FillLayout());

    attachListeners();
  }

  /**
   * Shows the PopOverShell in a suitable location relative to the parent component. Classes extending PopOverShell will
   * provide the <code>Region</code> via the abstract <code>getAppropriatePopOverRegion()</code> method.
   */
  public void show() {
    runBeforeShowPopOverShell();

    Point popOverShellSize = getAppropriatePopOverSize();
    popOverRegion = new Region();
    popOverRegion.add(new Rectangle(0, 0, popOverShellSize.x, popOverShellSize.y));

    Point location = getShellDisplayLocation(parentShell, poppedOverItem, popOverShellDisplayLocation,
            popOverShellPointCenteredOnParent, popOverRegion.getBounds(), parentShell.getDisplay().getBounds());

    if (isBottomCutOff(parentShell.getDisplay().getBounds(),
            location, popOverRegion.getBounds())) {
      popOverShellDisplayLocation = PopOverShellDisplayLocation.ABOVE_PARENT;
      location = getShellDisplayLocation(parentShell, getPoppedOverItem(), popOverShellDisplayLocation,
        popOverShellPointCenteredOnParent, popOverRegion.getBounds(), parentShell.getDisplay().getBounds());
    }

    if (isRightCutOff(parentShell.getDisplay().getBounds(),
            location, popOverRegion.getBounds())) {
      popOverShellPointCenteredOnParent = PopOverShellPointCenteredOnParent.TOP_RIGHT_CORNER;
      location = getShellDisplayLocation(parentShell, getPoppedOverItem(), popOverShellDisplayLocation,
              popOverShellPointCenteredOnParent, popOverRegion.getBounds(), parentShell.getDisplay().getBounds());
    }

    if (isStillOffScreen(parentShell.getDisplay().getBounds(),
            location, popOverRegion.getBounds())) {
      stillOffScreenAfterConfiguration = true;
      location = getShellDisplayLocation(parentShell, getPoppedOverItem(), popOverShellDisplayLocation,
              popOverShellPointCenteredOnParent, popOverRegion.getBounds(), parentShell.getDisplay().getBounds());
    }

//    while (!popOverShellIsFullyConfigured) {
//      popOverShellIsFullyConfigured = configurePopOverShellIfWouldBeCutOff(parentShell.getDisplay().getBounds(),
//              location, popOverRegion.getBounds());
//      location = getShellDisplayLocation(parentShell, getPoppedOverItem(), popOverShellDisplayLocation,
//              popOverShellPointCenteredOnParent, popOverRegion.getBounds());
//    }

    popOverShell.setRegion(popOverRegion);
    popOverShell.setSize(popOverRegion.getBounds().width, popOverRegion.getBounds().height);
    popOverShell.setLocation(location);
    popOverShell.setAlpha(FULLY_VISIBLE_ALPHA);
    popOverShell.setVisible(true);
  }

  /**
   * Toggles visibility of the PopOverShell. If the PopOverShell is visible, it will fade it from the screen, otherwise
   * it will pop it up.
   */
  public void toggle() {
    if (isVisible() && !getIsFadeEffectInProgress()) {
      fadeOut();
    } else {
      show();
    }
  }

  /**
   * Implementers of this method return a Point describing the width and height the PopOverShell should be.
   * @return A Point object describing the appropriate PopOverSize. The x is the width and y is the height.
   */
  abstract Point getAppropriatePopOverSize();

  /**
   * Implementers of this method run any logic that needs to be executed before the PopOverShell is shown to
   * the user.
   */
  abstract void runBeforeShowPopOverShell();

  /**
   * Implementers of this method should do any clean-up needed to reset the widget to its default state.
   */
  abstract void resetWidget();

  /**
   * Called when the parent <code>PopOverShell</code> is disposed. Make sure you clean up any leftover elements
   * that need to be disposed. See https://github.com/ReadyTalk/swt-bling/wiki/Finding-SWT-Resource-Leaks-with-Sleak
   * for more information on detecting leaks with Sleak.
   */
  abstract void widgetDispose();

  PoppedOverItem getPoppedOverItem() {
    return poppedOverItem;
  }
  Shell getPopOverShell() { return popOverShell; }

  public void checkSubclass() {
    //no-op
  }

  private void attachListeners() {
    popOverListener = new Listener() {
      public void handleEvent(Event event) {
        switch (event.type) {
          case SWT.Dispose:
            onDispose(event);
            break;
        }
      }
    };

    addListener(SWT.Dispose, popOverListener);

    parentListener = new Listener() {
      public void handleEvent(Event event) {
        dispose();
      }
    };
    parentControl.addListener(SWT.Dispose, parentListener);
  }

  private void onDispose(Event event) {
    widgetDispose();

    parentControl.removeListener(SWT.Dispose, parentListener);
    removeListener(SWT.Dispose, parentListener);
    event.type = SWT.None;

    backgroundColor.dispose();
    popOverShell.dispose();
    popOverShell = null;

    if (popOverRegion != null) {
      popOverRegion.dispose();
    }
    popOverRegion = null;
  }

//  boolean configurePopOverShellIfWouldBeCutOff(Rectangle displayBounds, Point locationRelativeToDisplay,
//                                               Rectangle containingRectangle) {
//    if (configurePopOverShellIfBottomCutOff(displayBounds, locationRelativeToDisplay, containingRectangle)) {
//      return false;
//    } else if  (configurePopOverShellIfRightmostTextCutOff(displayBounds, locationRelativeToDisplay, containingRectangle)) {
//      return false;
//    }
//
//    return true;
//  }


  boolean isBottomCutOff(Rectangle displayBounds, Point locationRelativeToDisplay,
                                              Rectangle containingRectangle) {
    Point lowestYPosition = new Point(locationRelativeToDisplay.x, locationRelativeToDisplay.y + containingRectangle.height);

    if (!displayBounds.contains(lowestYPosition)) {
      return true;
    } else {
      return false;
    }
  }

  boolean isStillOffScreen(Rectangle displayBounds, Point locationRelativeToDisplay,
                           Rectangle containingRectangle) {
    Point currentPosition = new Point (locationRelativeToDisplay.x + containingRectangle.width,
            locationRelativeToDisplay.y + containingRectangle.height);
    if (!displayBounds.contains(currentPosition)) {
      return true;
    } else {
      return false;
    }
  }

  boolean isRightCutOff(Rectangle displayBounds, Point locationRelativeToDisplay,
                                                     Rectangle containingRectangle) {
    Point farthestXPosition = new Point(locationRelativeToDisplay.x + containingRectangle.width, locationRelativeToDisplay.y);

    if (!displayBounds.contains(farthestXPosition)) {
      popOverShellPointCenteredOnParent = PopOverShellPointCenteredOnParent.TOP_RIGHT_CORNER;
      return true;
    } else {
      return false;
    }
  }

  private Point getShellDisplayLocation(Shell parentShell, PoppedOverItem poppedOverItem,
                                        PopOverShellDisplayLocation aboveOrBelow,
                                        PopOverShellPointCenteredOnParent popOverShellPointCenteredOnParent,
                                        Rectangle popOverRectangle, Rectangle displayBounds) {
    Point poppedOverItemSize = poppedOverItem.getSize();
    Point parentLocationRelativeToDisplay = parentShell.getDisplay().map(parentShell, null, poppedOverItem.getLocation());
    Point appropriateDisplayLocation = new Point(0, 0);

    switch (aboveOrBelow) {
      case ABOVE_PARENT:
        appropriateDisplayLocation.y = parentLocationRelativeToDisplay.y - popOverRectangle.height;
        break;
      case BELOW_PARENT:
        appropriateDisplayLocation.y = parentLocationRelativeToDisplay.y + poppedOverItemSize.y;
        break;
    }

    switch(popOverShellPointCenteredOnParent) {
      case TOP_LEFT_CORNER:
        appropriateDisplayLocation.x = parentLocationRelativeToDisplay.x + (poppedOverItemSize.x / 2);
        break;
      case TOP_RIGHT_CORNER:
        appropriateDisplayLocation.x = parentLocationRelativeToDisplay.x - popOverRectangle.width + (poppedOverItemSize.x / 2);
        break;
    }

    if (stillOffScreenAfterConfiguration) {
      if (!displayBounds.contains(new Point(parentLocationRelativeToDisplay.x + popOverRectangle.width, 0))) {
        appropriateDisplayLocation.x = displayBounds.width - popOverRectangle.width;
      }
      if (!displayBounds.contains(new Point(0, parentLocationRelativeToDisplay.y + popOverRectangle.height))) {
        appropriateDisplayLocation.y = displayBounds.height - popOverRectangle.height;
      }
    }

    return appropriateDisplayLocation;
  }

  /**
   * Returns whether the PopOverShell is currently visible on screen.
   * Note: If you utilize <code>PopOverShell.fadeOut()</code>, this method will return true while it's fading.
   * To determine if it's fading out, call <code>PopOverShell.getIsFadeEffectInProgress</code>
   * @return Visibility state of the PopOverShell
   */
  public boolean isVisible() {
    return popOverShell.isVisible();
  }

  /**
   * Fades the <code>PopOverShell</code> off the screen.
   */
  public void fadeOut() {
    if (fadeEffectInProgress) {
      return;
    }

    try {
      fadeEffectInProgress = true;
      FadeEffect fade = new FadeEffect.FadeEffectBuilder().
              setFadeable(this).
              setFadeCallback(new PopOverShellFadeCallback()).
              setFadeTimeInMilliseconds(FADE_OUT_TIME).
              setCurrentAlpha(FULLY_VISIBLE_ALPHA).
              setTargetAlpha(FULLY_HIDDEN_ALPHA).build();

      fade.startEffect();
    } catch (InvalidEffectArgumentException e) {
      LOG.warning("Invalid argument provided to FadeEffect.");
    }
  }

  /**
   * Returns whether the PopOverShell is currently fading from the screen.
   * Calls to <code>PopOverShell.isVisible()</code> will return true while the PopOverShell is dismissing.
   * @return Whether or not the PopOverShell is currently fading from the screen
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

  private class PopOverShellFadeCallback implements FadeEffect.FadeCallback {
    public void fadeComplete() {
      hide();
    }
  }

  public class PoppedOverItem {
    private Control control;
    private CustomElementDataProvider customElementDataProvider;

    public PoppedOverItem(Control control) {
      this.control = control;
    }

    public PoppedOverItem(CustomElementDataProvider customElementDataProvider) {
      this.customElementDataProvider = customElementDataProvider;
    }

    Point getSize() {
      if (control != null) {
        return control.getSize();
      } else {
        return customElementDataProvider.getSize();
      }
    }

    Point getLocation() {
      if (control != null) {
        return control.getLocation();
      } else {
        return customElementDataProvider.getLocation();
      }
    }

    Control getControl() {
      if (control != null) {
        return control;
      } else {
        return customElementDataProvider.getPaintedElement();
      }
    }

    Object getControlOrCustomElement() {
      if (customElementDataProvider != null) {
        return customElementDataProvider;
      } else {
        return control;
      }
    }

    CustomElementDataProvider getCustomElementDataProvider() {
      return customElementDataProvider;
    }
  }
}
