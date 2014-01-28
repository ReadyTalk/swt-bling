package com.readytalk.swt.util;


import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.widgets.Display;


/**
 * ResourceQuery returns mmp of key value pairs of the count results
 * contained in the DeviceData array passed into display.  DeviceData
 * will hold references to resources; so this should probably not be used
 * in production.
 */
public class ResourceQuery {

  public static Map<String, Integer> getAllocatedResourceCounts(final Display display) {
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
