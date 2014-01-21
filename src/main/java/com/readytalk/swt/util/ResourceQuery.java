package com.readytalk.swt.util;


import java.util.HashMap;

import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.widgets.Display;

public class ResourceQuery {

  public static HashMap<String, Integer> getAllocatedResourceCounts(final Display display) {
    HashMap<String, Integer> results = new HashMap<String, Integer>();
    DeviceData data = display.getDeviceData();
    for (Object object : data.objects) {
      String name = object.getClass().getCanonicalName();
      Integer count = results.get(name);
      if (count == null) {
        count = 0;
      }
      int c = count.intValue();
      c++;
      results.put(name, c);
    }
    return results;
  }
}
