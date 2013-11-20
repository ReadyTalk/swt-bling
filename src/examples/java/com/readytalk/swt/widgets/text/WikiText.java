package com.readytalk.swt.widgets.text;

import java.util.Timer;
import java.util.TimerTask;

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

public class WikiText {

	static class TextCanvas extends Canvas {
		
		Timer timer;
		
		public TextCanvas(Composite parent, int style) {
			super(parent, style);
			timer = new Timer();
			
			final TextPaintEventHandler eventHandler1 = new TextPaintEventHandler(
					this).setText(
					"This is clipped normal text \nABCDEFGHIJKLMNOPQRSTUVWXYZ")
					.setBounds(new Rectangle(0, 0, 300, 25));

			final int width = 250;
			Rectangle wikiTextBounds = new Rectangle(0, 50, width, 100);
			final TextPaintEventHandler eventHandler2 = new TextPaintEventHandler(
					this)
					.setTokenizer(new WikiTextTokenizer())
					.setText("This is '''wiki text''' is auto-wrapped and can display "
									+ "''Italic Text,'' '''Bold Text,''' and "
									+ "'''''Bold and Italic Text'''''"
									+ " naked url: http://www.google.com"
									+ " [http://www.stackoverflow.com StackOverflow]")
					.setClipping(false)
					.setBounds(wikiTextBounds)
					.setDrawBounds(true).setWrapping(true)
					.addNavigationListener(new NavigationListener() {
						@Override
						public void navigate(NavigationEvent event) {
							System.out.println("Navigate to: " + event.getUrl());
						}
					});
			
	        timer.scheduleAtFixedRate(new TimerTask() {
	        	double counter = 0.0;
	        	
				@Override
				public void run() {
					counter += 0.01;
					int w = (int)(width * Math.sin(counter) / 3) + width;
					eventHandler2.setBounds(new Rectangle(0, 50, w, 100));
					
					Display.getDefault().syncExec(new Runnable() {
					    public void run() {
					    	if (!isDisposed()) {
					    		redraw();
					    	}
					    }
					});
				}
			}, 0, 50);

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
		TextCanvas canvas = new TextCanvas(shell, SWT.NONE);
		FillLayout fillLayout = new FillLayout();
		shell.setLayout(fillLayout);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()){
				display.sleep();
			}
		}
		display.dispose();
		canvas.timer.cancel();
	}
}
