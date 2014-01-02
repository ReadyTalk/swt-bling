package com.readytalk.swt.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FontFactoryTest {

  public static final String MONOSPACED = "Monospaced";
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
    display.dispose();
    FontFactory.disposeAll();
  }

  @Test
  public void getFont_CreatesFont_OneObjectAddedToDeviceDataObjects() {
    int numberOfItemsBefore = shell.getDisplay().getDeviceData().objects.length;
    FontFactory.getFont(shell.getDisplay(), 12, SWT.NORMAL);
    Assert.assertEquals(numberOfItemsBefore+1, shell.getDisplay().getDeviceData().objects.length);
  }

  @Test
  public void getFont_CreateAndDisposeFont_NoAdditionalObjectInoDeviceDataObjects() {
    int numberOfItemsBefore = shell.getDisplay().getDeviceData().objects.length;
    FontFactory.getFont(shell.getDisplay());
    FontFactory.disposeAll();
    Assert.assertEquals(shell.getDisplay().getDeviceData().objects.length, numberOfItemsBefore);
  }

  @Test
  public void getFont_CreateThreeDifferentFonts_FontMapSizeIs3() {
    FontFactory.getFont(shell.getDisplay(), 13);
    FontFactory.getFont(shell.getDisplay(), 14);
    FontFactory.getFont(shell.getDisplay(), 15);
    Assert.assertEquals(3, FontFactory.fontMap.size());
  }

  @Test
  public void getFont_CreateAndDisposeFont_FontMapSizeIs0() {
    FontFactory.getFont(shell.getDisplay(), 23);
    FontFactory.disposeAll();
    Assert.assertEquals(0, FontFactory.fontMap.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void getFont_CreateFontViaNegativeSize_ThrowsIllegalArgumentException() {
    FontFactory.getFont(shell.getDisplay(), -23);
  }
}
