package com.readytalk.swt.widgets.notifications;

import com.readytalk.swt.widgets.CustomElementDataProvider;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;

public class PoppedOverItem {
  private Control control;
  private CustomElementDataProvider customElementDataProvider;

  public PoppedOverItem(Control control) {
    this.control = control;
  }

  public PoppedOverItem(CustomElementDataProvider customElementDataProvider) {
    this.customElementDataProvider = customElementDataProvider;
  }

  Point getSize() {
    if (control != null) {
      return control.getSize();
    } else {
      return customElementDataProvider.getSize();
    }
  }

  Point getLocation() {
    if (control != null) {
      return control.getLocation();
    } else {
      return customElementDataProvider.getLocation();
    }
  }

  Control getControl() {
    if (control != null) {
      return control;
    } else {
      return customElementDataProvider.getPaintedElement();
    }
  }

  Object getControlOrCustomElement() {
    if (customElementDataProvider != null) {
      return customElementDataProvider;
    } else {
      return control;
    }
  }

  CustomElementDataProvider getCustomElementDataProvider() {
    return customElementDataProvider;
  }
}
