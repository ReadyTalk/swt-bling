package com.readytalk.swt.widgets.notifications;

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
import org.eclipse.swt.widgets.Shell;

import com.readytalk.swt.widgets.text.tokenizer.WikiText;

public class BubbleExample {
  public static void main(String[] args) {
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

    Button button = new Button(composite, SWT.PUSH);
    button.setText("I Have a Bubble Tooltip");
    button.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

    Bubble bubble = new Bubble(button, "This text explains a new, exciting feature!");
    BubbleRegistry.getInstance().register(button, bubble, BubbleTag.NEW);

    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
  }
}