package com.readytalk.swt.util;

import java.nio.charset.Charset;
import java.util.ArrayList;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ColorFactoryTest {

  private Shell shell;

  @Before
  public void setup() throws Exception {
    DeviceData data = new DeviceData();
    data.tracking = true;
    Display display = new Display(data);
    shell = new Shell(display);
  }

  @Test
  public void garbageCollection_DisposesColors() throws InterruptedException {

    ArrayList<Color> list =  new ArrayList<Color>();
    DeviceData info = shell.getDisplay().getDeviceData();

    // Fifty Shades of Gray
    for (int i = 0; i < 50; i++) {
      list.add(ColorFactory.getColor(shell.getDisplay(), i, i, i));
      info = shell.getDisplay().getDeviceData();
      System.out.println(info.objects.length);
    }

    for (int i = 0; i < 10; i++) {
      list.remove(i);
      System.gc();
      Thread.sleep(100);
      info = shell.getDisplay().getDeviceData();
      System.out.println("Cleared to " + info.objects.length);
    }

    list.clear();
    System.gc();
    Thread.sleep(1000);
    info = shell.getDisplay().getDeviceData();
    System.out.println("> Cleared to " + info.objects.length);

  }

}
