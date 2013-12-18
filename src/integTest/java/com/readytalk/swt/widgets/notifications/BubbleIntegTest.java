package com.readytalk.swt.widgets.notifications;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(Enclosed.class)
public class BubbleIntegTest {
  public static class TextExtentTest {
    @Mock
    Bubble bubble;

    GC gc;

    @Before
    public void setUp() {
      MockitoAnnotations.initMocks(this);

      gc = new GC(Display.getDefault());
      when(bubble.maybeBreakLines(any(String.class))).thenCallRealMethod();
      when(bubble.getDisplay()).thenReturn(Display.getDefault());
    }

    @After
    public void tearDown() {
      gc.dispose();
    }

    @Test
    public void maybeBreakLines_stringIsLessThanMaxLength_returnsRawString() {
      String shorterThanMax = "Short";

      assertTrue(gc.textExtent(shorterThanMax).x < Bubble.MAX_STRING_LENGTH);
      assertTrue(bubble.maybeBreakLines(shorterThanMax).equals(shorterThanMax));
    }

    @Test
    public void maybeBreakLines_stringHasLineBreaksAndLessThanMax_returnsRawString() {
      String shortWithLineBreaks = "Short\nWith\nLines";

      assertTrue(bubble.maybeBreakLines(shortWithLineBreaks).equals(shortWithLineBreaks));
    }

    @Test
    public void maybeBreakLines_stringHasLineBreaksAndIsLongerThanMax_returnsRawString() {
      String longWithLineBreaks = "This is a very long string, but I've defined my own line\nbreaks, which you " +
              "should probably pay attention to.\nBecause I know better than you.\nThat is science. And I don\'t see " +
              "you trying to argue with science, right?";

      assertTrue(bubble.maybeBreakLines(longWithLineBreaks).equals(longWithLineBreaks));
    }

    @Test
    public void maybeBreakLines_stringIsLongerThanMax_returnsStringWithLineBreaks() {
      String longWithoutLineBreaks = "This is a very long string, but I have not defined lined breaks. My clients " +
              "would appreciate if this Bubble message wasn\'t extraoridinarily long, so please break this up for me.";

      assertTrue(bubble.maybeBreakLines(longWithoutLineBreaks).contains("\n"));
    }
  }

  public static class BubbleConstructorTests {
    @Test(expected=IllegalArgumentException.class)
    public void createBubble_textIsNull_throwsIllegalArgumentException() {
      Shell shell = new Shell();
      Bubble.createBubble(shell, null);
    }

    @Test
    public void createBubble_validArguments_returnsBubbleObject() {
      Shell shell = new Shell();
      Bubble bubble = Bubble.createBubble(shell, "Some test text");
      assertNotNull(bubble);
    }
  }

  public static class BubbleTagTests {
    Composite composite;
    Bubble bubble;
    BubbleRegistry bubbleRegistry;
    BubbleRegistry.BubbleRegistrant registrant;

    @Before
    public void setUp() {
      MockitoAnnotations.initMocks(this);

      Display display = Display.getDefault();
      Shell shell = new Shell(display);

      composite = new Composite(shell, SWT.NONE);
      bubbleRegistry = BubbleRegistry.getInstance();
      bubble = Bubble.createBubble(composite, "Some test text");
      registrant = bubbleRegistry.findRegistrant(composite);
    }

    @Test
    public void addTags_addANewTag_tagIsAdded() {
      assertEquals(registrant.getTags().size(), 0);

      bubble.addTags(BubbleTag.NEW);

      assertNotNull(bubbleRegistry.tagMap.get(BubbleTag.NEW.getText()));
      assertTrue(registrant.getTags().contains(BubbleTag.NEW));
    }

    @Test
    public void removeTags_removeTag_tagIsRemoved() {
      assertEquals(registrant.getTags().size(), 0);

      bubble.addTags(BubbleTag.NEW);
      bubble.removeTags(BubbleTag.NEW);

      assertFalse(registrant.getTags().contains(BubbleTag.NEW));
    }
  }
}
