package com.readytalk.examples.swt.widgets.buttons;

import com.readytalk.examples.swt.RunnableExample;
import com.readytalk.examples.swt.SwtBlingExample;
import com.readytalk.swt.util.ColorFactory;
import com.readytalk.swt.widgets.buttons.SquareButton;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SquareButtonExample implements SwtBlingExample {
  @RunnableExample(name="SquareButton")
  public SquareButtonExample() { }

  public void run(Display display, Shell shell) {
    /* Colors */
    final Color offBlack = ColorFactory.getColor(display, 74, 74, 74);
    final Color lightGray = ColorFactory.getColor(display, 204, 204, 204);
    final Color lighterGray = ColorFactory.getColor(display, 232, 232, 232);
    final Color lightestGray = ColorFactory.getColor(display, 239, 239, 239);
    final Color white = ColorFactory.getColor(display, 255, 255, 255);

    /* SquareButton Resources */
    Image buttonImage = display.getSystemImage(SWT.ICON_INFORMATION);
    SquareButton.ImagePosition BUTTON_IMAGE_POSITION = SquareButton.ImagePosition.ABOVE_TEXT;
    int CORNER_RADIUS = 3;
    SquareButton.SquareButtonColorGroup BUTTON_HOVER_COLOR_GROUP =
            new SquareButton.SquareButtonColorGroup(white, white, white, offBlack);
    SquareButton.SquareButtonColorGroup BUTTON_DEFAULT_COLOR_GROUP =
            new SquareButton.SquareButtonColorGroup(lighterGray, lightestGray, lightGray, offBlack);

    shell.setLayout(new FillLayout());
    Composite composite = new Composite(shell, SWT.NONE);
    FillLayout fillLayout = new FillLayout();
    fillLayout.type = SWT.VERTICAL;
    composite.setLayout(fillLayout);

    Composite topComposite = new Composite(composite, SWT.NONE);
    topComposite.setLayout(new FillLayout());
    Composite middleComposite = new Composite(composite, SWT.NONE);
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 1;
    middleComposite.setLayout(gridLayout);
    Composite bottomComposite = new Composite(composite, SWT.NONE);
    bottomComposite.setLayout(new FormLayout());

    SquareButton.SquareButtonBuilder builder = new SquareButton.SquareButtonBuilder();
    builder .setParent(topComposite)
            .setText("Simple square button")
            .setImage(buttonImage)
            .setImagePosition(BUTTON_IMAGE_POSITION)
            .setCornerRadius(CORNER_RADIUS)
            .setHoverColors(BUTTON_HOVER_COLOR_GROUP)
            .setDefaultColors(BUTTON_DEFAULT_COLOR_GROUP);
    SquareButton button = builder.build();

    builder = new SquareButton.SquareButtonBuilder();
    builder .setParent(middleComposite)
        .setText("Wide Button")
        .setImage(buttonImage)
        .setImagePosition(BUTTON_IMAGE_POSITION)
        .setCornerRadius(CORNER_RADIUS)
        .setHoverColors(BUTTON_HOVER_COLOR_GROUP)
        .setDefaultColors(BUTTON_DEFAULT_COLOR_GROUP);
    SquareButton horizontallyStretchingButton = builder.build();
    GridData gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    horizontallyStretchingButton.setLayoutData(gridData);

    builder = new SquareButton.SquareButtonBuilder();
    builder .setParent(middleComposite)
        .setText("Tall Button")
        .setImage(buttonImage)
        .setImagePosition(BUTTON_IMAGE_POSITION)
        .setCornerRadius(CORNER_RADIUS)
        .setHoverColors(BUTTON_HOVER_COLOR_GROUP)
        .setDefaultColors(BUTTON_DEFAULT_COLOR_GROUP);
    SquareButton verticallyStretchingButton = builder.build();
    gridData = new GridData();
    gridData.grabExcessVerticalSpace = true;
    verticallyStretchingButton.setLayoutData(gridData);

    String bigButtonText = "Big Toggleable SquareButton";
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
      .setToggleable(true)
      .setDefaultColors(BIG_BUTTON_DEFAULT_COLOR_GROUP);
    SquareButton bigButton = builder.build();

    FormData formData = new FormData();
    formData.left = new FormAttachment(10);
    formData.top = new FormAttachment(10);
    formData.right = new FormAttachment(90);
    formData.bottom = new FormAttachment(90);
    bigButton.setLayoutData(formData);


    shell.setSize(300, 500);
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
