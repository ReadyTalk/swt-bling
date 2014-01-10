package com.readytalk.examples.swt.widgets.buttons;

import com.readytalk.examples.swt.RunnableExample;
import com.readytalk.examples.swt.SwtBlingExample;
import com.readytalk.swt.util.ColorFactory;
import com.readytalk.swt.widgets.buttons.SquareButton;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class SquareButtonExample implements SwtBlingExample {

  @RunnableExample(name="SquareButton")
  public SquareButtonExample() { }

  public void run(Display display, final Shell shell) {
    /* Colors */
    final Color offBlack = ColorFactory.getColor(display, 74, 74, 74);
    final Color lightGray = ColorFactory.getColor(display, 204, 204, 204);
    final Color lighterGray = ColorFactory.getColor(display, 232, 232, 232);
    final Color lightestGray = ColorFactory.getColor(display, 239, 239, 239);
    final Color white = ColorFactory.getColor(display, 255, 255, 255);

    final Color lightMagenta = ColorFactory.getColor(display, 255, 0, 225);
    final Color lighterMagenta = ColorFactory.getColor(display, 255, 92, 236);
    final Color lightestMagenta = ColorFactory.getColor(display, 255, 184, 247);

    final Color lightAqua = ColorFactory.getColor(display, 0, 255, 168);
    final Color lighterAqua = ColorFactory.getColor(display, 102, 255, 204);
    final Color lightestAqua = ColorFactory.getColor(display, 163, 247, 219);

    final Color lightRed = ColorFactory.getColor(display, 255, 0, 0);
    final Color lighterRed = ColorFactory.getColor(display, 255, 112, 112);
    final Color lightestRed = ColorFactory.getColor(display, 255, 199, 199);

    /* SquareButton Resources */
    Image buttonImage = display.getSystemImage(SWT.ICON_INFORMATION);
    SquareButton.ImagePosition BUTTON_IMAGE_POSITION = SquareButton.ImagePosition.ABOVE_TEXT;
    int CORNER_RADIUS = 3;
    SquareButton.SquareButtonColorGroup BUTTON_HOVER_COLOR_GROUP =
            new SquareButton.SquareButtonColorGroup(white, white, white, offBlack);
    SquareButton.SquareButtonColorGroup BUTTON_DEFAULT_COLOR_GROUP =
            new SquareButton.SquareButtonColorGroup(lighterGray, lightestGray, lightGray, offBlack);

    SquareButton.SquareButtonColorGroup FUN_BUTTON_HOVER_COLOR_GROUP =
        new SquareButton.SquareButtonColorGroup(lightAqua, lightestAqua, lighterAqua, offBlack);
    SquareButton.SquareButtonColorGroup FUN_BUTTON_DEFAULT_COLOR_GROUP =
        new SquareButton.SquareButtonColorGroup(lighterAqua, lightestAqua, lightAqua, offBlack);
    SquareButton.SquareButtonColorGroup FUN_BUTTON_SELECTED_COLOR_GROUP =
        new SquareButton.SquareButtonColorGroup(lighterAqua, lightestAqua, lightAqua, white);

    SquareButton.SquareButtonColorGroup MORE_FUN_BUTTON_HOVER_COLOR_GROUP =
        new SquareButton.SquareButtonColorGroup(lightMagenta, lightestMagenta, lighterMagenta, offBlack);
    SquareButton.SquareButtonColorGroup MORE_FUN_BUTTON_DEFAULT_COLOR_GROUP =
        new SquareButton.SquareButtonColorGroup(lighterMagenta, lightestMagenta, lightMagenta, offBlack);
    SquareButton.SquareButtonColorGroup MORE_FUN_BUTTON_SELECTED_COLOR_GROUP =
        new SquareButton.SquareButtonColorGroup(lighterMagenta, lightestMagenta, lightMagenta, lightMagenta);

    SquareButton.SquareButtonColorGroup FUN_BUTTON_CLICK_COLOR_GROUP =
        new SquareButton.SquareButtonColorGroup(lighterRed, lightRed, lightestRed, white);

    shell.setLayout(new FillLayout());
    Composite composite = new Composite(shell, SWT.NONE);
    FormLayout formLayout = new FormLayout();
    composite.setLayout(formLayout);

    Composite leftComposite = new Composite(composite, SWT.NONE);
    leftComposite.setLayout(new FillLayout());

    FormData formData = new FormData();
    formData.left = new FormAttachment(10);
    formData.top = new FormAttachment(10);
    formData.bottom = new FormAttachment(90);
    formData.width = 350;
    leftComposite.setLayoutData(formData);


    Composite middleComposite = new Composite(composite, SWT.NONE);
    middleComposite.setLayout(new FillLayout());

    formData = new FormData();
    formData.left = new FormAttachment(leftComposite, 10);
    formData.top = new FormAttachment(10);
    formData.bottom = new FormAttachment(90);
    middleComposite.setLayoutData(formData);

    Composite rightComposite = new Composite(composite, SWT.NONE);
    rightComposite.setLayout(new FillLayout());

    formData = new FormData();
    formData.left = new FormAttachment(middleComposite, 10);
    formData.right = new FormAttachment(90);
    formData.top = new FormAttachment(10);
    formData.bottom = new FormAttachment(90);
    rightComposite.setLayoutData(formData);

    Group topGroup = new Group(leftComposite, SWT.SHADOW_ETCHED_IN);
    topGroup.setText("Common Use Case");
    topGroup.setLayout(new FormLayout());

    Label basicButtonLabel = new Label(topGroup, SWT.WRAP);
    basicButtonLabel.setText("This is a simple button in a form layout.  If you add listeners, be sure to handle both keyed events and mouse events.");

    formData = new FormData();
    formData.left = new FormAttachment(5);
    formData.right = new FormAttachment(95);
    formData.top = new FormAttachment(5);
    basicButtonLabel.setLayoutData(formData);

    SquareButton.SquareButtonBuilder builder = new SquareButton.SquareButtonBuilder();
    builder .setParent(topGroup)
            .setText("Press It Now!")
            .setImage(display.getSystemImage(SWT.ICON_WARNING))
            .setImagePosition(SquareButton.ImagePosition.LEFT_OF_TEXT)
            .setCornerRadius(CORNER_RADIUS)
            .setHoverColors(BUTTON_HOVER_COLOR_GROUP)
            .setDefaultColors(BUTTON_DEFAULT_COLOR_GROUP)
            .setClickedColors(FUN_BUTTON_CLICK_COLOR_GROUP)
            .setDefaultMouseClickAndReturnKeyHandler(new SquareButton.DefaultButtonClickHandler() {
              @Override
              public void clicked() {
                openTestDialog(shell);
              }
            });
    SquareButton button = builder.build();

    formData = new FormData();
    formData.left = new FormAttachment(30);
    formData.right = new FormAttachment(70);
    formData.top = new FormAttachment(basicButtonLabel, 5);
    button.setLayoutData(formData);

    topGroup.pack();

    Group middleGroup = new Group(middleComposite, SWT.SHADOW_ETCHED_IN);
    middleGroup.setText("Other Buttons");
    middleGroup.setLayout(new GridLayout());

    Label gridedButtonsLabel = new Label(middleGroup, SWT.WRAP);
    gridedButtonsLabel.setText("In a GridLayout buttons look a little different, depending on your GridData.  You can also change colors with buttons or use them without images.");
    GridData gridData = new GridData();
    gridData.widthHint = 400;
    gridedButtonsLabel.setLayoutData(gridData);

    builder = new SquareButton.SquareButtonBuilder();
    builder .setParent(middleGroup)
        .setText("This is a rather wide button!")
        .setCornerRadius(CORNER_RADIUS)
        .setHoverColors(FUN_BUTTON_HOVER_COLOR_GROUP)
        .setDefaultColors(FUN_BUTTON_DEFAULT_COLOR_GROUP)
        .setSelectedColors(FUN_BUTTON_SELECTED_COLOR_GROUP)
        .setClickedColors(FUN_BUTTON_CLICK_COLOR_GROUP)
        .setDefaultMouseClickAndReturnKeyHandler(new SquareButton.DefaultButtonClickHandler() {
          @Override
          public void clicked() {
            openTestDialog(shell);
          }
        });
    SquareButton horizontallyStretchingButton = builder.build();
    gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    gridData.horizontalAlignment = SWT.CENTER;
    horizontallyStretchingButton.setLayoutData(gridData);

    builder = new SquareButton.SquareButtonBuilder();
    builder .setParent(middleGroup)
        .setText("Tall Button")
        .setImage(buttonImage)
        .setImagePosition(BUTTON_IMAGE_POSITION)
        .setCornerRadius(CORNER_RADIUS)
        .setHoverColors(MORE_FUN_BUTTON_HOVER_COLOR_GROUP)
        .setDefaultColors(MORE_FUN_BUTTON_DEFAULT_COLOR_GROUP)
        .setClickedColors(FUN_BUTTON_CLICK_COLOR_GROUP)
        .setSelectedColors(MORE_FUN_BUTTON_SELECTED_COLOR_GROUP)
        .setDefaultMouseClickAndReturnKeyHandler(new SquareButton.DefaultButtonClickHandler() {
          @Override
          public void clicked() {
            openTestDialog(shell);
          }
        });
    SquareButton verticallyStretchingButton = builder.build();
    gridData = new GridData();
    gridData.grabExcessVerticalSpace = true;
    gridData.horizontalAlignment = SWT.CENTER;
    verticallyStretchingButton.setLayoutData(gridData);

    middleGroup.pack();

    Group rightGroup = new Group(rightComposite, SWT.SHADOW_ETCHED_IN);
    rightGroup.setText("Radio Like Buttons");
    rightGroup.setLayout(new FormLayout());

    Label rightButtonsLabel = new Label(rightGroup, SWT.WRAP);
    rightButtonsLabel.setText("Setting your toggleable (via setToggleable(true)) can allow you to mimic radio button behavior, if you tie the listeners together.");
    formData = new FormData();
    formData.left = new FormAttachment(5);
    formData.top = new FormAttachment(5);
    formData.right = new FormAttachment(95);
    rightButtonsLabel.setLayoutData(formData);

    String bigButtonTextOne = "Toggleable Radio-like Button One";
    String bigButtonTextTwo = "Toggleable Radio-like Button Two";
    Image bigButtonImage = display.getSystemImage(SWT.ICON_ERROR);
    SquareButton.ImagePosition BIG_BUTTON_IMAGE_POSITION = SquareButton.ImagePosition.LEFT_OF_TEXT;
    int BIG_CORNER_RADIUS = 3;
    SquareButton.SquareButtonColorGroup BIG_BUTTON_HOVER_COLOR_GROUP =
      new SquareButton.SquareButtonColorGroup(white, white, white, offBlack);
    SquareButton.SquareButtonColorGroup BIG_BUTTON_DEFAULT_COLOR_GROUP =
      new SquareButton.SquareButtonColorGroup(lighterGray, lightestGray, lightGray, offBlack);

    builder = new SquareButton.SquareButtonBuilder();
    builder.setParent(rightGroup)
      .setText(bigButtonTextOne)
      .setImage(bigButtonImage)
      .setImagePosition(BIG_BUTTON_IMAGE_POSITION)
      .setCornerRadius(BIG_CORNER_RADIUS)
      .setHoverColors(BIG_BUTTON_HOVER_COLOR_GROUP)
      .setToggleable(true)
      .setDefaultColors(BIG_BUTTON_DEFAULT_COLOR_GROUP);
    final SquareButton bigButtonOne = builder.build();

    formData = new FormData();
    formData.left = new FormAttachment(10);
    formData.top = new FormAttachment(rightButtonsLabel, 10);
    formData.right = new FormAttachment(90);
    bigButtonOne.setLayoutData(formData);

    builder = new SquareButton.SquareButtonBuilder();
    builder.setParent(rightGroup)
      .setText(bigButtonTextTwo)
      .setImage(bigButtonImage)
      .setImagePosition(BIG_BUTTON_IMAGE_POSITION)
      .setCornerRadius(BIG_CORNER_RADIUS)
      .setHoverColors(BIG_BUTTON_HOVER_COLOR_GROUP)
      .setToggleable(true)
      .setDefaultColors(BIG_BUTTON_DEFAULT_COLOR_GROUP);
    final SquareButton bigButtonTwo = builder.build();

    formData = new FormData();
    formData.left = new FormAttachment(10);
    formData.top = new FormAttachment(bigButtonOne, 10);
    formData.right = new FormAttachment(90);
    bigButtonTwo.setLayoutData(formData);

    bigButtonOne.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseUp(MouseEvent e) {
        if(bigButtonTwo.isToggled()) {
          bigButtonTwo.setToggled(false);
        }
      }
    });

    bigButtonOne.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.character) {
          case ' ':
          case '\r':
          case '\n':
            if(bigButtonTwo.isToggled()) {
              bigButtonTwo.setToggled(false);
            }
            break;
          default:
            break;
        }
      }
    });

    bigButtonTwo.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseUp(MouseEvent e) {
        if(bigButtonOne.isToggled()) {
          bigButtonOne.setToggled(false);
        }
      }
    });

    bigButtonTwo.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.character) {
          case ' ':
          case '\r':
          case '\n':
            if(bigButtonOne.isToggled()) {
              bigButtonOne.setToggled(false);
            }
            break;
          default:
            break;
        }
      }
    });

    bigButtonOne.setToggled(true);
//    shell.setSize(1200, 200);

    topGroup.pack();
    leftComposite.pack();
    middleGroup.pack();
    middleComposite.pack();
    rightComposite.pack();
    composite.pack();
    shell.pack();


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

  void openTestDialog(Shell shell) {
    MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
    messageBox.setText("Yo!");
    messageBox.setMessage("Click to dismiss.");
    messageBox.open();
  }
}
