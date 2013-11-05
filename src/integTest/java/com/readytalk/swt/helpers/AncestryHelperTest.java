package com.readytalk.swt.helpers;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

public class AncestryHelperTest {
  Display display;

  @Before
  public void setUp() {
    display = Display.getDefault();
  }

  @Test
  public void getShellFromControl_argumentIsShell_returnsShellFromArgument() {
    Shell shell = new Shell(display);

    assertEquals(shell, AncestryHelper.getShellFromControl(shell));
  }

  @Test
  public void getShellFromControl_argumentIsNull_returnsNull() {
    assertEquals(null, AncestryHelper.getShellFromControl(null));
  }

  @Test
  public void getShellFromControl_argumentIsNestedOneDeep_returnsParentShell() {
    Shell shell = new Shell(display);
    Composite composite = new Composite(shell, SWT.NONE);

    assertEquals(shell, AncestryHelper.getShellFromControl(composite));
  }

  @Test
  public void getShellFromControl_argumentIsButtonNestedTwoDeep_returnsParentShell() {
    Shell shell = new Shell(display);
    Composite composite = new Composite(shell, SWT.NONE);
    Button button = new Button(composite, SWT.PUSH);

    assertEquals(shell, AncestryHelper.getShellFromControl(button));
  }
}
