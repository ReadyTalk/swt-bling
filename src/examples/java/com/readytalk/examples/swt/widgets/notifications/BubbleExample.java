package com.readytalk.examples.swt.widgets.notifications;

import com.readytalk.examples.swt.RunnableExample;
import com.readytalk.examples.swt.SwtBlingExample;
import com.readytalk.swt.widgets.notifications.Bubble;
import com.readytalk.swt.widgets.notifications.BubbleRegistry;
import com.readytalk.swt.widgets.notifications.BubbleTag;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class BubbleExample implements SwtBlingExample {
  @RunnableExample(name="Bubble")
  public BubbleExample() { }

  public void run() {
    final Display display = new Display();
    final Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());
    Composite composite = new Composite(shell, SWT.NONE);
    composite.setLayout(new GridLayout(3, false));

    final BubbleRegistry bubbleRegistry = BubbleRegistry.getInstance();

    Button displayNewBubbles = new Button(composite, SWT.PUSH);
    displayNewBubbles.setText("Display Bubbles Tag NEW");
    Bubble.createBubble(displayNewBubbles, "Click here to display all Bubbles of type \"NEW\"");
    displayNewBubbles.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        bubbleRegistry.showBubblesByTags(BubbleTag.NEW);
      }
    });

    Button displayAllBubbles = new Button(composite, SWT.PUSH);
    displayAllBubbles.setText("Display All Bubbles");
    Bubble.createBubble(displayAllBubbles, "Click here to display all Bubbles");
    displayAllBubbles.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        bubbleRegistry.showAllBubbles();
      }
    });

    Button dismissAllBubbles = new Button(composite, SWT.PUSH);
    dismissAllBubbles.setText("Dismiss All Bubbles");
    Bubble.createBubble(dismissAllBubbles, "Click here to dismiss all Bubbles");
    dismissAllBubbles.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        bubbleRegistry.dismissAllBubbles();
      }
    });

    Button newFeatureButton = new Button(composite, SWT.PUSH);
    newFeatureButton.setText("A New Feature");
    Bubble.createBubble(newFeatureButton, "This text explains a new, exciting feature!", BubbleTag.NEW);

    Label labelExample = new Label(composite, SWT.NONE);
    labelExample.setText("A Short Label");
    Bubble.createBubble(labelExample, "This is a longer description of what the short label explains");

    ProgressBar progress = new ProgressBar(composite, SWT.INDETERMINATE);
    Bubble.createBubble(progress, "This is a really long description where no line breaks are provided. " +
            "We will automatically break these lines for you, so that users aren't overwhelmed by a long single line.");

    Label boldLabel = new Label(composite, SWT.NONE);
    boldLabel.setText("This is a label with a bold Bubble");
    Bubble.createBubble(boldLabel, "This is a bold Bubble tooltip text", true);

    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
  }
}