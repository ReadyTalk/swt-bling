package com.readytalk.swt.effects;

import java.util.logging.Logger;

/**
 * A roll-your-own fade effect.  Basically, this effect does the math to fade from a current to target alpha value.
 * A default time and fade interval will be provided, if you do not wish to provide one, though your mileage may vary.<br/>
 * <br/>
 * Depending on your systems threading model, you can provide your own LinkableEffect.Executor like so:<br/>
 *
 * <pre><code>
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
 *  to execute.
 *
 *  An important nugget to keep in mind when rolling a Fade effect is SWT's limited support for setting alpha values.
 *  Out-of-the-box, the only component you'll be able to fade the alpha on is a <code>Shell</code>. Another option is to
 *  utilize this effect with elements drawn with a <code>GC</code>. You can make use of <code>gc.setAlpha()</code> to
 *  achieve transparency effects with images or other UI components (though you have to manage your drawing code effectively).
 */
public class FadeEffect extends LinkableEffect {
  private static final Logger LOG = Logger.getLogger(FadeEffect.class.getName());
  private static final int DEFAULT_TIME_INTERVAL = 10; //milliseconds
  private static final int DEFAULT_FADE_TIME = 1000; //milliseconds

  private int currentAlpha;
  private int targetAlpha;
  private boolean changeAlpha;

  private final int timeInterval;
  private final int fadeTime;
  private final int fadeStepSize;

  private FadeCallback fadeCallback;
  private final Fadeable fader;

  private FadeEffect(int currentAlpha,
                     int targetAlpha,
                     int fadeTime,
                     int timeInterval,
                     Executor executor,
                     Fadeable fader,
                     FadeCallback fadeCallback,
                     LinkableEffect parent,
                     LinkableEffect[] linkableEffects) throws InvalidEffectArgumentException {
    super(parent, executor, timeInterval, linkableEffects);

    this.currentAlpha = currentAlpha;
    this.targetAlpha = targetAlpha;
    changeAlpha = currentAlpha != targetAlpha;
    this.fadeCallback = fadeCallback;
    this.fadeTime = (fadeTime == 0) ? DEFAULT_FADE_TIME : fadeTime;
    this.timeInterval = (timeInterval == 0) ? DEFAULT_TIME_INTERVAL : timeInterval;
    this.fader = fader;

    fadeStepSize = calculateFadeStepSize(this.fadeTime, this.timeInterval, this.currentAlpha, this.targetAlpha);

    if (!changeAlpha) {
      LOG.warning("This effect will not fade anything, please check arguments.");
    }
  }

  @Override
  public boolean isEffectComplete() {
    return fader.fadeComplete(targetAlpha);
  }

  @Override
  public void effectStep() {
    if (changeAlpha) {
      currentAlpha = calculateDiff(currentAlpha, targetAlpha);
    }

    fader.fade(currentAlpha);
  }

  @Override
  public void effectComplete() {
    if (fadeCallback != null) {
      fadeCallback.fadeComplete();
    }
  }

  private int calculateDiff(int current, int target) {
    int result = current;
    int diff = Math.abs(current - target);
    int alphaDiff = (diff < fadeStepSize) ? diff : fadeStepSize;
    if (current > target) {
      result = current - alphaDiff;
    }
    if (current < target) {
      result = current + alphaDiff;
    }

    return result;
  }

  private int calculateFadeStepSize(int fadeTime, int timeInterval, int currentAlpha, int targetAlpha) {
    int diffAlpha = Math.abs(targetAlpha - currentAlpha);
    double numberOfIterations = Math.ceil((double) fadeTime / (double) timeInterval);
    return Math.round(diffAlpha / (int) numberOfIterations);
  }

  /**
   * Builder used to create and customize a FadeEffect.
   */
  public static class FadeEffectBuilder {
    private int currentAlpha = 255;
    private int targetAlpha = 0;
    private int timeInterval;
    private int fadeTime;
    private FadeCallback fadeCallback;
    private Executor executor;
    private Fadeable fader;
    private LinkableEffect[] linkableEffects;
    private LinkableEffect parent;

    /**
     * (Optional) Link other effects to the FadeEffect.
     * @param linkableEffects 1 to many Effects to link
     */
    public FadeEffectBuilder setLinkableEffects(LinkableEffect ... linkableEffects) {
      this.linkableEffects = linkableEffects;
      return this;
    }

