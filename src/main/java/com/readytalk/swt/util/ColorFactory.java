package com.readytalk.swt.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.ref.Reference;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * This is meant as a central repository for getting colors so that we don't allocate
 * duplicates everywhere, which greatly decreases the number of colors we allocate.
 * This currently doesn't do any reference counting or anything else, which we should
 * probably implement in the future.
 */
public class ColorFactory {

  private static final int FIRST_DISPOSE_DELAY = 1000;
  private static final int DISPOSE_PERIOD = 1000;
  static Map<RGB, WeakReference<Color>> colorMap;
  static ReferenceQueue<Color> colorReferenceQueue;
  static Timer timer = new Timer();
  static TimerTask resourceDisposerTask = new TimerTask() {
    @Override
    public void run() {
      synchronized (colorReferenceQueue) {
        try {
          Reference<? extends Color> ref;
          while ((ref = colorReferenceQueue.remove()) != null) {
            Color color = ref.get();
            colorMap.remove(color.getRGB());
            if (color != null && !color.isDisposed()) {
              color.dispose();
            }
          }
        } catch (Exception e) {
          // TODO Don't know what to do with this yet.... figure it out.
          e.printStackTrace();
        }
      }
    }
  };

  static {
    colorMap = Collections.synchronizedMap(new WeakHashMap<RGB, WeakReference<Color>>());
    colorReferenceQueue = new ReferenceQueue<Color>();
    timer = new Timer();
    timer.scheduleAtFixedRate(resourceDisposerTask, FIRST_DISPOSE_DELAY, DISPOSE_PERIOD);
  }

  // this should use the current display
  public static Color getColor(int red, int green, int blue) {
    return getColor(Display.getCurrent(), red, green, blue);
  }

  /**
   * Get a pre-generated Color object based on the passed in parameters.
   * This Color object may be shared, so it SHOULD NOT be disposed except through this
   * class' disposeAll() method. The owner of the Color object is ColorFactory, not the
   * caller of this method.
   * @param device Device object needed to create the color
   * @param red
   * @param green
   * @param blue
   * @return A possibly shared Color object with the specified components
   */
  public static Color getColor(Device device, int red, int green, int blue) {
    RGB rgb = new RGB(red, green, blue);
    return getColor(device, rgb);
  }

  /**
   * Get a pre-generated Color object based on the passed in parameters.
   * This Color object may be shared, so it SHOULD NOT be disposed except through this
   * class' disposeAll() method. The owner of the Color object is ColorFactory, not the
   * caller of this method.
   * @param device Device object needed to create the color
   * @param rgb
   * @return A possibly shared Color object with the specified rgb values
   */
  public static Color getColor(Device device, RGB rgb) {
    WeakReference<Color> ref = colorMap.get(rgb);
    if (ref == null || ref.get() == null) {
      Color returnval = buildColor(device, rgb);
      return returnval;
    }
    return ref.get();
  }

  static Color buildColor(Device device, RGB rgb) {
    Color color = new Color(device, rgb);

    WeakReference weakref = new WeakReference(color, colorReferenceQueue);
    synchronized (colorReferenceQueue) {
      weakref.enqueue();
    }

    colorMap.put(rgb, weakref);
    return color;
  }

  /**
   * Disposes all the colors and clears the internal storage of colors. Does not do ref counting,
   * so use this with care.
   */
  public static void disposeAll() {
    for (WeakReference<Color> current: colorMap.values()) {
      current.get().dispose();
    }
    colorMap.clear();
  }

  static int getColorCount() {
    int refct = 0;
    for (WeakReference<Color> ref: colorMap.values()) {
      if(ref.get()!=null) {
        refct++;
      }
    }
    return refct;
  }
}
