package com.readytalk.swt.text.painter;

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

import com.readytalk.swt.text.navigation.NavigationEvent;
import com.readytalk.swt.text.navigation.NavigationListener;
import com.readytalk.swt.text.painter.TextPainter;
import com.readytalk.swt.text.tokenizer.TextTokenizer;
import com.readytalk.swt.text.tokenizer.TextTokenizerFactory;
import com.readytalk.swt.text.tokenizer.TextTokenizerType;

public class TextPainterExample {

  static class TextCanvas extends Canvas {

    Timer timer;

    public TextCanvas(Composite parent, int style)
        throws InstantiationException, IllegalAccessException,
        ClassNotFoundException {

      super(parent, style);
      timer = new Timer();

      final TextPainter eventHandler1 = new TextPainter(this).setText(
          "This is clipped normal text \nABCDEFGHIJKLMNOPQRSTUVWXYZ")
          .setBounds(new Rectangle(0, 0, 300, 25));

      final int width = 250;
      Rectangle wikiTextBounds = new Rectangle(0, 50, width, 100);
      TextTokenizer tokenizer = TextTokenizerFactory
          .createTextTokenizer(TextTokenizerType.WIKI);
      final TextPainter eventHandler2 = new TextPainter(this)
          .setTokenizer(tokenizer)
          .setText(
              "This is '''wiki text''' is auto-wrapped and can display "
                  + "''Italic Text,'' '''Bold Text,''' and "
                  + "'''''Bold and Italic Text'''''"
                  + " naked url: http://www.google.com"
                  + " wiki url: [http://www.readytalk.com ReadyTalk]"  
                  + " url: [http://www.readytalk.com]")
          .setClipping(false).setBounds(wikiTextBounds).setDrawBounds(true)
          .setWrapping(true).addNavigationListener(new NavigationListener() {
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
          int w = (int) (width * Math.sin(counter) / 3) + width;
          eventHandler2.setBounds(new Rectangle(0, 50, w, 100));
          Display.getDefault().syncExec(new Runnable() {
            public void run() {
              if (!isDisposed()) {
                redraw();
              }
            }
          });
        }
      }, 0, 40);

      addPaintListener(new PaintListener() {
        @Override
        public void paintControl(PaintEvent e) {
          eventHandler1.handlePaint(e);
          eventHandler2.handlePaint(e);
        }
      });
    }
  };

  public static void main(String[] args) throws InstantiationException,
      IllegalAccessException, ClassNotFoundException {

    final Display display = new Display();
    final Shell shell = new Shell(display);
    shell.setSize(400, 250);
    TextCanvas canvas = new TextCanvas(shell, SWT.NONE);
    FillLayout fillLayout = new FillLayout();
    shell.setLayout(fillLayout);
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();
    canvas.timer.cancel();
  }
}
