package com.readytalk.examples.swt;

import com.readytalk.examples.swt.util.Sleak;
import com.readytalk.swt.util.ColorFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.logging.Logger;

public class Examples {
  private static final Logger logger = Logger.getLogger("com.readytalk.examples.Example");

  public static void main(String[] args) {
    if (args.length <= 0) {
      logger.severe("You must pass a parameter to the Examples class");
      return;
    }

    String exampleToRun = args[0];
    boolean debug = false;
    if (args.length > 1 && args[1].equals("sleakDebug")) {
      debug = true;
    }

    Reflections reflections = new Reflections(new ConfigurationBuilder().
                                                  setUrls(ClasspathHelper.forPackage("com.readytalk.examples")).
                                                  setScanners(new MethodAnnotationsScanner()));
    Set<Constructor> constructors = reflections.getConstructorsAnnotatedWith(RunnableExample.class);

    for (Constructor constructor : constructors) {
      RunnableExample annotation = (RunnableExample) constructor.getAnnotation(RunnableExample.class);
      if (annotation.name().equals(exampleToRun)) {
        try {
          Display display = createDisplay(debug);
          Shell exampleShell = new Shell(display);
          SwtBlingExample example = (SwtBlingExample) constructor.newInstance();
          exampleShell.addListener(SWT.Dispose, new Listener() {
            public void handleEvent(Event event) {
              switch (event.type) {
                case SWT.Dispose:
                  ColorFactory.disposeAll();
                  break;
              }
            }
          });

          Sleak sleak = null;
          if (debug) {
            sleak = new Sleak(example, display, exampleShell);
            sleak.open();
          } else {
            example.run(display, exampleShell);
          }

          runGuiLoop(display, exampleShell, sleak);
          return;
        } catch (InstantiationException e) {
          logger.severe("Caught InstantiationException");
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          logger.severe("Caught IllegalAccessException");
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          logger.severe("Caught InvocationTargetException");
          e.printStackTrace();
        }
      }
    }

    logger.severe("Coudn't find requested example " + exampleToRun);
    StringBuilder validChoices = new StringBuilder();
    validChoices.append("Valid choices are:\n");
    for (Constructor constructor : constructors) {
      RunnableExample annotation = (RunnableExample) constructor.getAnnotation(RunnableExample.class);
      validChoices.append("    * " + annotation.name() + "\n");
    }
    logger.info(validChoices.toString());
  }

  private static void runGuiLoop(Display display, Shell exampleShell, Sleak sleak) {
    Shell shellToMonitor;
    if (sleak != null) {
      shellToMonitor = sleak.getShell();
    } else {
      shellToMonitor = exampleShell;
    }

    while (!shellToMonitor.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }

    display.dispose();
  }

  private static Display createDisplay(boolean debug) {
    Display display;
    if (debug) {
      DeviceData data = new DeviceData();
      data.tracking = true;
      display = new Display(data);
    } else {
      display = new Display();
    }

    return display;
  }
}
