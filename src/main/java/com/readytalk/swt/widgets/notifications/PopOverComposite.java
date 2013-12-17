package com.readytalk.swt.widgets.notifications;

import com.readytalk.swt.widgets.CustomElementDataProvider;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * A PopOverShell used to display an arbitrary <code>Composite</code> over a <code>Control</code> or
 * <code>CustomElementDataProvider</code>.
 * <pre>
 *   Most commonly, you'll use PopOverComposite like this:
 *   <code>
 *     Button button = new Button(shell, SWT.PUSH);
 *     PopOverComposite popTart = PopOverComposite.createPopOverComposite(button);
 *     Composite composite = new Composite(popTart.getPopOverCompositeShell, SWT.NONE);
 *     // add components to the composite
 *     popTart.setComposite(composite);
 *     button.addSelectionListener(new SelectionAdapter() {
 *       public void widgetSelected(SelectionEvent e) {
 *         popTart.toggle();
 *       }
 *     });
 *   </code>
 * </pre>
 */
public class PopOverComposite extends PopOverShell {
  private Composite composite;

  /**
   * Create a PopOverComposite that will appear over a <code>Control</code>.
   * @param parentControl The <code>Control</code> to center the PopOverComposite on.
   */
  public static PopOverComposite createPopOverComposite(Control parentControl) {
    return new PopOverComposite(parentControl, null);
  }

  /**
   * Create a PopOverComposite that will appear over a <code>CustomElementDataProvider</code>.
   * @param customElementDataProvider The <code>CustomElementDataProvider</code> to center the PopOverComposite on.
   */
  public static PopOverComposite createPopOverCompositeForCustomWidget(CustomElementDataProvider customElementDataProvider) {
    return new PopOverComposite(customElementDataProvider.getPaintedElement(), customElementDataProvider);
  }

  private PopOverComposite(Control parentControl, CustomElementDataProvider customElementDataProvider) {
    super(parentControl, customElementDataProvider);
  }

  /**
   * Get the underlying parent <code>Shell</code> that you'll need to pass when you call <code>new Composite</code>.
   * @return The PopOver parent <code>Shell</code>
   */
  public Shell getPopOverCompositeShell() {
    return getPopOverShell();
  }

  /**
   * Inform the PopOverComposite of the <code>Composite</code> it should contain
   * @param composite The <code>Composite</code> that appears inside the PopOverComposite.
   */
  public void setComposite(Composite composite) {
    this.composite = composite;
  }

  Point getAppropriatePopOverSize() {
    getPopOverCompositeShell().pack();
    getPopOverCompositeShell().layout();
    return composite.getSize();
  }

  void runBeforeShowPopOverShell() { }

  void resetWidget() { }

  void widgetDispose() { }
}
