package com.readytalk.swt.widgets.notifications;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

public class Bubble extends Widget {
  private static final RGB BACKGROUND_COLOR = new RGB(74, 74, 74);

  private Listener listener;
  private String tooltipText;
  private Shell tooltip;

  public Bubble(Widget parent, String tooltipText, int style) {
    super(parent, style);

    this.tooltipText = tooltipText;
    tooltip = new Shell((Shell) parent, SWT.ON_TOP | SWT.NO_TRIM);
    tooltip.setBackground(new Color(parent.getDisplay(), BACKGROUND_COLOR)); // TODO: we need to manage our colors onDispose

    listener = new Listener() {
      public void handleEvent(Event event) {
        switch (event.type) {
          case SWT.Dispose:
            onDispose(event);
            break;
          case SWT.Paint:
            onPaint(event);
            break;
          case SWT.MouseDown:
            onMouseDown(event);
            break;
        }
      }
    };
    addListener(SWT.Dispose, listener);
    tooltip.addListener(SWT.Paint, listener);
    tooltip.addListener(SWT.MouseDown, listener);
  }

  private void onDispose(Event event) {
    // TODO: dispose all the things here
  }

  private void onPaint(Event event) {
    // TODO: paint things
  }

  private void onMouseDown(Event event) {
    // TODO: dismiss the tooltip if they click it
  }
}