package com.readytalk.swt.widgets.buttons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SquareButtonGroupIntegTest {

  private Display display;
  private Shell shell;

  private SquareButton toggleOne, toggleTwo, toggleThree, nonToggled;

  private SquareButtonGroup group;

  @Before
  public void setUp() {
    display = Display.getDefault();
    shell = new Shell(display);
    shell.setLayout(new FillLayout());


    SquareButton.SquareButtonBuilder builder = new SquareButton.SquareButtonBuilder();
    builder.setParent(shell)
      .setText("one")
      .setToggleable(true);
    toggleOne = builder.build();

    builder = new SquareButton.SquareButtonBuilder();
    builder.setParent(shell)
      .setText("two")
      .setToggleable(true);
    toggleTwo = builder.build();

    builder = new SquareButton.SquareButtonBuilder();
    builder.setParent(shell)
      .setText("three")
      .setToggleable(true);
    toggleThree = builder.build();

    builder = new SquareButton.SquareButtonBuilder();
    builder.setParent(shell)
      .setText("non toggle");
    nonToggled = builder.build();

    group = new SquareButtonGroup(toggleOne, toggleTwo, toggleThree, nonToggled);

    shell.open();
  }

  @Test
  public void testDefaultToggle() {
    Assert.assertTrue(toggleOne.isToggled());
    Assert.assertFalse(toggleTwo.isToggled());
    Assert.assertFalse(toggleThree.isToggled());
    Assert.assertFalse(nonToggled.isToggled());
  }

  @Test
  public void testBasicToggle() {
    toggleTwo.notifyListeners(SWT.MouseUp, new Event());
    Assert.assertFalse(toggleOne.isToggled());
    Assert.assertTrue(toggleTwo.isToggled());
    Assert.assertFalse(toggleThree.isToggled());
    Assert.assertFalse(nonToggled.isToggled());
  }

  @Test
  public void testDoubleToggle() {
    toggleTwo.notifyListeners(SWT.MouseUp, new Event());
    Assert.assertFalse(toggleOne.isToggled());
    Assert.assertTrue(toggleTwo.isToggled());
    Assert.assertFalse(toggleThree.isToggled());
    Assert.assertFalse(nonToggled.isToggled());

    toggleTwo.notifyListeners(SWT.MouseUp, new Event());
    Assert.assertFalse(toggleOne.isToggled());
    Assert.assertTrue(toggleTwo.isToggled());
    Assert.assertFalse(toggleThree.isToggled());
    Assert.assertFalse(nonToggled.isToggled());
  }

  @Test
  public void testForcedSelection() {
    group.setCurrentlyToggledButton(toggleThree);
    Assert.assertFalse(toggleOne.isToggled());
    Assert.assertFalse(toggleTwo.isToggled());
    Assert.assertTrue(toggleThree.isToggled());
    Assert.assertFalse(nonToggled.isToggled());
  }

  @After
  public void tearDown() {
    shell.close();
    display.dispose();
  }
}
