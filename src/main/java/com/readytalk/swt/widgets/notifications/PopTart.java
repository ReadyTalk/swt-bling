package com.readytalk.swt.widgets.notifications;

import com.readytalk.swt.widgets.CustomElementDataProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * A PopOverShell used to display an arbitrary <code>Composite</code> over a <code>Control</code> or
 * <code>CustomElementDataProvider</code>.
 */
public class PopTart extends PopOverShell {
  private Composite composite;

  /**
   * Create a PopTart that will appear over a <code>Control</code>.
   * @param parentControl The <code>Control</code> to center the PopTart on.
   */
  public static PopTart createPopTart(Control parentControl) {
    return new PopTart(parentControl, null);
  }

  /**
   * Create a PopTart that will appear over a <code>CustomElementDataProvider</code>.
   * @param customElementDataProvider The <code>CustomElementDataProvider</code> to center the PopTart on.
   */
  public static PopTart createPopTartForCustomWidget(CustomElementDataProvider customElementDataProvider) {
    return new PopTart(customElementDataProvider.getPaintedElement(), customElementDataProvider);
  }

  private PopTart(Control parentControl, CustomElementDataProvider customElementDataProvider) {
    super(parentControl, customElementDataProvider);
  }

  /**
   * Get the underlying parent <code>Shell</code> that you'll need to pass when you call <code>new Composite</code>.
   * @return The PopOver parent <code>Shell</code>
   */
  public Shell getPopTartShell() {
    return getPopOverShell();
  }

  /**
   * Inform the PopTart of the <code>Composite</code> it should contain
   * @param composite The <code>Composite</code> that appears inside the PopTart.
   */
  public void setComposite(Composite composite) {
    this.composite = composite;
  }

  Point getAppropriatePopOverSize() {
    getPopTartShell().pack();
    getPopTartShell().layout();
    return composite.getSize();
  }

  void runBeforeShowPopOverShell() { }

  void resetWidget() { }
}
