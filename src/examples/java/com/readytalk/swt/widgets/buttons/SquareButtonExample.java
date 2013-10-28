package com.readytalk.swt.widgets.buttons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SquareButtonExample {
  public static final Display DISPLAY = new Display();

  /* Colors */
  public static final Color OFF_BLACK = new Color(DISPLAY, 74, 74, 74);
  public static final Color LIGHT_GRAY = new Color(DISPLAY, 204, 204, 204);
  public static final Color LIGHTER_GRAY = new Color(DISPLAY, 232, 232, 232);
  public static final Color LIGHTEST_GRAY = new Color(DISPLAY, 239, 239, 239);
  public static final Color WHITE = new Color(DISPLAY, 255, 255, 255);

  /* SquareButton */
  public static final String BUTTON_TEXT = "SquareButton";
  public static final Image BUTTON_IMAGE = DISPLAY.getSystemImage(SWT.ICON_INFORMATION);
  public static final SquareButton.ImagePosition BUTTON_IMAGE_POSITION = SquareButton.ImagePosition.ABOVE_TEXT;
  public static final int CORNER_RADIUS = 3;
  public static final SquareButton.SquareButtonColorGroup BUTTON_HOVER_COLOR_GROUP =
          new SquareButton.SquareButtonColorGroup(LIGHTER_GRAY, LIGHTEST_GRAY, LIGHT_GRAY, OFF_BLACK);
  public static final SquareButton.SquareButtonColorGroup BUTTON_DEFAULT_COLOR_GROUP =
          new SquareButton.SquareButtonColorGroup(WHITE, WHITE, WHITE, OFF_BLACK);

  public static void main(String[] args) {
    Shell shell = new Shell(DISPLAY);
    shell.setLayout(new FillLayout());
    Composite composite = new Composite(shell, SWT.NONE);
    composite.setLayout(new GridLayout());

    SquareButton.SquareButtonBuilder builder = new SquareButton.SquareButtonBuilder();
    builder .setParent(composite)
            .setText(BUTTON_TEXT)
            .setImage(BUTTON_IMAGE)
            .setImagePosition(BUTTON_IMAGE_POSITION)
            .setCornerRadius(CORNER_RADIUS)
            .setHoverColors(BUTTON_HOVER_COLOR_GROUP)
            .setDefaultColors(BUTTON_DEFAULT_COLOR_GROUP);
    SquareButton button = builder.build();

    button.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

    shell.setSize(200, 200);
    shell.open();
    while (!shell.isDisposed()) {
      if (!DISPLAY.readAndDispatch()) {
        DISPLAY.sleep();
      }
    }

    DISPLAY.dispose();
  }
}