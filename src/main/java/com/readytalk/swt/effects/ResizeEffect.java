package com.readytalk.swt.effects;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

import java.util.logging.Logger;

/**
 * A roll-your-own resize.  Basically, you can resize any SWT component over time.  A default time and
 * interval will be provided, if you do not wish to provide one, though your mileage may vary.
 *
 * Depending on your systems threading model, you can provide your own LinkableEffect.Executor like so:
 *
 *  new Executor() {
 *    void timerExec(final int time, final Runnable runnable) {
 *         Display.getCurrent().asyncExec( new Runnable() {
 *           public void run() {
 *               display.timerExec(time, runnable);
 *           }
 *     });
 *
 *     void asyncExec(Runnable runnable) { }
 *   }
 *  }
 *
 *  This example is unnecessary though, as the default behavior for LinkableEffect is to use the Display.getCurrentDisplay()
 *  to execute.
 *
 *  The other part to consider is setting your destination rectangle.  This will be what we draw to.  It can be in
 *  a different location, if you choose to specify it.
 *
 *  A note on naming:
 *    While this is designed for Resize, it can also move an item.  HOWEVER, the name is still
 *    appropriate.  Why?  If the consumers of this animation are not using FormLayout, they will need
 *    to carefully adjust their origin if they are growing controls down.  Remember that in most UI systems, top
 *    left is origin at 0,0 and x grows to the right, y grows down.  
 */
public class ResizeEffect extends LinkableEffect {
  private static final Logger LOG = Logger.getLogger(ResizeEffect.class.getName());

  private static final int DEFAULT_PIXEL_INCREMENT = 10;
  private static final int DEFAULT_TIME_INTERVAL = 10;

  private boolean changeXPosition;
  private boolean changeYPosition;
  private boolean changeHeight;
  private boolean changeWidth;
  private int currentX;
  private int currentY;
  private int targetX;
  private int targetY;
  private int currentHeight;
  private int currentWidth;
  private int targetHeight;
  private int targetWidth;

  private final int pixelIncrement;

  private ResizeCallback resizeCallback;
  private final Executor executor;
  private final Resizeable resizer;

  /** 
   * Use the builder!
   */
  private ResizeEffect(int currentX,
                      int currentY,
                      int targetX,
                      int targetY,
                      int currentHeight,
                      int currentWidth,
                      int targetHeight,
                      int targetWidth,
                      int pixelIncrement,
                      int timeInterval,
                      Executor executor,
                      Resizeable resizer,
                      ResizeCallback resizeCallback,
                      LinkableEffect parent,
                      LinkableEffect ... linkableEffects) throws InvalidEffectArgumentException {


    super(parent, executor, timeInterval, linkableEffects);

    // origin data
    this.currentX = currentX;
    this.targetX = targetX;
    changeXPosition = currentX != targetX;

    this.currentY = currentY;
    this.targetY = targetY;
    changeYPosition = currentY != targetY;

    // size data
    this.currentHeight = currentHeight;
    this.targetHeight = targetHeight;
    changeHeight = currentHeight != targetHeight;

    this.currentWidth = currentWidth;
    this.targetWidth = targetWidth;
    changeWidth = currentWidth != targetWidth;

    this.pixelIncrement = (pixelIncrement == 0) ? DEFAULT_PIXEL_INCREMENT : pixelIncrement;
    this.resizeCallback = resizeCallback;
    this.executor = executor;
    this.resizer = resizer;

    // lets warn implementers if they are not doing anything...
    if(!changeXPosition && !changeYPosition && !changeWidth && !changeHeight) {
      LOG.warning("This effect will neither move nor resize, please check your arguments");
    }
  }

  private void resizeWidth() {
    if(changeWidth) {
      currentWidth = calculateDiff(currentWidth, targetWidth);
    }
  }

  private void resizeHeight() {
    if(changeHeight) {
      currentHeight = calculateDiff(currentHeight, targetHeight);
    }
  }

  private void moveX() {
    if(changeXPosition) {
      currentX = calculateDiff(currentX, targetX);
    }
  }

  private void moveY() {
    if(changeYPosition) {
      currentY = calculateDiff(currentY, targetY);
    }
  }

  /**
   * Convienience method, but as primtives are pass-by-value only you must assign the results!
   */
  private int calculateDiff(int current, int target) {
    int result = current;
    int diff = Math.abs(current - target);
    int pixels = (diff < pixelIncrement) ? diff : pixelIncrement;
    if (current > target) {
      result = current - pixels;
    }
    if (current < target) {
      result = current + pixels;
    }
    return result;
  }

  /**
   * Clients should not call this directly.  Use startEffect() instead.
   */
  public void effectStep() {

    resizeWidth();
    resizeHeight();
    moveX();
    moveY();

    resizer.resize(currentWidth, currentHeight);
    resizer.move(currentX, currentY);
  }

