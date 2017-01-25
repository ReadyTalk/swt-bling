package com.readytalk.swt.font;

import com.google.common.io.Files;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FontLoaderTest {
  private static final String TEST_STRING = "It was the best of times, it was the blurst of times";
  private static final String FONT_NAME = "Super Ultra Comic Sans Plus Plus Monospace";
  private static final int STYLE = SWT.BOLD;

  private final UUID TEST_ID = UUID.randomUUID();
  private final String TEST_FONT_FILE_PATH = Files.createTempDir().getAbsolutePath();

  private FontLoader fontLoader;

  @Before
  public void setup() {
    fontLoader = new FontLoader(Display.getDefault(), TEST_FONT_FILE_PATH);
  }

  @After
  public void tearDown() {
    fontLoader.deleteExtractedFiles();

    // Clean up extracted font files
    File fontFileDir = new File(TEST_FONT_FILE_PATH);
    fontFileDir.delete();

    // Ensure a fresh Display for each test
    Display.getDefault().dispose();
  }

  @Test
  public void extractFontFile_validFontName_fontFileExtracted() {
    final File fontFile = new File(TEST_FONT_FILE_PATH + File.separator + "TestFontFile_" + TEST_ID);
    final InputStream input = new ByteArrayInputStream(TEST_STRING.getBytes());

    removeFile(fontFile);

    assertTrue(fontLoader.extractFontFile(fontFile, input));
    assertEquals(TEST_STRING.getBytes().length, fontFile.length());

    removeFile(fontFile);
  }

  @Test
  public void copy_validInput_validOutput() throws Exception {
    final InputStream input = new ByteArrayInputStream(TEST_STRING.getBytes());
    final OutputStream output = new ByteArrayOutputStream();

    FontLoader.copy(input, output);

    assertEquals(TEST_STRING, output.toString());
  }



  @Test
  public void deleteExtractedFiles_fontFilesExtracted_extractedFilesListEmpty() {
    fontLoader.loadFont(TEST_FONT_FILE_PATH, new ByteArrayInputStream(TEST_STRING.getBytes()));

    assertFalse(fontLoader.extractedFiles.isEmpty());
    assertEquals(1, new File(TEST_FONT_FILE_PATH).list().length);

    fontLoader.deleteExtractedFiles();

    assertTrue(fontLoader.extractedFiles.isEmpty());
    assertEquals(0, new File(TEST_FONT_FILE_PATH).list().length);
  }

  private void removeFile(File file) {
    if (file.exists()) {
      file.delete();
    }
  }

  private boolean isFontDataMatch(Font font, String name, int size, int style) {
    final FontData fontData = font.getFontData()[0];
    return name.equals(fontData.getName()) && size == fontData.getHeight() && style == fontData.getStyle();
  }

  /*private void verifyFontFileDirectory() {
    final File fontDir = new File(TEST_FONT_FILE_PATH);
    final ImmutableList<String> fontFilesInDirectory = ImmutableList.copyOf(fontDir.list());

    assertEquals(FontLoader.DEFAULT_FONT_FILE_NAMES.length, fontFilesInDirectory.size());

    for (String fontName : FontLoader.DEFAULT_FONT_FILE_NAMES) {
      assertTrue(fontFilesInDirectory.contains(fontName));
    }
  }

  private void verifyDefaultFontsLoaded() {
    for (String fontName : FontLoader.DEFAULT_FONT_NAMES) {
      assertTrue(Display.getCurrent().getFontList(fontName, true).length > 0);
    }
  }*/
}
