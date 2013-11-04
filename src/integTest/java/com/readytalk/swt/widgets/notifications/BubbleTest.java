package com.readytalk.swt.widgets.notifications;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BubbleTest {
  private static final String BUBBLE_TEXT = "Testing tooltip. It's quite concise, no?";

  Display display;
  Shell shell;
  Button button;
  Bubble bubble;

  @Before
  public void setUp() {
    display = new Display();
    shell = new Shell(display);
    shell.setLayout(new FillLayout());
    Composite composite = new Composite(shell, SWT.NONE);
    composite.setLayout(new GridLayout());

    button = new Button(composite, SWT.PUSH);
    button.setText("Testing Button");

    bubble = new Bubble(button, BUBBLE_TEXT);
    button.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, true, true));
    shell.pack();
    shell.layout();
  }

  @After
  public void tearDown() {
    shell.dispose();
    display.dispose();
  }

  @Test
  public void bubble_anyParameters_hasDefaultBubbleDisplayLocationBelowAndCenteredOnParent() {
    assertEquals(bubble.bubbleDisplayLocation, Bubble.BubbleDisplayLocation.BELOW_PARENT);
    assertEquals(bubble.bubbleTopLeftCornerLocation, Bubble.BubbleTopLeftCornerLocation.CENTER_OF_PARENT);
  }

  @Test
  public void bubble_textCutOffBottomOfScreen_hasBubbleDisplayLocationAbove() {
    shell.setLocation(getShellLocationForBubbleTextCutoff(BubbleTextCutoffPosition.BOTTOM));
    bubble.show();

    assertEquals(bubble.bubbleDisplayLocation, Bubble.BubbleDisplayLocation.ABOVE_PARENT);
  }

  @Test
  public void bubble_textCutOffRightOfScreen_hasTopLeftLocationLeftOfParent() {
    shell.setLocation(getShellLocationForBubbleTextCutoff(BubbleTextCutoffPosition.RIGHT));
    bubble.show();

    shell.open();

    assertEquals(bubble.bubbleTopLeftCornerLocation, Bubble.BubbleTopLeftCornerLocation.LEFT_OF_PARENT);
  }

  private enum BubbleTextCutoffPosition { BOTTOM, RIGHT }
  private Point getShellLocationForBubbleTextCutoff(BubbleTextCutoffPosition cutoffPosition) {
    Point appropriateShellLocation = null;

    Rectangle displayBounds = display.getClientArea();
    Rectangle buttonSize = button.getBounds();
    switch (cutoffPosition) {
      case BOTTOM:
        appropriateShellLocation = new Point(displayBounds.width - buttonSize.width,
                displayBounds.height - buttonSize.height);
        break;
      case RIGHT:
        appropriateShellLocation = new Point(displayBounds.width - buttonSize.width,
                displayBounds.height / 2);
        break;
    }

    return appropriateShellLocation;
  }
}
