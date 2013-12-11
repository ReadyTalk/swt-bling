package com.readytalk.examples.swt.util;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import com.readytalk.examples.swt.SwtBlingExample;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Sleak {
  Display display;
  Shell shell;
  List list;
  Canvas canvas;
  Button capture, runExample, clear, check;
  Text text;
  Label label;

  SwtBlingExample example;
  Display exampleDisplay;
  Shell exampleShell;

  Object [] oldObjects = new Object [0];
  Error [] oldErrors = new Error [0];
  Object [] objects = new Object [0];
  Error [] errors = new Error [0];

  public Sleak(SwtBlingExample example, Display exampleDisplay, Shell exampleShell) {
    this.example = example;
    this.exampleDisplay = exampleDisplay;
    this.exampleShell = exampleShell;
  }

  public void createGUI() {
    display = Display.getCurrent ();
    shell = new Shell (display);
    shell.setText ("S-Leak");

    FormLayout formLayout = new FormLayout();
    formLayout.marginHeight = 5;
    formLayout.marginWidth = 5;
    shell.setLayout(formLayout);

    FormData data = new FormData();

    capture = new Button (shell, SWT.PUSH);
    capture.setText("Capture");
    capture.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        refreshAll();
      }
    });
    data.top = new FormAttachment(0);
    data.left = new FormAttachment(0);
    data.right = new FormAttachment(30);
    capture.setLayoutData(data);

    runExample = new Button(shell, SWT.PUSH);
    runExample.setText("Run Example");
    runExample.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        if (exampleShell.isDisposed()) {
          exampleShell = new Shell(exampleDisplay);
        }
        example.run(exampleDisplay, exampleShell);
      }
    });
    data = new FormData();
    data.top = new FormAttachment(capture);
    data.left = new FormAttachment(0);
    data.right = new FormAttachment(30);
    runExample.setLayoutData(data);

    clear = new Button (shell, SWT.PUSH);
    clear.setText("Clear");
    clear.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        refreshDifference();
      }
    });
    data = new FormData();
    data.top = new FormAttachment(runExample);
    data.left = new FormAttachment(0);
    data.right = new FormAttachment(30);
    clear.setLayoutData(data);

    check = new Button (shell, SWT.CHECK);
    check.setText ("Stack");
    check.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        toggleStackTrace ();
      }
    });
    data = new FormData();
    data.top = new FormAttachment(clear);
    data.left = new FormAttachment(0);
    data.right = new FormAttachment(30);
    check.setLayoutData(data);

    list = new List (shell, SWT.BORDER | SWT.V_SCROLL);
    list.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        refreshObject ();
      }
    });
    data = new FormData();
    data.top = new FormAttachment(check);
    data.left = new FormAttachment(0);
    data.right = new FormAttachment(30);
    data.bottom = new FormAttachment(80);
    list.setLayoutData(data);

    label = new Label (shell, SWT.BORDER);
    label.setText ("0 object(s)");
    data = new FormData();
    data.top = new FormAttachment(list);
    data.left = new FormAttachment(0);
    data.right = new FormAttachment(30);
    data.bottom = new FormAttachment(100);
    label.setLayoutData(data);

    text = new Text (shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    data = new FormData();
    data.top = new FormAttachment(0);
    data.left = new FormAttachment(list);
    data.bottom = new FormAttachment(100);
    data.right = new FormAttachment(100);
    text.setLayoutData(data);

    canvas = new Canvas (shell, SWT.BORDER);
    canvas.addListener (SWT.Paint, new Listener () {
      public void handleEvent (Event event) {
        paintCanvas (event);
      }
    });
    data = new FormData();
    data.top = new FormAttachment(0);
    data.left = new FormAttachment(list);
    data.bottom = new FormAttachment(100);
    data.right = new FormAttachment(100);
    canvas.setLayoutData(data);

    check.setSelection (false);
    text.setVisible (false);
    Point size = shell.getSize ();
    shell.setSize (size.x / 2, size.y / 2);
  }

  public void open() {
    if (shell == null || shell.isDisposed()) {
      createGUI();
    }

    shell.open ();
  }

  void refreshLabel () {
    int colors = 0, cursors = 0, fonts = 0, gcs = 0, images = 0, regions = 0;
    for (int i=0; i<objects.length; i++) {
      Object object = objects [i];
      if (object instanceof Color) colors++;
      if (object instanceof Cursor) cursors++;
      if (object instanceof Font) fonts++;
      if (object instanceof GC) gcs++;
      if (object instanceof Image) images++;
      if (object instanceof Region) regions++;
    }
    String string = "";
    if (colors != 0) string += colors + " Color(s)\n";
    if (cursors != 0) string += cursors + " Cursor(s)\n";
    if (fonts != 0) string += fonts + " Font(s)\n";
    if (gcs != 0) string += gcs + " GC(s)\n";
    if (images != 0) string += images + " Image(s)\n";
    /* Currently regions are not counted. */
    //  if (regions != 0) string += regions + " Region(s)\n";
    if (string.length () != 0) {
      string = string.substring (0, string.length () - 1);
    }
    label.setText (string);
  }

  void refreshDifference () {
    DeviceData info = display.getDeviceData ();
    if (!info.tracking) {
      MessageBox dialog = new MessageBox (shell, SWT.ICON_WARNING | SWT.OK);
      dialog.setText (shell.getText ());
      dialog.setMessage ("Warning: Device is not tracking resource allocation");
      dialog.open ();
    }
    Object [] newObjects = info.objects;
    Error [] newErrors = info.errors;
    Object [] diffObjects = new Object [newObjects.length];
    Error [] diffErrors = new Error [newErrors.length];
    int count = 0;
    for (int i=0; i<newObjects.length; i++) {
      int index = 0;
      while (index < oldObjects.length) {
        if (newObjects [i] == oldObjects [index]) break;
        index++;
      }
      if (index == oldObjects.length) {
        diffObjects [count] = newObjects [i];
        diffErrors [count] = newErrors [i];
        count++;
      }
    }
    objects = new Object [count];
    errors = new Error [count];
    System.arraycopy (diffObjects, 0, objects, 0, count);
    System.arraycopy (diffErrors, 0, errors, 0, count);
    list.removeAll ();
    text.setText ("");
    canvas.redraw ();
    for (int i=0; i<objects.length; i++) {
      list.add (objectName (objects [i]));
    }
    refreshLabel ();
    layout ();
  }

  String objectName (Object object) {
    String string = object.toString ();
    int index = string.lastIndexOf ('.');
    if (index == -1) return string;
    return string.substring (index + 1, string.length ());
  }

  void toggleStackTrace () {
    refreshObject ();
    layout ();
  }

  void paintCanvas (Event event) {
    canvas.setCursor (null);
    int index = list.getSelectionIndex ();
    if (index == -1) return;
    GC gc = event.gc;
    Object object = objects [index];
    if (object instanceof Color) {
      if (((Color)object).isDisposed ()) return;
      gc.setBackground ((Color) object);
      gc.fillRectangle (canvas.getClientArea());
      return;
    }
    if (object instanceof Cursor) {
      if (((Cursor)object).isDisposed ()) return;
      canvas.setCursor ((Cursor) object);
      return;
    }
    if (object instanceof Font) {
      if (((Font)object).isDisposed ()) return;
      gc.setFont ((Font) object);
      FontData[] array = gc.getFont ().getFontData ();
      String string = "";
      String lf = text.getLineDelimiter ();
      for (int i=0; i<array.length; i++) {
        FontData data = array [i];
        String style = "NORMAL";
        int bits = data.getStyle ();
        if (bits != 0) {
          if ((bits & SWT.BOLD) != 0) style = "BOLD ";
          if ((bits & SWT.ITALIC) != 0) style += "ITALIC";
        }
        string += data.getName () + " " + data.getHeight () + " " + style + lf;
      }
      gc.drawString (string, 0, 0);
      return;
    }
    if (object instanceof Image) {
      if (((Image)object).isDisposed ()) return;
      gc.drawImage ((Image) object, 0, 0);
      return;
    }
    if (object instanceof Region) {
      if (((Region)object).isDisposed ()) return;
      String string = ((Region)object).getBounds().toString();
      gc.drawString (string, 0, 0);
      return;
    }
  }

  void refreshObject () {
    int index = list.getSelectionIndex ();
    if (index == -1) return;
    if (check.getSelection ()) {
      ByteArrayOutputStream stream = new ByteArrayOutputStream ();
      PrintStream s = new PrintStream (stream);
      errors [index].printStackTrace (s);
      text.setText (stream.toString ());
      text.setVisible (true);
      canvas.setVisible (false);
    } else {
      canvas.setVisible (true);
      text.setVisible (false);
      canvas.redraw ();
    }
  }

  void refreshAll () {
    oldObjects = new Object [0];
    oldErrors = new Error [0];
    refreshDifference ();
    oldObjects = objects;
    oldErrors = errors;
  }

  void layout () { }

  public Shell getShell() {
    return shell;
  }
}