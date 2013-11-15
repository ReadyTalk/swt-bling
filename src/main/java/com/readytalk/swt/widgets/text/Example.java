package com.readytalk.swt.widgets.text;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.readytalk.swt.widgets.text.tokenizer.WikiTextTokenizer;

public class Example {

  static class ExperimentalExample extends Canvas {
    
    public ExperimentalExample (Composite parent, int style) {
      super(parent, style);
      
      final TextPaintEventHandler eventHandler1 = 
          new TextPaintEventHandler(this)
          .setText("This is clipped normal text \nABCDEFGHIJKLMNOPQRSTUVWXYZ")
          .setBounds(new Rectangle(0, 0, 300, 25));
      
      final TextPaintEventHandler eventHandler2 = 
          new TextPaintEventHandler(this)
          .setTokenizer(new WikiTextTokenizer())
          .setText("This is wiki text \n '''BOLD TEXT''' http://www.google.com")
          .setClipping(false)
          .setBounds(new Rectangle(0, 50, 100, 100))
          .setDrawBounds(true)
          .setWrapping(true)
          .addNavigationListener(new NavigationListener() {
            @Override
            public void navigate(NavigationEvent event) {}
          });
    
      addPaintListener(new PaintListener() {
        
        @Override
        public void paintControl(PaintEvent e) {
          eventHandler1.handlePaint(e);
          eventHandler2.handlePaint(e);
        }
      });
    }
  };
  
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setSize(400, 500);
		ExperimentalExample example = new ExperimentalExample(shell, SWT.NONE);
		FillLayout fillLayout = new FillLayout();
		shell.setLayout(fillLayout);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()){
				display.sleep();
			}
		}
		display.dispose();
	}
}