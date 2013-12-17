package com.readytalk.examples.swt.widgets.notifications;

import com.readytalk.examples.swt.RunnableExample;
import com.readytalk.examples.swt.SwtBlingExample;
import com.readytalk.swt.widgets.buttons.SquareButton;
import com.readytalk.swt.widgets.notifications.PopTart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class PopTartExample implements SwtBlingExample {
  @RunnableExample(name="PopTart")
  public PopTartExample() { }

  public void run(Display display, Shell shell) {
    shell.setLayout(new FillLayout());
    Button button = new Button(shell, SWT.PUSH);
    button.setText("Open PopTart");

    final PopTart popTart = PopTart.createPopTart(button);
    button.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        popTart.show();
      }
    });

    Composite composite = new Composite(popTart.getPopTartShell(), SWT.NONE);
    composite.setLayout(new GridLayout(2, true));

    Image iconInformation = display.getSystemImage(SWT.ICON_INFORMATION);
    Image iconError = display.getSystemImage(SWT.ICON_ERROR);
    Image iconWarning = display.getSystemImage(SWT.ICON_WARNING);

    new SquareButton.SquareButtonBuilder()
            .setImage(iconInformation)
            .setImagePosition(SquareButton.ImagePosition.LEFT_OF_TEXT)
            .setText("This button provides you with some information.")
            .setParent(composite)
            .build();

    new SquareButton.SquareButtonBuilder()
            .setImage(iconWarning)
            .setImagePosition(SquareButton.ImagePosition.LEFT_OF_TEXT)
            .setText("You should be cautious clicking this button.")
            .setParent(composite)
            .build();

    new SquareButton.SquareButtonBuilder()
            .setImage(iconError)
            .setImagePosition(SquareButton.ImagePosition.LEFT_OF_TEXT)
            .setText("This button will produce an error.")
            .setParent(composite)
            .build();

    new SquareButton.SquareButtonBuilder()
            .setImage(iconError)
            .setImagePosition(SquareButton.ImagePosition.LEFT_OF_TEXT)
            .setText("This button is bad news.")
            .setParent(composite)
            .build();

    popTart.setComposite(composite);

    shell.pack();
    shell.open();
  }
}
