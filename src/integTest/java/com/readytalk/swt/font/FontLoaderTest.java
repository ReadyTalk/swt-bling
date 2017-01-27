package com.readytalk.swt.font;

import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FontLoaderTest {
  private static final String TEST_FONT_RESOURCE_PATH = "fonts" + File.separator + "FontAwesome.otf";
  private static final String TEST_FONT_FILE_PATH = "src" + File.separator + "test" + File.separator + "resources" +
      File.separator + TEST_FONT_RESOURCE_PATH;

  private FontLoader fontLoader;

  @Before
  public void setup() {
    fontLoader = new FontLoader(Display.getDefault());
  }

  @After
  public void tearDown() {
    // Ensure a fresh Display for each test
    Display.getDefault().dispose();
  }

  @Test(expected = NullPointerException.class)
  public void loadFontResource_invalidResource_throwException() {
    fontLoader.loadFontResource("bogus/resource/path");
  }

  @Test
  public void loadFontFile_invalidFile_returnFalse() {
    assertFalse(fontLoader.loadFontFile("bogus/file/path"));
  }

  @Test
  public void loadFontResource_validResource_loadFontReturnTrue() throws Exception {
    assertTrue(fontLoader.loadFontResource(TEST_FONT_RESOURCE_PATH));
  }

  @Test
  public void loadFontFile_validFile_loadFontReturnTrue() throws Exception {
    assertTrue(fontLoader.loadFontFile(TEST_FONT_FILE_PATH));
  }
}
