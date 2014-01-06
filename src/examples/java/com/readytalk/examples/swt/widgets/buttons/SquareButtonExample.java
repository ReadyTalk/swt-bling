package com.readytalk.examples.swt.widgets.buttons;

import com.readytalk.examples.swt.RunnableExample;
import com.readytalk.examples.swt.SwtBlingExample;
import com.readytalk.swt.widgets.buttons.SquareButton;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SquareButtonExample implements SwtBlingExample {
  @RunnableExample(name="SquareButton")
  public SquareButtonExample() { }

  public void run(Display display, Shell shell) {
    /* Colors */
    final Color offBlack = new Color(display, 74, 74, 74);
    final Color lightGray = new Color(display, 204, 204, 204);
    final Color lighterGray = new Color(display, 232, 232, 232);
    final Color lightestGray = new Color(display, 239, 239, 239);
    final Color white = new Color(display, 255, 255, 255);

    /* SquareButton */
    String buttonText = "SquareButton";
    Image buttonImage = display.getSystemImage(SWT.ICON_INFORMATION);
    SquareButton.ImagePosition BUTTON_IMAGE_POSITION = SquareButton.ImagePosition.ABOVE_TEXT;
    int CORNER_RADIUS = 3;
    SquareButton.SquareButtonColorGroup BUTTON_HOVER_COLOR_GROUP =
            new SquareButton.SquareButtonColorGroup(white, white, white, offBlack);
    SquareButton.SquareButtonColorGroup BUTTON_DEFAULT_COLOR_GROUP =
            new SquareButton.SquareButtonColorGroup(lighterGray, lightestGray, lightGray, offBlack);

    shell.setLayout(new FillLayout());
    Composite composite = new Composite(shell, SWT.NONE);
    composite.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
    FillLayout fillLayout = new FillLayout();
    fillLayout.type = SWT.VERTICAL;
    composite.setLayout(fillLayout);

    Composite topComposite = new Composite(composite, SWT.NONE);
    topComposite.setLayout(new FillLayout());
    topComposite.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
    Composite bottomComposite = new Composite(composite, SWT.NONE);
    bottomComposite.setLayout(new FormLayout());
    bottomComposite.setBackground(display.getSystemColor(SWT.COLOR_MAGENTA));

    SquareButton.SquareButtonBuilder builder = new SquareButton.SquareButtonBuilder();
    builder .setParent(topComposite)
            .setText(buttonText)
            .setImage(buttonImage)
            .setImagePosition(BUTTON_IMAGE_POSITION)
            .setCornerRadius(CORNER_RADIUS)
            .setHoverColors(BUTTON_HOVER_COLOR_GROUP)
            .setDefaultColors(BUTTON_DEFAULT_COLOR_GROUP);
    SquareButton button = builder.build();

    String bigButtonText = "Big SquareButton";
    Image bigButtonImage = display.getSystemImage(SWT.ICON_QUESTION);
    SquareButton.ImagePosition BIG_BUTTON_IMAGE_POSITION = SquareButton.ImagePosition.ABOVE_TEXT;
    int BIG_CORNER_RADIUS = 3;
    SquareButton.SquareButtonColorGroup BIG_BUTTON_HOVER_COLOR_GROUP =
      new SquareButton.SquareButtonColorGroup(white, white, white, offBlack);
    SquareButton.SquareButtonColorGroup BIG_BUTTON_DEFAULT_COLOR_GROUP =
      new SquareButton.SquareButtonColorGroup(lighterGray, lightestGray, lightGray, offBlack);

    builder = new SquareButton.SquareButtonBuilder();
    builder.setParent(bottomComposite)
      .setText(bigButtonText)
      .setImage(bigButtonImage)
      .setImagePosition(BIG_BUTTON_IMAGE_POSITION)
      .setCornerRadius(BIG_CORNER_RADIUS)
      .setHoverColors(BIG_BUTTON_HOVER_COLOR_GROUP)
      .setDefaultColors(BIG_BUTTON_DEFAULT_COLOR_GROUP);
    SquareButton bigButton = builder.build();

    FormData formData = new FormData();
    formData.left = new FormAttachment(10);
    formData.top = new FormAttachment(10);
    formData.right = new FormAttachment(90);
    formData.bottom = new FormAttachment(90);
    bigButton.setLayoutData(formData);


    shell.setSize(200, 200);
    shell.open();

    shell.addDisposeListener(new DisposeListener() {
      public void widgetDisposed(DisposeEvent e) {
        offBlack.dispose();
        lightGray.dispose();
        lighterGray.dispose();
        lightestGray.dispose();
        white.dispose();
      }
    });
  }
}
