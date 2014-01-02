package com.readytalk.swt.widgets.notifications;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(Enclosed.class)
public class PopOverShellTest {
  @RunWith(Parameterized.class)
  public static class PopOverShellPlacementTests {
    private static final int DISPLAY_WIDTH = 400;
    private static final int DISPLAY_HEIGHT = 400;
    private static final Point LOCATION_RELATIVE_TO_DISPLAY = new Point(0, 0);
    private static final Rectangle DISPLAY_BOUNDS = new Rectangle(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
    private static final int OUTSIDE_BOUNDS_VALUE = 100;
    private static final int INSIDE_BOUNDS_VALUE = 100;

    @Mock
    PopOverShell popOverShell;

    @Before
    public void setUp() {
      MockitoAnnotations.initMocks(this);

      when(popOverShell.isBottomCutOff(any(Rectangle.class), any(Point.class), any(Rectangle.class))).thenCallRealMethod();
      when(popOverShell.isRightCutOff(any(Rectangle.class), any(Point.class), any(Rectangle.class))).thenCallRealMethod();
    }

    @Parameterized.Parameters(name="{index}: BottomIsCutOff? {0}, RightIsCutOff? {1}")
    public static List<Object[]> data() {
      return Arrays.asList(new Object[][]{
              { false, false,  getRectangleNotCutOff() },
              { false, true, getRectangleForCutOff(PopOverShellCutOffPosition.RIGHT) },
              { true, false, getRectangleForCutOff(PopOverShellCutOffPosition.BOTTOM) },
              { true, true, getRectangleForCutOff(PopOverShellCutOffPosition.BOTTOM_AND_RIGHT) }
      });
    }

    boolean bottomCutOff;
    boolean rightCutOff;
    Rectangle popOverBounds;

    public PopOverShellPlacementTests(boolean bottomCutOff, boolean rightCutOff, Rectangle popOverBounds) {
      this.bottomCutOff = bottomCutOff;
      this.rightCutOff = rightCutOff;
      this.popOverBounds = popOverBounds;
    }

    @Test
    public void isBottomCutOff_differingParameters_returnsCorrectBoolean() {
      assertEquals(popOverShell.isBottomCutOff(DISPLAY_BOUNDS, LOCATION_RELATIVE_TO_DISPLAY, popOverBounds),
              bottomCutOff);
    }

    @Test
    public void isRightCutOff_differingParameters_returnsCorrectBoolean() {
      assertEquals(popOverShell.isRightCutOff(DISPLAY_BOUNDS, LOCATION_RELATIVE_TO_DISPLAY, popOverBounds),
              rightCutOff);
    }

    private enum PopOverShellCutOffPosition { BOTTOM, RIGHT, BOTTOM_AND_RIGHT }
    private static Rectangle getRectangleForCutOff(PopOverShellCutOffPosition cutOffPosition) {
      switch (cutOffPosition) {
        case BOTTOM:
          return new Rectangle(LOCATION_RELATIVE_TO_DISPLAY.x, LOCATION_RELATIVE_TO_DISPLAY.y,
                  DISPLAY_WIDTH - INSIDE_BOUNDS_VALUE, DISPLAY_HEIGHT + OUTSIDE_BOUNDS_VALUE);
        case RIGHT:
          return new Rectangle(LOCATION_RELATIVE_TO_DISPLAY.x, LOCATION_RELATIVE_TO_DISPLAY.y,
                  DISPLAY_WIDTH + OUTSIDE_BOUNDS_VALUE, DISPLAY_HEIGHT - INSIDE_BOUNDS_VALUE);
        case BOTTOM_AND_RIGHT:
          return new Rectangle(LOCATION_RELATIVE_TO_DISPLAY.x, LOCATION_RELATIVE_TO_DISPLAY.y,
                  DISPLAY_WIDTH + OUTSIDE_BOUNDS_VALUE, DISPLAY_HEIGHT + OUTSIDE_BOUNDS_VALUE);
      }

      return null;
    }

    private static Rectangle getRectangleNotCutOff() {
      return new Rectangle(LOCATION_RELATIVE_TO_DISPLAY.x, LOCATION_RELATIVE_TO_DISPLAY.y,
              DISPLAY_WIDTH - INSIDE_BOUNDS_VALUE, DISPLAY_HEIGHT - INSIDE_BOUNDS_VALUE);
    }
  }

  public static class PopOverShellOffscreenTests {
    @Mock
    PopOverShell popOverShell;

    private static final int OFFSCREEN_PADDING = 10;
    private static final Rectangle DISPLAY_BOUNDS = new Rectangle(0, 0, 400, 400);
    private static final Rectangle POPOVER_BOUNDS = new Rectangle(0, 0, 100, 100);

    @Before
    public void setUp() {
      MockitoAnnotations.initMocks(this);

      when(popOverShell.isStillOffScreen(any(Rectangle.class), any(Point.class), any(Rectangle.class))).thenCallRealMethod();
    }

    @Test
    public void isStillOffScreen_popOverShellOffScreenRight_returnsTrue() {
      assertTrue(popOverShell.isStillOffScreen(DISPLAY_BOUNDS, new Point(DISPLAY_BOUNDS.width + OFFSCREEN_PADDING, 0),
              POPOVER_BOUNDS));
    }

    @Test
    public void isStillOffScreen_popOverShellOffScreenBottom_returnsTrue() {
      assertTrue(popOverShell.isStillOffScreen(DISPLAY_BOUNDS, new Point(0, DISPLAY_BOUNDS.height + OFFSCREEN_PADDING),
              POPOVER_BOUNDS));
    }

    @Test
    public void isStillOffScreen_popOverShellIsOnScreen_returnsFalse() {
      assertFalse(popOverShell.isStillOffScreen(DISPLAY_BOUNDS, new Point(0, 0), POPOVER_BOUNDS));
    }
  }
}
