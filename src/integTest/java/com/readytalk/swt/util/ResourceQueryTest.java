package com.readytalk.swt.util;

import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResourceQueryTest {

  private Display display;

  @Before
  public void setUp() throws Exception {
    DeviceData data = new DeviceData();
    data.tracking = true;
    display = new Display(data);
  }

  @After
  public void tearDown() {
    display.dispose();
  }

  @Test
  public void getResources_Create4Fonts_CountEquals4() {
    for (int i = 10; i < 14; i++) {
      FontFactory.getFont(display, i);
    }
    Assert.assertEquals(4, ResourceQuery.getAllocatedResourceCounts(display).get(Font.class.getName()).intValue());
  }

  @Test
  public void getResources_Create4Colors_CountEqualsInitialCountPlus4() {
    int initialCount = ResourceQuery.getAllocatedResourceCounts(display).get(Color.class.getName()).intValue();
    for (int i = 10; i < 14; i++) {
      ColorFactory.getColor(display, 73, i, i);
    }
    Assert.assertEquals(initialCount + 4,
        ResourceQuery.getAllocatedResourceCounts(display).get(Color.class.getName()).intValue());
  }

  @Test
  public void getResources_Create4Cursors_CountEquals4() {
    Map<String, Integer> counts = ResourceQuery.getAllocatedResourceCounts(display);
    for (int i = 0; i < 4; i++) {
      new Cursor(display, i);
    }
    Assert.assertEquals(4, ResourceQuery.getAllocatedResourceCounts(display).get(Cursor.class.getName()).intValue());
  }

  @Test
  public void getResources_Create4Regions_CountEquals4() {
    Map<String, Integer> counts = ResourceQuery.getAllocatedResourceCounts(display);
    for (int i = 10; i < 14; i++) {
      Region r = new Region(display);
      r.add(0,0, i,i);
    }
    Assert.assertEquals(4, ResourceQuery.getAllocatedResourceCounts(display).get(Region.class.getName()).intValue());
  }
}
