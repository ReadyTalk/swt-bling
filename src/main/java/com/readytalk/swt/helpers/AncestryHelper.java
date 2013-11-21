package com.readytalk.swt.helpers;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Helper methods to answer common ancestry questions related to SWT components.<br/>
 * <br/>
 * Methods in this class should be <code>static</code>.
 */
public class AncestryHelper {
  /**
   * Helper method to determine the shell an SWT <code>Control</code> element belongs to.
   * @param control The <code>Control</code> element you want to determine what Shell it belongs to.
   * @return The <code>Shell</code> that the <code>Control</code> belongs to.
   */
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
