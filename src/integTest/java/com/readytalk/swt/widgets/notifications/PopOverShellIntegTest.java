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
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class PopOverShellIntegTest {
  static final String BUBBLE_TEXT = "Testing tooltip. It's quite concise, no?";

  static Display display;
  static Shell shell;
  static Button button;
  static PopOverShell popOverShell;
  static boolean initialized = false;

  private static void initialize() {
    if (needsInitialization()) {
      display = Display.getDefault();
      shell = new Shell(display);
      shell.setLayout(new FillLayout());
      Composite composite = new Composite(shell, SWT.NONE);
      composite.setLayout(new GridLayout());

      button = new Button(composite, SWT.PUSH);
      button.setText("Testing Button");

      popOverShell = Bubble.createBubble(button, BUBBLE_TEXT);
      button.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, true, true));
      shell.pack();
      shell.layout();

      initialized = true;
    }
  }

  private static boolean needsInitialization() {
    if (!initialized || display.isDisposed() || shell.isDisposed()) {
      return true;
    } else {
      return false;
    }
  }

  public static void main(String[] args) {
    initialize();
    shell.open();
    shell.setLocation(2540, 678);

//    shell.addListener(SWT.Move, new Listener() {
//      @Override
//      public void handleEvent(Event event) {
//        System.out.println(shell.getBounds());
//      }
//    });

    while (shell.isVisible()) {
      display.readAndDispatch();
    }
  }

  @RunWith(Parameterized.class)
  public static class PopOverShellPlacementTests {
    private static final int SHELL_OFFSCREEN_PADDING = 20;

    @Before
    public void setUp() {
      initialize();
    }

    @After
    public void tearDown() {
      popOverShell.hide();
      shell.close();
    }

    @Parameterized.Parameters(name="{index}: BottomIsCutOff? {0}, RightIsCutOff? {1}")
    public static List<Object[]> data() {
      return Arrays.asList(new Object[][]{
              {false, false, getShellLocationNotCutOff(), Bubble.DEFAULT_DISPLAY_LOCATION, Bubble.DEFAULT_POINT_CENTERED},
              {false, true, getShellLocationForPopOverCutoff(PopOverShellCutoffPosition.RIGHT), Bubble.DEFAULT_DISPLAY_LOCATION, PopOverShell.PopOverCornerCenteredOnParent.TOP_RIGHT_CORNER},
              {true, false, getShellLocationForPopOverCutoff(PopOverShellCutoffPosition.BOTTOM), PopOverShell.PopOverAboveOrBelowParent.ABOVE_PARENT, Bubble.DEFAULT_POINT_CENTERED},
              {true, true, getShellLocationForPopOverCutoff(PopOverShellCutoffPosition.BOTTOM_RIGHT), PopOverShell.PopOverAboveOrBelowParent.ABOVE_PARENT, PopOverShell.PopOverCornerCenteredOnParent.TOP_RIGHT_CORNER}
      });
    }

    Point shellPoint;
    PopOverShell.PopOverAboveOrBelowParent expectedDisplayLocation;
    PopOverShell.PopOverCornerCenteredOnParent expectedCenteredOnParent;

    public PopOverShellPlacementTests(boolean bottomIsCutOff, boolean rightIsCutOff, Point shellPoint,
                                      PopOverShell.PopOverAboveOrBelowParent expectedDisplayLocation, PopOverShell.PopOverCornerCenteredOnParent expectedCenteredOnParent) {
      this.shellPoint = shellPoint;
      this.expectedDisplayLocation = expectedDisplayLocation;
      this.expectedCenteredOnParent = expectedCenteredOnParent;
    }

    @Test
    public void configurePopOverShellIfWouldBeCutOff_differingParameters_shellHasCorrectParams() {
      shell.open();
      shell.setLocation(shellPoint);
      popOverShell.show();

      assertEquals(popOverShell.popOverAboveOrBelowParent, expectedDisplayLocation);
      assertEquals(popOverShell.popOverCornerCenteredOnParent, expectedCenteredOnParent);
    }


    private enum PopOverShellCutoffPosition { BOTTOM, RIGHT, BOTTOM_RIGHT }
    private static Point getShellLocationForPopOverCutoff(PopOverShellCutoffPosition cutoffPosition) {
      Point appropriateShellLocation = null;

      if (needsInitialization()) {
        initialize();
      }

      Rectangle displayBounds = display.getClientArea();
      Rectangle buttonSize = button.getBounds();
      switch (cutoffPosition) {
        case BOTTOM:
          appropriateShellLocation = new Point(displayBounds.width / 2,
                  displayBounds.height - SHELL_OFFSCREEN_PADDING);
          break;
        case RIGHT:
          appropriateShellLocation = new Point(displayBounds.width - SHELL_OFFSCREEN_PADDING,
                  displayBounds.height / 2);
          break;
        case BOTTOM_RIGHT:
          appropriateShellLocation = new Point(displayBounds.width - SHELL_OFFSCREEN_PADDING,
                  displayBounds.height - SHELL_OFFSCREEN_PADDING);
          break;
      }

      return appropriateShellLocation;
    }

    private static Point getShellLocationNotCutOff() {
      return new Point(0, 0);
    }
  }

  public static class PopOverShellVisibilityTest {
    @Before
    public void setUp() {
      initialize();
    }

    @Test
    public void show_noParameters_isVisible() {
      popOverShell.show();
      assertTrue(popOverShell.isVisible());
    }

    @Test
    public void hide_popOverShellIsNotShown_isNotVisible() {
      popOverShell.hide();
      assertFalse(popOverShell.isVisible());
    }

    @Test
    public void fadeOut_noParameters_fadeEffectInProgress() {
      popOverShell.fadeOut();
      assertTrue(popOverShell.getIsFadeEffectInProgress());
    }

    @Test
    public void toggle_popOverShellIsNotShown_isVisible() {
      popOverShell.toggle();
      assertTrue(popOverShell.isVisible());
    }

    @Test
    public void toggle_popOverShellIsShown_fadeEffectInProgress() {
      popOverShell.show();
      popOverShell.toggle();
      assertTrue(popOverShell.getIsFadeEffectInProgress());
    }

    @Test
    public void toggle_popOverShellToggledTwice_fadeEffectInProgress() {
      popOverShell.toggle();
      popOverShell.toggle();
      assertTrue(popOverShell.getIsFadeEffectInProgress());
    }
  }
}
