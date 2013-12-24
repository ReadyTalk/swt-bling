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
  public  void garbageCollection_DisposesColors() throws InterruptedException {
    DeviceData info;
    createColors(10, 10, 10);
    Thread.sleep(1000);
    info = shell.getDisplay().getDeviceData();
    System.out.println("> Cleared to " + info.objects.length);


    for (int i = 0; i < 4; i++) {
      Thread.sleep(1000);
      info = shell.getDisplay().getDeviceData();
      System.gc();
      System.runFinalization();
      System.out.println("Cleared to " + info.objects.length);
      System.out.println("Free Memory  :" + Runtime.getRuntime().freeMemory());
      System.out.println("Total Memory :" + Runtime.getRuntime().totalMemory());
      System.out.println("Max Memory   :" + Runtime.getRuntime().maxMemory());
      createColors(i*50, 10, 10);
    }
  }

  void createColors(int R, int G, int B) throws InterruptedException {
    DeviceData info;

    // Fifty Shades of Gray
    for (int r = 0; r < R; r++) {
      for (int g = 0; g < G; g++) {
        for (int b = 0; b < B; b++) {
          ColorFactory.getColor(shell.getDisplay(), r, g, b);
        }
      }
      System.gc();
      System.runFinalization();
    }

    info = shell.getDisplay().getDeviceData();
    System.out.println(info.objects.length);
    System.out.println("Free Memory  :" + Runtime.getRuntime().freeMemory());
    System.out.println("Total Memory :" + Runtime.getRuntime().totalMemory());
    System.out.println("Max Memory   :" + Runtime.getRuntime().maxMemory());


  }
}
