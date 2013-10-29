package com.readytalk.swt.widgets.notifications;

import org.eclipse.swt.widgets.Widget;

public class Bubble extends Widget {
  private String tooltipText;

  public Bubble(Widget parent, String tooltipText, int style) {
    super(parent, style);

    this.tooltipText = tooltipText;
  }
}