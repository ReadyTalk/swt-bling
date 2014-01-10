package com.readytalk.swt.widgets.buttons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Assert;
import org.junit.Test;

public class SquareButtonIntegTest {

  private String buttonText = "Text";


  @Test
  public void testTextSetting() {
    Display display = Display.getDefault();
    Shell shell = new Shell(display);

    shell.open();

    SquareButton button = new SquareButton(shell, SWT.CENTER);
    button.setText(buttonText);

    Assert.assertEquals(buttonText, button.getText());

    shell.close();
    display.dispose();
  }

  @Test
  public void testSizing() {

  }

  @Test
  public void testSizeHint() {

  }

  @Test
  public void testToggleable() {
    Display display = Display.getDefault();
    Shell shell = new Shell(display);

    shell.open();

    SquareButton button = new SquareButton(shell, SWT.CENTER);
    button.setText(buttonText);
    button.setToggleable(true);

    Assert.assertTrue(button.isToggleable());

    button.setToggled(true);
    Assert.assertTrue(button.isToggled());


    button.setToggled(false);
    Assert.assertFalse(button.isToggled());

    shell.close();
    display.dispose();
  }
}
