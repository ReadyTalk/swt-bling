package com.readytalk.swt.color;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ColorPaletteTest {
  private ColorPalette colorPalette;

  @Before
  public void setup() {
    colorPalette = new ColorPalette();
  }

  @After
  public void tearDown() {
    colorPalette.clear();
    disposeDisplay();
  }

  @Test
  public void loadColorsFromProperties_loadBundle_sizesMatch() {
    final ResourceBundle bundle = getFakeColorBundle();

    colorPalette.loadColorsFromProperties(bundle);

    assertEquals(bundle.keySet().size(), colorPalette.colorMap.size());
  }

  @Test
  public void loadColorsFromProperties_loadBundleTwice_sizesMatch() {
    final ResourceBundle bundle = getFakeColorBundle();

    colorPalette.loadColorsFromProperties(bundle);
    colorPalette.loadColorsFromProperties(bundle);

    assertEquals(bundle.keySet().size(), colorPalette.colorMap.size());
  }

  @Test
  public void loadColorsFromProperties_loadTestBundle_colorsMatch() {
    colorPalette.loadColorsFromProperties(getFakeColorBundle());

    Display display = Display.getCurrent();
    List<Color> colors = new ArrayList<Color>();
    colors.add(new Color(display, 218, 187, 30));
    colors.add(new Color(display, 190, 167, 213));
    colors.add(new Color(display, 122, 177, 229));

    assertEquals(colors.get(0), colorPalette.getColor("background"));
    assertEquals(colors.get(1), colorPalette.getColor("foreground"));
    assertEquals(colors.get(2), colorPalette.getColor("border"));

    for (Color color : colors) {
      disposeColor(color);
    }
  }

  @Test
  public void clear_colorMapPopulated_colorMapEmpty() {
    final ResourceBundle bundle = getFakeColorBundle();

    colorPalette.loadColorsFromProperties(bundle);

    assertEquals(bundle.keySet().size(), colorPalette.colorMap.size());

    colorPalette.clear();

    assertTrue(colorPalette.colorMap.isEmpty());
  }

  private void disposeColor(Color color) {
    if (color != null && !color.isDisposed()) {
      color.dispose();
    }
  }

  private void disposeDisplay() {
    Display display = Display.getCurrent();

    if (display != null && !display.isDisposed()) {
      display.dispose();
    }
  }

  private ResourceBundle getFakeColorBundle() {
    final Map<String, String> fakeColors = new HashMap<String, String>();
    fakeColors.put("background", "#dabb1e");
    fakeColors.put("foreground", "#bea7d5");
    fakeColors.put("border", "#7ab1e5");

    final ResourceBundle bundle = new ResourceBundle() {
      @Override
      protected Object handleGetObject(String key) {
        return fakeColors.get(key);
      }

      @Override
      public Enumeration<String> getKeys() {
        return Collections.enumeration(fakeColors.keySet());
      }
    };

    return bundle;
  }
}
