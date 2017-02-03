package com.readytalk.swt.color;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
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

    assertEquals(new Color(Display.getCurrent(), 218, 187, 30), colorPalette.getColor("background"));
    assertEquals(new Color(Display.getCurrent(), 190, 167, 213), colorPalette.getColor("foreground"));
    assertEquals(new Color(Display.getCurrent(), 122, 177, 229), colorPalette.getColor("border"));
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
