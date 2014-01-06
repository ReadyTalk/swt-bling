package com.readytalk.swt.util;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ColorFactoryTest {

  private Shell shell;
  private Display display;

  @Before
  public void setup() throws Exception {
    DeviceData data = new DeviceData();
    data.tracking = true;
    display = new Display(data);
    shell = new Shell(display);
  }

  @After
  public void tearDown() throws Exception {
    if (display != null) {
      display.dispose();
    }
    ColorFactory.disposeAll();
  }

  @Test
  public void getColor_CreatesColor_OneObjectAddedToDeviceDataObjects() {
    int numberOfItemsBefore = shell.getDisplay().getDeviceData().objects.length;
    ColorFactory.getColor(shell.getDisplay(), 23, 34, 45);
    Assert.assertEquals(numberOfItemsBefore+1, shell.getDisplay().getDeviceData().objects.length);
  }

  @Test
  public void getColor_CreateAndDisposeColor_NoAdditionalObjectInoDeviceDataObjects() {
    int numberOfItemsBefore = shell.getDisplay().getDeviceData().objects.length;
    ColorFactory.getColor(shell.getDisplay(), 23, 34, 45);
    ColorFactory.disposeAll();
    Assert.assertEquals(shell.getDisplay().getDeviceData().objects.length, numberOfItemsBefore);
  }

  @Test
  public void getColor_CreateTwoSameColor_OnlyOneObjectAddedToDeviceDataObjects() {
    ColorFactory.getColor(shell.getDisplay(), 23, 34, 45);
    int numberOfItemsBefore = shell.getDisplay().getDeviceData().objects.length;
    ColorFactory.getColor(shell.getDisplay(), 23, 34, 45);
    Assert.assertEquals(shell.getDisplay().getDeviceData().objects.length, numberOfItemsBefore);
  }

  @Test
  public void getColor_CreateTwoSameColor_GetSameObjectFromColorMap() {
    Color a = ColorFactory.getColor(shell.getDisplay(), 23, 34, 45);
    Color b = ColorFactory.getColor(shell.getDisplay(), 23, 34, 45);

    // Equals may compare RGB values in some cases; so we look at the original object hashcode
    Assert.assertEquals(System.identityHashCode(a), System.identityHashCode(b));
  }

  @Test
  public void getColor_CreateThreeDifferentColors_ColorMapSizeIs3() {
    ColorFactory.getColor(shell.getDisplay(), 23, 34, 45);
    ColorFactory.getColor(shell.getDisplay(), 255, 214, 55);
    ColorFactory.getColor(shell.getDisplay(), 255, 94, 55);
    Assert.assertEquals(3, ColorFactory.colorMap.size());
  }

  @Test
  public void getColor_CreateAndDisposeColor_ColorMapSizeIs0() {
    ColorFactory.getColor(shell.getDisplay(), 23, 34, 45);
    ColorFactory.disposeAll();
    Assert.assertEquals(0, ColorFactory.colorMap.size());
  }

  @Test
  public void getColor_CreateColorViaDeviceAndInts_ReturnsExpectedColor() {
    Color c = ColorFactory.getColor(shell.getDisplay(), 23, 34, 45);
    Assert.assertEquals(c.getRGB(), new RGB(23, 34, 45));
  }

  @Test
  public void getColor_CreateColorViaRGB_ReturnsExpectedColor() {
    RGB rgb = new RGB(23, 34, 45);
    Color c = ColorFactory.getColor(shell.getDisplay(),rgb);
    Assert.assertEquals(c.getRGB(), rgb);
  }

  @Test
  public void getColor_CreateColorViaInts_ReturnsExpectedColor() {
    Color c = ColorFactory.getColor(33, 34, 45);
    Assert.assertEquals(c.getRGB(), new RGB(33, 34, 45));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getColor_CreateColorViaNegativeInts_ThrowsIllegalArgumentException() {
    ColorFactory.getColor(-33, -34, -45);
  }
}
