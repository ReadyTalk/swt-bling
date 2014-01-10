package com.readytalk.swt.widgets.buttons;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

/**
 */
public class SquareButtonGroupTest {

  @Mock
  private SquareButton topButton;

  @Mock
  private SquareButton middleButton;

  @Mock
  private SquareButton bottomButton;

  private FauxToggleable topToggleable, middleToggleable, bottomToggleable;

  @Before
  public void setup() {

    topToggleable = new FauxToggleable();
    middleToggleable = new FauxToggleable();
    bottomToggleable = new FauxToggleable();

//    doNothing().when(topButton).setToggleable(anyBoolean()).thenCallRealMethod();
    when(topButton.isToggleable()).thenCallRealMethod();

  }

  @Test
  public void testMembership() {

  }



  private class FauxToggleable {
    boolean toggleable, toggled;

    private boolean isToggleable() {
      return toggleable;
    }

    private void setToggleable(boolean toggleable) {
      this.toggleable = toggleable;
    }

    private boolean isToggled() {
      return toggled;
    }

    private void setToggled(boolean toggled) {
      this.toggled = toggled;
    }
  }
}
