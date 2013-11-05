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

import org.junit.Assert;
import org.junit.Test;

/**
 */
public class BubbleRegistryIntegTest {

  Bubble topBubble, middleBubble, bottomBubble;
  Button topButton, middleButton, bottomButton;
  Shell shell;

  Display display;

  BubbleRegistry bubbleRegistry;

  private void setupShell() {
    display = Display.getDefault();
    shell = new Shell(display);
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

    topBubble = new Bubble(topButton,
      "Some text.");

    middleBubble = new Bubble(middleButton,
      "Some middlin' text");

    bottomBubble = new Bubble(bottomButton,
      "Some other text");

    bubbleRegistry = new BubbleRegistry();

    shell.setSize(500, 250);
    shell.open();
  }

  private void closeShell() {
    shell.close();
    display.close();
  }


  @Test
  public void test_show_by_tag() {
    setupShell();
    bubbleRegistry.register(topButton, topBubble, BubbleTag.NEW);
    bubbleRegistry.register(middleButton, middleBubble, BubbleTag.FREE_FORM);
    bubbleRegistry.register(bottomButton, bottomBubble, BubbleTag.NEW);

    bubbleRegistry.showBubblesByTags(BubbleTag.NEW);

    Assert.assertTrue(topBubble.isVisible());
    Assert.assertFalse(middleBubble.isVisible());
    Assert.assertTrue(bottomBubble.isVisible());
    closeShell();
  }

  @Test
  public void test_hide_by_tag() {
    setupShell();
    bubbleRegistry.register(topButton, topBubble, BubbleTag.NEW);
    bubbleRegistry.register(middleButton, middleBubble, BubbleTag.FREE_FORM);
    bubbleRegistry.register(bottomButton, bottomBubble, BubbleTag.NEW);

    bubbleRegistry.showBubblesByTags(BubbleTag.NEW);

    Assert.assertTrue(topBubble.isVisible());
    Assert.assertFalse(middleBubble.isVisible());
    Assert.assertTrue(bottomBubble.isVisible());

    bubbleRegistry.dismissBubblesByTag(BubbleTag.NEW);
    bubbleRegistry.showBubblesByTags(BubbleTag.FREE_FORM);

    Assert.assertFalse(topBubble.isVisible());
    Assert.assertTrue(middleBubble.isVisible());
    Assert.assertFalse(bottomBubble.isVisible());

    closeShell();
  }

  @Test
  public void test_show_all() {
    setupShell();
    bubbleRegistry.register(topButton, topBubble, BubbleTag.NEW);
    bubbleRegistry.register(middleButton, middleBubble, BubbleTag.FREE_FORM);
    bubbleRegistry.register(bottomButton, bottomBubble, BubbleTag.NEW);

    bubbleRegistry.showAllBubbles();

    Assert.assertTrue(topBubble.isVisible());
    Assert.assertTrue(middleBubble.isVisible());
    Assert.assertTrue(bottomBubble.isVisible());
    closeShell();
  }

  @Test
  public void test_hide_all() {
    setupShell();
    bubbleRegistry.register(topButton, topBubble, BubbleTag.NEW);
    bubbleRegistry.register(middleButton, middleBubble, BubbleTag.FREE_FORM);
    bubbleRegistry.register(bottomButton, bottomBubble, BubbleTag.NEW);

    bubbleRegistry.showAllBubbles();

    Assert.assertTrue(topBubble.isVisible());
    Assert.assertTrue(middleBubble.isVisible());
    Assert.assertTrue(bottomBubble.isVisible());

    bubbleRegistry.dismissAllBubbles();

    Assert.assertFalse(topBubble.isVisible());
    Assert.assertFalse(middleBubble.isVisible());
    Assert.assertFalse(bottomBubble.isVisible());
    closeShell();
  }
}
