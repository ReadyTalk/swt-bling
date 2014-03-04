package com.readytalk.swt.util;

import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoAnnotations.Mock;

public class DisplaySafeTest {
  @Mock
  Display display;

  @Mock
  Display otherDisplay;

  @Before
  public void initMocks() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void getLatestDisplay_CurrentIsNull_DisplayUnchanged() {
    DisplaySafe safe = new DisplaySafe.Builder().setDisplay(display).build();
    
  }
}
