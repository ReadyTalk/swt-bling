package com.readytalk.swt.util;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

public class DisplaySafe {
  private Display display;

  private DisplaySafe(Display display) {
    this.display = display;
  }

  public Display getDisplay() {
    return display;
  }

  public Display getLatestDisplay() throws NullDisplayException {
    Display display = Display.getCurrent();
    if(display != this.display) {
      this.display = display;
    } else if(this.display == null) {
      throw new NullDisplayException();
    }
    return this.display;
  }

  public static class Builder {
    private Display display;
    private Control control;
    private Widget widget;

    public Builder setDisplay(Display display) {
      this.display = display;
      return this;
    }

    public Builder setControl(Control control) {
      this.control = control;
      return this;
    }

    public Builder setWidget(Widget widget) {
      this.widget = widget;
      return this;
    }

    public DisplaySafe build() {
      DisplaySafe safeDisplay = null;

      Display display = null;

      if(this.display != null) {
        display = this.display;
      } else if(control != null) {
        display = control.getDisplay();
      } else if(widget != null) {
        display = widget.getDisplay();
      } else {
        display = Display.getCurrent();
      }

      safeDisplay = new DisplaySafe(display);

      return safeDisplay;
    }
  }

  public static class NullDisplayException extends Exception {
    NullDisplayException() {
      super("No non-null display could be found");
    }
  }
}
