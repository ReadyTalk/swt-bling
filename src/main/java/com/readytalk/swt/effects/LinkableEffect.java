package com.readytalk.swt.effects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.readytalk.swt.helpers.DebugHelper;
import org.eclipse.swt.widgets.Display;

public abstract class LinkableEffect implements Runnable {
  private static final Logger log = Logger.getLogger(LinkableEffect.class.getName());
  private static final int DEFAULT_TIME_INTERVAL = 10;
  protected List<LinkableEffect> effectList;
  protected Executor executor;
  protected int timeInterval;
  protected boolean continueEffects = true;

  protected EffectAbortedCallback effectAbortedCallback;

  /**
   * this determines when all effects have executed and we need to stop.  it will reset when startEffect is called again
   */
  private int effectCompleteCount;

  public LinkableEffect(LinkableEffect parent, Executor executor, int timeInterval, LinkableEffect ... linkableEffects) throws InvalidEffectArgumentException {

    if(parent != null) {
      parent.linkEffect(this);
    } else {
      this.timeInterval = (timeInterval == 0) ? DEFAULT_TIME_INTERVAL : timeInterval;

      if(executor == null) {
        executor = new Executor() {
          @Override
          public void timerExec(int time, Runnable runnable) {
            Display.getCurrent().timerExec(time, runnable);
          }

          @Override
          public void asyncExec(Runnable runnable) {
            Display.getCurrent().asyncExec(runnable);
          }
        };
      }

      this.executor = executor;
      getEffectList().add(this);
      if(linkableEffects != null) {
        getEffectList().addAll(Arrays.asList(linkableEffects));
      }
    }
  }

  /**
   * Tell the main effect if you are done.
   *
   * @return
   */
  public abstract boolean isEffectComplete();

  /**
   * This will be called at every time interval, so do your business here.  Do not quit early in this method, instead implement the
   * other two abstract methods and tell your parent when you are done.
   */
  public abstract void effectStep();

  /**
   * At the end of each of your steps, this may be called if isEffectComplete now returns true.  So do your last bits of work here.
   */
  public abstract void effectComplete();

  /**
   * We only want to call effectComplete once
   */
  protected boolean finishedEffectExecution = false;
  protected void completeEffect() {
    if(!finishedEffectExecution) {
      finishedEffectExecution = true;
      effectComplete();
    }
  }

  /**
   * this should never be overridden
   *
   * it will reset the effectCompleteCount as it will assume no effects have completed yet
   */
  public final void startEffect() {
    // we use a slight delay... this came from trial and error growing/shrinking ShareController
    effectCompleteCount = 0;
    continueEffects = true;
    executor.timerExec(1, this);
  }

  public final void stopEffect() {
    stopEffect(null);
  }

  public final void stopEffect(EffectAbortedCallback effectAbortedCallback) {
    this.effectAbortedCallback = effectAbortedCallback;
    fullStop();
  }

  protected void fullStop() {
    continueEffects = false;
    boolean callEffectComplete = true;
    if(effectAbortedCallback != null) {
      callEffectComplete = effectAbortedCallback.completeAbortion();
    }
    if(callEffectComplete) {
      for(LinkableEffect effect : effectList) {
        effect.completeEffect();
        effectCompleteCount++;
      }
    }
  }

  protected boolean shouldIStillBeRunning(LinkableEffect effect) {
    boolean keepRunning = true;
    if(!continueEffects) {
      keepRunning = false;
    } else if(effect.isEffectComplete()) {
      keepRunning = false;
    }
    return keepRunning;
  }

  /**
   * This should never be overridden
   */
  public void run() {
    for(LinkableEffect effect : effectList) {
      try {
        boolean shortCircuit = false;
        if(shouldIStillBeRunning(effect)) {
          effect.effectStep();
          // if the effect is now complete, tell the effect to finish it's business
          shortCircuit = (shouldIStillBeRunning(effect) == false);
        } else {
          shortCircuit = true;
        }

        if(shortCircuit) {
          effect.completeEffect();
          effectCompleteCount++;
        }
      } catch (Throwable t) {
        log.severe("Could not execute effect! " + effect.getClass().getCanonicalName() + " " + t.getMessage() +
                DebugHelper.getStackTraceAsString(t));
      }
    }
    if(effectCompleteCount != effectList.size()) {
      executor.timerExec(timeInterval, this);
    }
  }

  public LinkableEffect linkEffect(LinkableEffect effect) {
    getEffectList().add(effect);
    return this;
  }

  public boolean unlinkEffect(LinkableEffect effect) {
    return getEffectList().remove(effect);
  }

  public List<LinkableEffect> getEffectList() {
    if(effectList == null) {
      effectList = new ArrayList<LinkableEffect>();
    }
    return effectList;
  }

  public Executor getExecutor() {
    return executor;
  }

  public int getTimeInterval() {
    return timeInterval;
  }

  public static abstract class EffectAbortedCallback {
    protected boolean abortionComplete = false;
    protected boolean callEffectComplete = false;

    protected boolean completeAbortion() {
      if(!abortionComplete) {
        callEffectComplete = abortEffect();
        abortionComplete = true;
      }
      return callEffectComplete;
    }

    /**
     * @return Indicate if you want effectCompleteCalled after the effect is aborterd
     */
    public abstract boolean abortEffect();
  }

  public interface Executor {
    public void timerExec(final int time, final Runnable runnable);
    public void asyncExec(Runnable runnable);
  }
}