    /**
     * (Optional) Set the LinkableEffect you want run first.
     * @param parent The LinkableEffect to run first
     */
    public FadeEffectBuilder setParent(LinkableEffect parent) {
      this.parent = parent;
      return this;
    }

    /**
     * (Optional) Set the current alpha of the component you want to fade.<br/>
     * Defaults to 255 (fully opaque)
     * @param currentAlpha The current alpha of the component. SWT uses values between 255 (opaque) and 0 (transparent).
     */
    public FadeEffectBuilder setCurrentAlpha(int currentAlpha) {
      this.currentAlpha = currentAlpha;
      return this;
    }

    /**
     * (Optional) Set the target alpha to fade to.<br/>
     * Defaults to 0 (fully transparent)
     * @param targetAlpha The target alpha you want to fade to. SWT uses values between 255 (opaque) and 0 (transparent).
     * @return
     */
    public FadeEffectBuilder setTargetAlpha(int targetAlpha) {
      this.targetAlpha = targetAlpha;
      return this;
    }

    /**
     * (Optional) (Advanced) Set a custom time interval for the executor to call the <code>fade()</code> method.<br/>
     * Remember that setting this will cause <code>fadeTimeInMillseconds()</code> to behave strangely.
     * @param timeInterval A custom time interval in millseconds
     */
    public FadeEffectBuilder setTimeInterval(int timeInterval) {
      this.timeInterval = timeInterval;
      return this;
    }

    /**
     * (Optional) Set how long you want the fade effect to take.<br/>
     * Defaults to 1 second.
     * @param fadeTime The amount of time to execute the effect (in milliseconds)
     */
    public FadeEffectBuilder setFadeTimeInMilliseconds(int fadeTime) {
      this.fadeTime = fadeTime;
      return this;
    }

    /**
     * (Optional) Set a callback to be invoked when the Fade is complete.<br/>
     * Defaults to no-op.
     * @param fadeCallback a FadeCallback
     */
    public FadeEffectBuilder setFadeCallback(FadeCallback fadeCallback) {
      this.fadeCallback = fadeCallback;
      return this;
    }

    /**
     * (Optional) Sets a custom executor for the effect. See the FadeEffect class Javadoc for an example.
     * @param executor an Executor
     */
    public FadeEffectBuilder setExecutor(Executor executor) {
      this.executor = executor;
      return this;
    }

    /**
     * (Required) A class that implements the Fadeable interface.
     * @param fader The class that implements the Fadeable interface.
     */
    public FadeEffectBuilder setFadeable(Fadeable fader) {
      this.fader = fader;
      return this;
    }

    public FadeEffectBuilder() { }

    /**
     * Build the FadeEffect given the options set in the Builder.
     * @return A FadeEffect
     * @throws InvalidEffectArgumentException If you forget to call <code>setFadeable()</code>
     */
    public FadeEffect build() throws InvalidEffectArgumentException {
      if (fader == null) {
        throw new InvalidEffectArgumentException("Fader required");
      }
      return new FadeEffect(currentAlpha,
                            targetAlpha,
                            fadeTime,
                            timeInterval,
                            executor,
                            fader,
                            fadeCallback,
                            parent,
                            linkableEffects);
    }
  }

  /**
   * Implementers of this interface are called when a FadeEffect completes.
   */
  public interface FadeCallback {
    /**
     * The logic to execute when the FadeEffect is complete.
     */
    public void fadeComplete();
  }

  /**
   * Classes that need to implement a FadeEffect must implement this interface.
   */
  public interface Fadeable {
    /**
     * This method is called by the FadeEffect after each step to determine whether or not the component has
     * gone from the current to the targetAlpha.
     * @param targetAlpha The targetAlpha you defined as part of the FadeEffect.
     * @return True if the component is at the desired alpha, false otherwise (continues the FadeEffect execution).
     */
    public boolean fadeComplete(int targetAlpha);

    /**
     * This method is called on each step of the Fade.<br/>
     * This method should handle actually setting the alpha of either the <code>Shell</code> or <code>GC</code>
     * @param alpha The current alpha in the effect (will start at the target alpha and end at the target alpha).
     */
    public void fade(int alpha);
  }
}
