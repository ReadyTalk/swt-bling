package com.readytalk.swt.widgets.notifications;

import org.junit.Assert;
import org.junit.Before;
import org.eclipse.swt.widgets.Composite;
import org.junit.Test;
import org.mockito.MockitoAnnotations.Mock;
import org.mockito.MockitoAnnotations;

/**
 */
public class BubbleRegistryTest {

  @Mock
  private Composite composite;

  @Mock
  private Bubble bubble;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void test_Register_Composite() {
    BubbleRegistry bubbleRegistry = new BubbleRegistry();
    bubbleRegistry.register(composite, bubble);
    Assert.assertNotNull(bubbleRegistry.findRegistrant(composite));
  }

  @Test
  public void test_Register_Composite_With_Tags() {
    BubbleRegistry bubbleRegistry = new BubbleRegistry();
    bubbleRegistry.register(composite, bubble, BubbleTag.NEW);
    BubbleRegistry.BubbleRegistrant registrant = bubbleRegistry.findRegistrant(composite);
    Assert.assertNotNull(registrant);
    if(registrant != null) {
      Assert.assertTrue(registrant.getTags().contains(BubbleTag.NEW));
    }
    Assert.assertNotNull(bubbleRegistry.tagMap.get(BubbleTag.NEW.getText()));
  }

  @Test
  public void test_Add_Tag() {
    BubbleRegistry bubbleRegistry = new BubbleRegistry();
    bubbleRegistry.register(composite, bubble);
    BubbleRegistry.BubbleRegistrant registrant = bubbleRegistry.findRegistrant(composite);
    Assert.assertEquals(registrant.getTags().size(), 0);
    bubbleRegistry.addTags(composite, BubbleTag.NEW);
    Assert.assertNotNull(bubbleRegistry.tagMap.get(BubbleTag.NEW.getText()));
    Assert.assertTrue(registrant.getTags().contains(BubbleTag.NEW));
  }

  @Test
  public void test_Add_Tag_Without_Registering() {
    BubbleRegistry bubbleRegistry = new BubbleRegistry();
    bubbleRegistry.addTags(composite, BubbleTag.NEW);
    BubbleRegistry.BubbleRegistrant registrant = bubbleRegistry.findRegistrant(composite);
    Assert.assertNull(registrant);
    Assert.assertFalse(bubbleRegistry.tagMap.containsKey(BubbleTag.NEW.getText()));
  }

  @Test
  public void test_Unregister_Composite() {
    BubbleRegistry bubbleRegistry = new BubbleRegistry();
    bubbleRegistry.register(composite, bubble);
    Assert.assertNotNull(bubbleRegistry.findRegistrant(composite));
    bubbleRegistry.unregister(composite);
    Assert.assertNull(bubbleRegistry.findRegistrant(composite));
  }

  @Test
  public void test_Remove_Tag() {
    BubbleRegistry bubbleRegistry = new BubbleRegistry();
    bubbleRegistry.register(composite, bubble);
    BubbleRegistry.BubbleRegistrant registrant = bubbleRegistry.findRegistrant(composite);
    Assert.assertEquals(registrant.getTags().size(), 0);
    bubbleRegistry.addTags(composite, BubbleTag.NEW);
    Assert.assertNotNull(bubbleRegistry.tagMap.get(BubbleTag.NEW.getText()));
    Assert.assertTrue(registrant.getTags().contains(BubbleTag.NEW));
    bubbleRegistry.removeTags(registrant, BubbleTag.NEW);
    Assert.assertFalse(registrant.getTags().contains(BubbleTag.NEW));
  }

}
