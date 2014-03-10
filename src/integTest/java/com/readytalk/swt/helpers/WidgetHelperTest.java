package com.readytalk.swt.helpers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WidgetHelperTest {

  Display display;
  Shell shell = null;
  Composite composite = null;
  Button button;


  @Before
  public void setup() {
    display = Display.getDefault();
  }

  @After
  public void teardown() {
    if (!display.isDisposed())
      display.syncExec(new Runnable() {

        @Override
        public void run() {
          display.dispose();
        }
      });

  }

  @Test
  public void testWidgetSafe() {
    display.syncExec(new Runnable() {
      @Override
      public void run() {
        shell = new Shell(display);
        shell.setLayout(new FillLayout());
        composite = new Composite(shell, SWT.NONE);
        button = new Button(composite, SWT.FLAT);
        shell.open();
        Assert.assertTrue(WidgetHelper.isWidgetSafe(button));

        shell.close();
      }
    });


  }

  @Test
  public void testWidgetSafeDisposed() {
    display.syncExec(new Runnable() {
      @Override
      public void run() {
        shell = new Shell(display);
        shell.setLayout(new FillLayout());
        composite = new Composite(shell, SWT.NONE);

        button = new Button(composite, SWT.FLAT);

        shell.open();
        shell.close();
        // display.dispose();
        Assert.assertFalse(WidgetHelper.isWidgetSafe(button));
      }
    });

  }

  @Test
  public void testWidgetSafeNull() {
    Button button = null;
    Assert.assertFalse(WidgetHelper.isWidgetSafe(button));
  }

  @Test
  public void testControlSafeAndVisible() {
    display.syncExec(new Runnable() {
      @Override
      public void run() {
        shell = new Shell(display);
        shell.setLayout(new FillLayout());
        composite = new Composite(shell, SWT.NONE);

        button = new Button(composite, SWT.FLAT);
        shell.open();

        Assert.assertTrue(WidgetHelper.isControlSafeAndVisible(button));

        shell.close();
      }
    });

  }

  @Test
  public void testControlSafeAndNotVisible() {

    display.syncExec(new Runnable() {
      @Override
      public void run() {
        shell = new Shell(display);
        shell.setLayout(new FillLayout());
        composite = new Composite(shell, SWT.NONE);

        button = new Button(composite, SWT.FLAT);
        shell.open();
        button.setVisible(false);

        Assert.assertFalse(WidgetHelper.isControlSafeAndVisible(button));

        shell.close();
      }
    });

  }

  @Test
  public void testControlSafeAndVisibleDisposed() {
    display.syncExec(new Runnable() {
      @Override
      public void run() {
        shell = new Shell(display);
        shell.setLayout(new FillLayout());
        composite = new Composite(shell, SWT.NONE);

        button = new Button(composite, SWT.FLAT);
        shell.open();

        shell.close();

        Assert.assertFalse(WidgetHelper.isControlSafeAndVisible(button));
      }
    });


  }

  @Test
  public void testControlSafeAndVisibleNull() {
    Button button = null;
    Assert.assertFalse(WidgetHelper.isControlSafeAndVisible(button));
  }
}
