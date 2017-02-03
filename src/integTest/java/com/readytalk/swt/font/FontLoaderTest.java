package com.readytalk.swt.font;

import com.google.common.io.Files;
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
  private static final String TEST_FONT_FILE_NAME = "FontAwesome.otf";
  private static final String TEST_FONT_RESOURCE_DIR = "fonts";
  private static final String TEST_FONT_RESOURCE_PATH = TEST_FONT_RESOURCE_DIR + File.separator + TEST_FONT_FILE_NAME;
  private static final String TEST_FONT_FILE_PATH = "src" + File.separator + "integTest" + File.separator + "resources" +
      File.separator + TEST_FONT_RESOURCE_PATH;

  private String fontFileExtractPath;
  private UUID testId;

  private FontLoader fontLoader;

  @Before
  public void setup() {
    fontFileExtractPath = Files.createTempDir().getAbsolutePath();
    testId = UUID.randomUUID();
    fontLoader = new FontLoader(Display.getDefault());
  }

  @After
  public void tearDown() {
    fontLoader.deleteExtractedFiles();

    // Clean up extracted font file directory
    File fontFileDir = new File(fontFileExtractPath);
    removeFile(fontFileDir);

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

  @Test
  public void extractFontFile_validFontName_fontFileExtracted() {
    final File fontFile = new File(fontFileExtractPath + File.separator + "TestFontFile_" + testId);
    final InputStream input = new ByteArrayInputStream(TEST_STRING.getBytes());

    removeFile(fontFile);

    assertTrue(fontLoader.extractFontFile(fontFile, input));
    assertEquals(TEST_STRING.getBytes().length, fontFile.length());

    removeFile(fontFile);
  }

  @Test
  public void extractAndLoadFontResource_validFontResource_fontExtractedAndLoaded() {
    assertTrue(fontLoader.extractAndLoadFontResource(TEST_FONT_RESOURCE_PATH, TEST_FONT_FILE_NAME, fontFileExtractPath));
  }

  @Test
  public void deleteExtractedFiles_fontFilesExtracted_extractedFilesListEmpty() {
    fontLoader.extractAndLoadFontResource(TEST_FONT_RESOURCE_PATH, TEST_FONT_FILE_NAME, fontFileExtractPath);

    assertFalse(fontLoader.extractedFiles.isEmpty());
    assertEquals(1, new File(fontFileExtractPath).list().length);

    fontLoader.deleteExtractedFiles();

    assertTrue(fontLoader.extractedFiles.isEmpty());
    assertEquals(0, new File(fontFileExtractPath).list().length);
  }

  @Test
  public void copy_validInput_validOutput() throws Exception {
    final InputStream input = new ByteArrayInputStream(TEST_STRING.getBytes());
    final OutputStream output = new ByteArrayOutputStream();

    fontLoader.copy(input, output);

    assertEquals(TEST_STRING, output.toString());
  }

  private void removeFile(File file) {
    if (file.exists()) {
      file.delete();
    }
  }
}
