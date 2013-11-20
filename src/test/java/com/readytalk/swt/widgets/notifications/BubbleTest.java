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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(Enclosed.class)
public class BubbleTest {
  @RunWith(Parameterized.class)
  public static class BubblePlacementTests {
    private static final int DISPLAY_WIDTH = 400;
    private static final int DISPLAY_HEIGHT = 400;
    private static final Point LOCATION_RELATIVE_TO_DISPLAY = new Point(0, 0);
    private static final Rectangle DISPLAY_BOUNDS = new Rectangle(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
    private static final int OUTSIDE_BOUNDS_VALUE = 100;
    private static final int INSIDE_BOUNDS_VALUE = 100;

    @Mock
    Bubble bubble;

    @Before
    public void setUp() {
      MockitoAnnotations.initMocks(this);

      when(bubble.configureBubbleIfBottomCutOff(any(Rectangle.class), any(Point.class), any(Rectangle.class))).thenCallRealMethod();
      when(bubble.configureBubbleIfRightmostTextCutOff(any(Rectangle.class), any(Point.class), any(Rectangle.class))).thenCallRealMethod();
      when(bubble.configureBubbleIfWouldBeCutOff(any(Rectangle.class), any(Point.class), any(Rectangle.class))).thenCallRealMethod();
    }

    @Parameterized.Parameters(name="{index}: BottomIsCutOff? {0}, RightIsCutOff? {1}")
    public static List<Object[]> data() {
      return Arrays.asList(new Object[][]{
              { false, false,  getRectangleNotCutOff() },
              { false, true, getRectangleForCutOff(BubbleCutOffPosition.RIGHT) },
              { true, false, getRectangleForCutOff(BubbleCutOffPosition.BOTTOM) },
              { true, true, getRectangleForCutOff(BubbleCutOffPosition.BOTTOM_AND_RIGHT) }
      });
    }

    boolean bottomCutOff;
    boolean rightCutOff;
    Rectangle bubbleRectangle;

    public BubblePlacementTests(boolean bottomCutOff, boolean rightCutOff, Rectangle bubbleRectangle) {
      this.bottomCutOff = bottomCutOff;
      this.rightCutOff = rightCutOff;
      this.bubbleRectangle = bubbleRectangle;
    }

    @Test
    public void configureBubbleIfBottomCutOff_differingParameters_returnsCorrectWasConfiguredBoolean() {
      assertEquals(bubble.configureBubbleIfBottomCutOff(DISPLAY_BOUNDS, LOCATION_RELATIVE_TO_DISPLAY,
              bubbleRectangle), bottomCutOff);
    }

    @Test
    public void configureBubbleIfRightmostTextCutOff_differingParameters_returnsCorrectWasConfiguredBoolean() {
      assertEquals(bubble.configureBubbleIfRightmostTextCutOff(DISPLAY_BOUNDS, LOCATION_RELATIVE_TO_DISPLAY,
              bubbleRectangle), rightCutOff);
    }

    private enum BubbleCutOffPosition { BOTTOM, RIGHT, BOTTOM_AND_RIGHT }
    private static Rectangle getRectangleForCutOff(BubbleCutOffPosition cutOffPosition) {
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
}
