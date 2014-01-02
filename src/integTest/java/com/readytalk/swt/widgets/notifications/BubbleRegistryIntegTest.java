package com.readytalk.swt.widgets.notifications;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class BubbleRegistryIntegTest {

  Bubble topBubble, middleBubble, bottomBubble;
  Button topButton, middleButton, bottomButton;

  BubbleRegistry bubbleRegistry;

  @Before
  public void setUp() {
    Display display = Display.getDefault();
    Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());
    Composite composite = new Composite(shell, SWT.NONE);
    composite.setLayout(new FormLayout());

    topButton = new Button(composite, SWT.PUSH);
    topButton.setText("Push");
    FormData formData = new FormData();
    formData.top = new FormAttachment(0, 50);
    formData.height = 25;
    formData.left = new FormAttachment(0);
    formData.right = new FormAttachment(100);
    topButton.setLayoutData(formData);

    middleButton = new Button(composite, SWT.PUSH);
    middleButton.setText("Push");
    formData = new FormData();
    formData.top = new FormAttachment(topButton, 50);
    formData.height = 25;
    formData.left = new FormAttachment(0);
    formData.right = new FormAttachment(100);
    middleButton.setLayoutData(formData);

    bottomButton = new Button(composite, SWT.PUSH);
    bottomButton.setText("Push");
    formData = new FormData();
    formData.top = new FormAttachment(middleButton, 50);
    formData.height = 25;
    formData.left = new FormAttachment(0);
    formData.right = new FormAttachment(100);
    bottomButton.setLayoutData(formData);

    topBubble = Bubble.createBubble(topButton,
      "Some text.", BubbleTag.NEW);

    middleBubble = Bubble.createBubble(middleButton,
      "Some middlin' text", BubbleTag.FREE_FORM);

    bottomBubble = Bubble.createBubble(bottomButton,
      "Some other text", BubbleTag.NEW);

    bubbleRegistry = BubbleRegistry.getInstance();

    shell.setSize(500, 250);
  }

  @Test
  public void test_show_by_tag() {
    bubbleRegistry.showBubblesByTags(BubbleTag.NEW);

    Assert.assertTrue(topBubble.isVisible());
    Assert.assertFalse(middleBubble.isVisible());
    Assert.assertTrue(bottomBubble.isVisible());
  }

  @Test
  public void test_hide_by_tag() {
    bubbleRegistry.showBubblesByTags(BubbleTag.NEW);

    Assert.assertTrue(topBubble.isVisible());
    Assert.assertFalse(middleBubble.isVisible());
    Assert.assertTrue(bottomBubble.isVisible());

    bubbleRegistry.dismissBubblesByTag(BubbleTag.NEW);
    bubbleRegistry.showBubblesByTags(BubbleTag.FREE_FORM);

    Assert.assertTrue(topBubble.getIsFadeEffectInProgress());
    Assert.assertTrue(middleBubble.isVisible());
    Assert.assertTrue(bottomBubble.getIsFadeEffectInProgress());
  }

  @Test
  public void test_show_all() {
    bubbleRegistry.showAllBubbles();

    Assert.assertTrue(topBubble.isVisible());
    Assert.assertTrue(middleBubble.isVisible());
    Assert.assertTrue(bottomBubble.isVisible());
  }

  @Test
  public void test_hide_all() {
    bubbleRegistry.showAllBubbles();

    Assert.assertTrue(topBubble.isVisible());
    Assert.assertTrue(middleBubble.isVisible());
    Assert.assertTrue(bottomBubble.isVisible());

    bubbleRegistry.dismissAllBubbles();

    Assert.assertTrue(topBubble.getIsFadeEffectInProgress());
    Assert.assertTrue(middleBubble.getIsFadeEffectInProgress());
    Assert.assertTrue(bottomBubble.getIsFadeEffectInProgress());
  }
}
