package com.readytalk.swt.util;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class DisplaySafeTest {

  @Before
  public void initializeEmptyShell() {
    // If we don't make a shell, Display will not initialize
    new Shell();
  }

  @Test(expected = DisplaySafe.NullDisplayException.class)
  public void getLatestDisplay_CurrentIsNull_DisplayUnchanged() throws DisplaySafe.NullDisplayException {
    DisplaySafe safe = new DisplaySafe(null);
    DisplaySafe safeSpy = spy(safe);
    when(safeSpy.getCurrent()).thenReturn(null);
    safeSpy.getLatestDisplay();
  }

  @Test
  public void getLatestDisplay_CurrentIsNull_DisplayChanged() {
    DisplaySafe safe = new DisplaySafe(null);
    Display display = null;

    try {
      display = safe.getLatestDisplay();
    } catch (DisplaySafe.NullDisplayException nde) {
      fail();
    }

    assertNotNull(display);
  }

  @Test
  public void getLatestDisplay_CurrentNotNull_DisplayUnchanged() {
    Display display = Display.getCurrent();
    DisplaySafe safe = new DisplaySafe(display);
    DisplaySafe safeSpy = spy(safe);
    when(safeSpy.getCurrent()).thenReturn(display);

    Display sameDisplay = null;

    try {
      sameDisplay = safeSpy.getLatestDisplay();
    } catch (DisplaySafe.NullDisplayException nde) {
      fail();
    }

    assertEquals(display, sameDisplay);
  }

  @Test
  public void getLatestDisplay_CurrentNull_DisplayNotNull() {
    DisplaySafe safe = new DisplaySafe(Display.getCurrent());
    DisplaySafe safeSpy = spy(safe);
    when(safeSpy.getCurrent()).thenReturn(null);

    Display display = null;

    try {
      display = safeSpy.getLatestDisplay();
    } catch (DisplaySafe.NullDisplayException nde) {
      fail();
    }

    assertNotNull(display);
  }
}
