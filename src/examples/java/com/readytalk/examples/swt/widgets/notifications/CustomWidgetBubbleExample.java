package com.readytalk.examples.swt.widgets.notifications;

import com.readytalk.examples.swt.RunnableExample;
import com.readytalk.examples.swt.SwtBlingExample;
import com.readytalk.swt.widgets.notifications.Bubblable;
import com.readytalk.swt.widgets.notifications.Bubble;
import com.readytalk.swt.widgets.notifications.BubbleRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class CustomWidgetBubbleExample implements SwtBlingExample {
  private CustomImageWidget widget;
  private Display display;
  private Shell shell;
  private Composite parentComposite;

  @RunnableExample(name="CustomWidgetBubble")
  public CustomWidgetBubbleExample() {
    display = new Display();
    shell = new Shell(display);
    shell.setLayout(new FillLayout());
    parentComposite = new Composite(shell, SWT.NONE);
    parentComposite.setLayout(new FillLayout());

    widget = new CustomImageWidget(parentComposite, display.getSystemImage(SWT.ICON_QUESTION));
  }

  public void run() {
    parentComposite.addPaintListener(new PaintListener() {
      public void paintControl(PaintEvent e) {
        Point shellSize = shell.getSize();
        Point centered = new Point((shellSize.x / 2) - (widget.getSize().x / 2),
                                   (shellSize.y / 2) - (widget.getSize().y / 2));
        widget.setLocation(centered);
        e.gc.drawImage(widget.getImage(), centered.x, centered.y);
      }
    });

    Bubble bubble = new Bubble(widget, "Here's some info about the custom icon. This item is drawn with a GC, and would " +
                                       "traditionally have un-tooltip-able");
    BubbleRegistry.getInstance().register(widget, bubble);

    shell.setSize(200, 200);
    shell.open();

    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
  }

  private class CustomImageWidget extends Bubblable {
    private Control paintedElement;
    private Image image;
    private Point location;
    private Point size;

    public CustomImageWidget(Control paintedElement, Image image) {
      this.paintedElement = paintedElement;
      this.image = image;
    }

    public Image getImage() {
      return image;
    }

    public void setLocation(Point location) {
      this.location = location;
    }

    public Control getPaintedElement() {
      return paintedElement;
    }

    public Point getLocation() {
      return location;
    }

    public Point getSize() {
      if (size == null) {
        Rectangle imageBounds = image.getBounds();
        size = new Point(imageBounds.width, imageBounds.height);
      }

      return size;
    }
  }
}
