package com.readytalk.swt.util;

import org.eclipse.swt.widgets.Display;

public class DisplaySafe {
  private Display display;

  public DisplaySafe(){
    setDisplay(Display.getCurrent());
  }

  public DisplaySafe(Display display) {
    setDisplay(display);
  }

  public Display getDisplay() {
    return display;
  }

  public Display getLatestDisplay() throws NullDisplayException {
    Display display = getCurrent();
    if(display != null && display != this.display) {
      setDisplay(display);
    } else if(this.display == null) {
      throw new NullDisplayException();
    }
    return this.display;
  }

  void setDisplay(Display display) {
    this.display = display;
  }

  Display getCurrent() {
    return Display.getCurrent();
  }

  public static class NullDisplayException extends Exception {
    NullDisplayException() {
      super("No non-null display could be found");
    }
  }
}
