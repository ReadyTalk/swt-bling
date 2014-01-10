package com.readytalk.swt.widgets.buttons;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;

public class SquareButtonGroupIntegTest {

  private Display display;
  private Shell shell;

  private SquareButton toggleOne, toggleTwo, toggleThree, nonToggled;

  @Before
  public void setUp() {
    display = Display.getDefault();
    shell = new Shell(display);

    shell.open();
  }

  @After
  public void tearDown() {
    shell.close();
    display.dispose();
  }
}
