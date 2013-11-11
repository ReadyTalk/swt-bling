package com.readytalk.examples.swt.widgets.notifications;

import com.readytalk.examples.swt.RunnableExample;
import com.readytalk.swt.widgets.notifications.Bubble;
import com.readytalk.swt.widgets.notifications.BubbleRegistry;
import com.readytalk.swt.widgets.notifications.BubbleTag;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class BubbleExample {
  @RunnableExample(name="Bubble")
  public static void run() {
    final Display display = new Display();
    final Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());
    Composite composite = new Composite(shell, SWT.NONE);
    composite.setLayout(new GridLayout(3, false));

    final BubbleRegistry bubbleRegistry = BubbleRegistry.getInstance();

    Button displayNewBubbles = new Button(composite, SWT.PUSH);
    displayNewBubbles.setText("Display Bubbles Tag NEW");
    Bubble displayNewBubblesBubble = new Bubble(displayNewBubbles, "Click here to display all Bubbles of type \"NEW\"");
    bubbleRegistry.register(displayNewBubbles, displayNewBubblesBubble);
    displayNewBubbles.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        bubbleRegistry.showBubblesByTags(BubbleTag.NEW);
      }
    });

    Button displayAllBubbles = new Button(composite, SWT.PUSH);
    displayAllBubbles.setText("Display All Bubbles");
    Bubble displayAllBubblesBubble = new Bubble(displayAllBubbles, "Click here to display all Bubbles");
    bubbleRegistry.register(displayAllBubbles, displayAllBubblesBubble);
    displayAllBubbles.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        bubbleRegistry.showAllBubbles();
      }
    });

    Button dismissAllBubbles = new Button(composite, SWT.PUSH);
    dismissAllBubbles.setText("Dismiss All Bubbles");
    Bubble hideAllBubblesBubble = new Bubble(dismissAllBubbles, "Click here to dismiss all Bubbles");
    bubbleRegistry.register(dismissAllBubbles, hideAllBubblesBubble);
    dismissAllBubbles.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        bubbleRegistry.dismissAllBubbles();
      }
    });

    Button newFeatureButton = new Button(composite, SWT.PUSH);
    newFeatureButton.setText("A New Feature");
    Bubble newFeatureButtonBubble = new Bubble(newFeatureButton, "This text explains a new, exciting feature!");
    bubbleRegistry.register(newFeatureButton, newFeatureButtonBubble, BubbleTag.NEW);

    Label labelExample = new Label(composite, SWT.NONE);
    labelExample.setText("A Short Label");
    Bubble labelBubble = new Bubble(labelExample, "This is a longer description of what the short label explains");
    bubbleRegistry.register(labelExample, labelBubble);

    ProgressBar progress = new ProgressBar(composite, SWT.INDETERMINATE);
    Bubble progressBubble = new Bubble(progress, "This is a really long description where no line breaks are provided. " +
            "We will automatically break these lines for you, so that users aren't overwhelmed by a long single line.");
    bubbleRegistry.register(progress, progressBubble);

    Label boldLabel = new Label(composite, SWT.NONE);
    boldLabel.setText("This is a label with a bold Bubble");
    Bubble boldBubble = new Bubble(boldLabel, "This is a bold Bubble tooltip text", true);
    bubbleRegistry.register(boldLabel, boldBubble);

    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
  }
}