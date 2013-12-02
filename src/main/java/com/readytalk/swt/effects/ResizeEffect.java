package com.readytalk.swt.effects;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

import java.util.logging.Logger;

/**
 * A roll-your-own resize.  Basically, you can resize any SWT component over time.  A default time and
 * interval will be provided, if you do not wish to provide one, though your mileage may vary. <br/>
 * <br/>
 * Depending on your systems threading model, you can provide your own LinkableEffect.Executor like so:<br/>
 *
 *  <pre><code>
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
 *  </code></pre>
 *
 *  This example is unnecessary though, as the default behavior for LinkableEffect is to use the Display.getCurrentDisplay()
 *  to execute.<br/>
 *  <br/>
 *  The other part to consider is setting your destination rectangle.  This will be what we draw to.  It can be in
 *  a different location, if you choose to specify it.<br/>
 *  <br/>
 *  A note on naming: <br/>
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

  /**
   * Builder used to create and customize a ResizeEffect.<br/>
   * Note: You need to either provide a current/target Size (or Height, Width), Location (or X, Y) or Bounds
   * for the Effect to do anything useful.
   */
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

    /**
     * (Optional) Link other effects to the ResizeEffect.
     * @param linkableEffects 1 to many Effects to link
     */
    public ResizeEffectBuilder setLinkableEffects(LinkableEffect ... linkableEffects) {
      this.linkableEffects = linkableEffects;
      return this;
    }

    /**
     * (Optional) Set the LinkableEffect you want run first.
     * @param parent The LinkableEffect to run first
     */
    public ResizeEffectBuilder setParent(LinkableEffect parent) {
      this.parent = parent;
      return this;
    }

    /**
     * (Optional) The current X location of the element to resize.
     * @param currentX The current X location
     */
    public ResizeEffectBuilder setCurrentX(int currentX) {
      this.currentX = currentX;
      return this;
    }

    /**
     * (Optional) The current Y location of the element to resize.
     * @param currentY The current Y location
     */
    public ResizeEffectBuilder setCurrentY(int currentY) {
      this.currentY = currentY;
      return this;
    }

    /**
     * (Optional) The target X location of the element to resize.
     * @param targetX The target X location
     */
    public ResizeEffectBuilder setTargetX(int targetX) {
      this.targetX = targetX;
      return this;
    }

    /**
     * (Optional) The target Y location of the element to resize.
     * @param targetY The target Y location
     */
    public ResizeEffectBuilder setTargetY(int targetY) {
      this.targetY = targetY;
      return this;
    }

    /**
     * (Optional) The target height of the element to resize.
     * @param targetHeight The target height
     */
    public ResizeEffectBuilder setTargetHeight(int targetHeight) {
      this.targetHeight = targetHeight;
      return this;
    }

    /**
     * (Optional) The target width of the element to resize.
     * @param targetWidth The target width
     */
    public ResizeEffectBuilder setTargetWidth(int targetWidth) {
      this.targetWidth = targetWidth;
      return this;
    }

    /**
     * (Optional) The current height of the element to resize.
     * @param currentHeight The current height
     */
    public ResizeEffectBuilder setCurrentHeight(int currentHeight) {
      this.currentHeight = currentHeight;
      return this;
    }

    /**
     * (Optional) The current width of the element to resize.
     * @param currentWidth The current width
     */
    public ResizeEffectBuilder setCurrentWidth(int currentWidth) {
      this.currentWidth = currentWidth;
      return this;
    }

    /**
     * (Optional) Defines how many pixels to resize on each call to <code>resize()</code><br/>
     * Defaults to 10 pixels.
     * @param pixelIncrement The number of pixels to resize on each call to <code>resize()</code>
     */
    public ResizeEffectBuilder setPixelIncrement(int pixelIncrement) {
      this.pixelIncrement = pixelIncrement;
      return this;
    }

    /**
     * (Optional) Defines how long you want the effect to last (in milliseconds).<br/>
     * Defaults to 10 milliseconds.
     * @param timeInterval The number of milliseconds you want the effect to last.
     */
    public ResizeEffectBuilder setTimeInterval(int timeInterval) {
      this.timeInterval = timeInterval;
      return this;
    }

    /**
     * (Optional) Set a callback to be invoked when the Resize is complete.<br/>
     * Defaults to no-op.
     * @param resizeCallback a ResizeCallback
     */
    public ResizeEffectBuilder setResizeCallback(ResizeCallback resizeCallback) {
      this.resizeCallback = resizeCallback;
      return this;
    }

    /**
     * (Optional) Sets a custom executor for the effect. See the FadeEffect class Javadoc for an example.
     * @param executor an Executor
     */
    public ResizeEffectBuilder setExecutor(Executor executor) {
      this.executor = executor;
      return this;
    }

    /**
     * (Required) A class that implements the Resizeable interface.
     * @param resizer The class that implements the Resizeable interface.
     */
    public ResizeEffectBuilder setResizeable(Resizeable resizer) {
      this.resizer = resizer;
      return this;
    }

    /**
     * (Optional) The current size of the element to resize.
     * @param size The current size
     */
    public ResizeEffectBuilder setCurrentSize(Point size) {
      this.currentWidth = size.x;
      this.currentHeight = size.y;
      return this;
    }

    /**
     * (Optional) The target size of the element to resize.
     * @param size The target size
     */
    public ResizeEffectBuilder setTargetSize(Point size) {
      this.targetWidth = size.x;
      this.targetHeight = size.y;
      return this;
    }

    /**
     * (Optional) The current location of the element to resize.
     * @param location The current location
     */
    public ResizeEffectBuilder setCurrentLocation(Point location) {
      this.currentX = location.x;
      this.currentY = location.y;
      return this;
    }

    /**
     * (Optional) The target location of the element to resize.
     * @param location The target location
     */
    public ResizeEffectBuilder setTargetLocation(Point location) {
      this.targetX = location.x;
      this.targetY = location.y;
      return this;
    }

    /**
     * (Optional) The current bounds of the element to resize.
     * @param bounds The current bounds
     */
    public ResizeEffectBuilder setCurrentBounds(Rectangle bounds) {
      this.currentX = bounds.x;
      this.currentY = bounds.y;
      this.currentWidth = bounds.width;
      this.currentHeight = bounds.height;
      return this;
    }

    /**
     * (Optional) The target bounds of the element to resize.
     * @param bounds The target bounds
     */
    public ResizeEffectBuilder setTargetBounds(Rectangle bounds) {
      this.targetX = bounds.x;
      this.targetY = bounds.y;
      this.targetWidth = bounds.width;
      this.targetHeight = bounds.height;
      return this;
    }

    /**
     * (Optional) The <code>Control</code> to Resize.
     * @param control controls
     */
    public ResizeEffectBuilder setCurrentControl(Control control) {
      setCurrentBounds(control.getBounds());
      return this;
    }

    public ResizeEffectBuilder() { }

    /**
     * Build the ResizeEffect given the options set in the Builder.
     * @return A ResizeEffect
     * @throws InvalidEffectArgumentException If you forget to call <code>setResizeable()</code>
     */
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

  /**
   * Implementers of this interface are called when a ResizeEffect completes.
   */
  public interface ResizeCallback {
    /**
     * The logic to execute when the ResizeEffect is complete.
     */
    public void resizeComplete();
  }

  /**
   * Classes that need to implement a ResizeEffect must implement this interface.
   */
  public interface Resizeable {
    /**
     * This method is called by the ResizeEffect after each step to determine whether or not the component has
     * gone from the current to the target values
     * @param targetWidth The targetWidth you defined as part of the ResizeEffect
     * @param targetHeight The targetHeight you defined as part of the ResizeEffect
     * @return true if the component is at the desired width/height, false otherwise
     */
    public boolean resizeComplete(int targetWidth, int targetHeight);

    /**
     * This method is called on each step of the Resize.<br/>
     * This method should handle setting the width and height on each step of the effect.
     * @param width The current width (will start at current width and end at the target width)
     * @param height The current height (will start at current height and end at the target height)
     */
    public void resize(int width, int height);
    /**
     * This method is called by the ResizeEffect after each step to determine whether or not the component has
     * gone from the current to the target values
     * @param targetX The targetX you defined as part of the ResizeEffect
     * @param targetY The targetY you defined as part of the ResizeEffect
     * @return true if the component is at the desired X/Y, false otherwise
     */
    public boolean moveComplete(int targetX, int targetY);
    /**
     * This method is called on each step of the Resize.<br/>
     * This method should handle setting the X and Y on each step of the effect.
     * @param x The current X (will start at current X and end at the target X)
     * @param y The current Y (will start at current Y and end at the target Y)
     */
    public void move(int x, int y);
  }
}