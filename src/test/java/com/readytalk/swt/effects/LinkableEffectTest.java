package com.readytalk.swt.effects;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 */
public class LinkableEffectTest {

  @MockitoAnnotations.Mock
  private LinkableEffect parent;

  @MockitoAnnotations.Mock
  private LinkableEffect child;

  private int TIME_INTERVAL = 200;

  @Before
  public void setUp() throws IllegalArgumentException{
    MockitoAnnotations.initMocks(this);
    Mockito.when(parent.linkEffect(Mockito.any(LinkableEffect.class))).thenCallRealMethod();
    Mockito.when(parent.getEffectList()).thenCallRealMethod();
  }

  @Test
  public void testConstructorWithParent() {
    try {
      // mocking an abstract classes constructor is not something do-able as far as i found, so we just make a dummy one
      LinkableEffect effect = new DummyLinkableEffect(parent, TIME_INTERVAL);

      Assert.assertTrue(parent.getEffectList().contains(effect));
      Assert.assertNotSame(TIME_INTERVAL, effect.getTimeInterval());
      Assert.assertNull(effect.getExecutor());
    } catch (InvalidEffectArgumentException iea) {
      Assert.fail(iea.getMessage());
    }
  }


  /**
   * Can't mock abstract classes, but for this problem this really is the best solution (making LinkableEffect an abstract class)... or the best I found.  :-(
   */
  private class DummyLinkableEffect extends LinkableEffect {

    public DummyLinkableEffect(LinkableEffect parent,
                               int timeInterval, LinkableEffect ... linkableEffects) throws InvalidEffectArgumentException {
      super(parent, null, timeInterval, linkableEffects);
    }

    @Override
    public boolean isEffectComplete() {
      return false;
    }

    @Override
    public void effectStep() {
    }

    @Override
    public void effectComplete() {
    }
  }

}
