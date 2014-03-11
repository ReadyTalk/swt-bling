package com.readytalk.swt.helpers;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

public class WidgetHelper {
  public static boolean isWidgetSafe(Widget widget) {
    return widget != null && !widget.isDisposed();
  }

  public static boolean isControlSafeAndVisible(Control control) {
    return isWidgetSafe(control) && control.isVisible();
  }
}