  public void effectComplete() {
    if(resizeCallback != null) {
      resizeCallback.resizeComplete();
    }
  }

  public boolean isEffectComplete() {
    return resizer.resizeComplete(targetWidth, targetHeight) &&
      resizer.moveComplete(targetX, targetY);
  }

  public static class ResizeEffectBuilder {

    private Control control;
    private Rectangle current;
    private Rectangle dest;

    private int currentX;
    private int currentY;
    private int targetX;
    private int targetY;
    private int currentHeight;
    private int currentWidth;
    private int targetHeight;
    private int targetWidth;
    private int pixelIncrement;
    private int timeInterval;
    private ResizeCallback resizeCallback;
    private Executor executor;
    private Resizeable resizer;
    private LinkableEffect[] linkableEffects;
    private LinkableEffect parent;

    public ResizeEffectBuilder setLinkableEffects(LinkableEffect ... linkableEffects) {
      this.linkableEffects = linkableEffects;
      return this;
    }

    public ResizeEffectBuilder setParent(LinkableEffect parent) {
      this.parent = parent;
      return this;
    }

    public ResizeEffectBuilder setCurrentX(int currentX) {
      this.currentX = currentX;
      return this;
    }

    public ResizeEffectBuilder setCurrentY(int currentY) {
      this.currentY = currentY;
      return this;
    }

    public ResizeEffectBuilder setTargetX(int targetX) {
      this.targetX = targetX;
      return this;
    }

    public ResizeEffectBuilder setTargetY(int targetY) {
      this.targetY = targetY;
      return this;
    }

    public ResizeEffectBuilder setTargetHeight(int targetHeight) {
      this.targetHeight = targetHeight;
      return this;
    }

    public ResizeEffectBuilder setTargetWidth(int targetWidth) {
      this.targetWidth = targetWidth;
      return this;
    }

    public ResizeEffectBuilder setCurrentHeight(int currentHeight) {
      this.currentHeight = currentHeight;
      return this;
    }

    public ResizeEffectBuilder setCurrentWidth(int currentWidth) {
      this.currentWidth = currentWidth;
      return this;
    }

    public ResizeEffectBuilder setPixelIncrement(int pixelIncrement) {
      this.pixelIncrement = pixelIncrement;
      return this;
    }

    public ResizeEffectBuilder setTimeInterval(int timeInterval) {
      this.timeInterval = timeInterval;
      return this;
    }

    public ResizeEffectBuilder setResizeCallback(ResizeCallback resizeCallback) {
      this.resizeCallback = resizeCallback;
      return this;
    }

    public ResizeEffectBuilder setTimedExecutor(Executor executor) {
      this.executor = executor;
      return this;
    }

    public ResizeEffectBuilder setResizeable(Resizeable resizer) {
      this.resizer = resizer;
      return this;
    }

    public ResizeEffectBuilder setCurrentSize(Point size) {
      this.currentWidth = size.x;
      this.currentHeight = size.y;
      return this;
    }

    public ResizeEffectBuilder setTargetSize(Point size) {
      this.targetWidth = size.x;
      this.targetHeight = size.y;
      return this;
    }

    public ResizeEffectBuilder setCurrentLocation(Point location) {
      this.currentX = location.x;
      this.currentY = location.y;
      return this;
    }

    public ResizeEffectBuilder setTargetLocation(Point location) {
      this.targetX = location.x;
      this.targetY = location.y;
      return this;
    }

    public ResizeEffectBuilder setCurrentBounds(Rectangle bounds) {
      this.currentX = bounds.x;
      this.currentY = bounds.y;
      this.currentWidth = bounds.width;
      this.currentHeight = bounds.height;
      return this;
    }

    public ResizeEffectBuilder setTargetBounds(Rectangle bounds) {
      this.targetX = bounds.x;
      this.targetY = bounds.y;
      this.targetWidth = bounds.width;
      this.targetHeight = bounds.height;
      return this;
    }

    public ResizeEffectBuilder setCurrentControl(Control control) {
      setCurrentBounds(control.getBounds());
      return this;
    }

    public ResizeEffectBuilder() { }

    public ResizeEffect build() throws InvalidEffectArgumentException {
      if(resizer == null) {
        throw new InvalidEffectArgumentException("Resizer required");
      }
      return new ResizeEffect(currentX,
                              currentY,
                              targetX,
                              targetY,
                              currentHeight,
                              currentWidth,
                              targetHeight,
                              targetWidth,
                              pixelIncrement,
                              timeInterval,
                              executor,
                              resizer,
                              resizeCallback,
                              parent,
                              linkableEffects);
    }
  }

  public interface ResizeCallback {
    public void resizeComplete();
  }

  public interface Resizeable {
    public boolean resizeComplete(int targetWidth, int targetHeight);
    public void resize(int width, int height);
    public boolean moveComplete(int targetX, int targetY);
    public void move(int x, int y);
  }
}