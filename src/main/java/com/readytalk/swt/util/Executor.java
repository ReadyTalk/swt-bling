package com.readytalk.swt.util;

/**
 * An interface to support custom execution for scheduled Events. <br/>
 *
 * Default behavior in swt-bling is to schedule Runnables on Display.getCurrentDisplay().
 * Utilize this interface if you want to override this behavior based on your system threading model.
 */
public interface Executor {
  public void timerExec(final int time, final Runnable runnable);
  public void asyncExec(Runnable runnable);
}