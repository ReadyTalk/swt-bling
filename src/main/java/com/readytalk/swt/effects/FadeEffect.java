package com.readytalk.swt.effects;

import java.util.logging.Logger;

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

  public static class FadeEffectBuilder {
    private int currentAlpha;
    private int targetAlpha;
    private int timeInterval;
    private int fadeTime;
    private FadeCallback fadeCallback;
    private Executor executor;
    private Fadeable fader;
    private LinkableEffect[] linkableEffects;
    private LinkableEffect parent;

    public FadeEffectBuilder setLinkableEffects(LinkableEffect ... linkableEffects) {
      this.linkableEffects = linkableEffects;
      return this;
    }

    public FadeEffectBuilder setParent(LinkableEffect parent) {
      this.parent = parent;
      return this;
    }

    public FadeEffectBuilder setCurrentAlpha(int currentAlpha) {
      this.currentAlpha = currentAlpha;
      return this;
    }

    public FadeEffectBuilder setTargetAlpha(int targetAlpha) {
      this.targetAlpha = targetAlpha;
      return this;
    }

    public FadeEffectBuilder setTimeInterval(int timeInterval) {
      this.timeInterval = timeInterval;
      return this;
    }

    public FadeEffectBuilder setFadeTimeInMilliseconds(int fadeTime) {
      this.fadeTime = fadeTime;
      return this;
    }

    public FadeEffectBuilder setFadeCallback(FadeCallback fadeCallback) {
      this.fadeCallback = fadeCallback;
      return this;
    }

    public FadeEffectBuilder setExecutor(Executor executor) {
      this.executor = executor;
      return this;
    }

    public FadeEffectBuilder setFadeable(Fadeable fader) {
      this.fader = fader;
      return this;
    }

    public FadeEffectBuilder() { }

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

  public interface FadeCallback {
    public void fadeComplete();
  }

  public interface Fadeable {
    public boolean fadeComplete(int targetAlpha);
    public void fade(int alpha);
  }
}
