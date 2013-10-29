package com.readytalk.swt.helpers;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class AncestryHelper {
  public static Shell getShellFromControl(Control control) {
    if (control == null) {
      return null;
    } else if (control instanceof Shell) {
      return (Shell) control;
    }

    Control currentControl = control;

    while(!(currentControl instanceof Shell)) {
      currentControl = currentControl.getParent();
    }

    return (Shell) currentControl;
  }
}
