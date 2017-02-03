package com.readytalk.swt.font;

import com.google.common.annotations.VisibleForTesting;
import lombok.extern.java.Log;
import org.eclipse.swt.widgets.Display;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

/**
 * A utility class for loading custom fonts into SWT
 */
@Log
public class FontLoader {
  private static final int BUFFER_SIZE = 1024 * 4;

  private final Display display;

  @VisibleForTesting
  final Set<File> extractedFiles = new HashSet<File>();

  /**
   * Creates an instance of FontLoader with the specified Display
   *
   * @param display
   */
  public FontLoader(Display display) {
    this.display = display;

    // Dispose all cached font resources on exit and clean up extracted files if possible
    if (display != null && !display.isDisposed()) {
      display.disposeExec(new Runnable() {
        @Override
        public void run() {
          deleteExtractedFiles();
        }
      });
    }
  }

  /**
   * Load a font from a resource located at fontFileResourcePath into SWT
   *
   * @param fontFileResourcePath
   * @return true if the font was successfully loaded, otherwise false
   */
  public boolean loadFontResource(String fontFileResourcePath) {
    return loadFontFile(FontLoader.class.getClassLoader().getResource(fontFileResourcePath).getPath());
  }

  /**
   * Load a font from a file located at fontFilePath into SWT
   *
   * @param fontFilePath
   * @return true if the font was successfully loaded, otherwise false
   */
  public boolean loadFontFile(String fontFilePath) {
    return display.loadFont(fontFilePath);
  }

  /**
   * Extract a font from a resource fontFileResourcePath to a temporary file named fontFileName in the directory at
   * extractionPath and load it into SWT
   *
   * @param fontFileResourcePath
   * @param fontFileName
   * @param extractionPath
   * @return
   */
  public boolean extractAndLoadFontResource(String fontFileResourcePath, String fontFileName, String extractionPath) {
    final InputStream fontFileResourceInputStream = FontLoader.class.getClassLoader()
        .getResourceAsStream(fontFileResourcePath);
    return extractAndLoadFontFromStream(fontFileName, extractionPath, fontFileResourceInputStream);
  }

  /**
   * Extract a font from a resource InputStream named fontFileName to a temporary file in the directory at
   * extractionPath and load it into SWT
   *
   * @param fontFileName
   * @param extractionPath
   * @param fontInputStream
   * @return
   */
  @VisibleForTesting
  boolean extractAndLoadFontFromStream(String fontFileName, String extractionPath, InputStream fontInputStream) {
    // SWT requires the font be loaded from a file on disk so we extract the font resource to a temp file
    final File fontFile = new File(extractionPath, fontFileName);
    final boolean extractSuccess = extractFontFile(fontFile, fontInputStream);

    return extractSuccess && display.loadFont(fontFile.getAbsolutePath());
  }

  /**
   * Extracts a font file resource from the classpath to fontFile, creating any directories if they don't exist,
   * and then copies the contents of the resource to the file. If fontFile initially exists it will be deleted before
   * recreating and copying the contents from the InputStream.
   *
   * @param fontFile
   * @param in
   * @return
   */
  @VisibleForTesting
  boolean extractFontFile(File fontFile, InputStream in) {
    boolean success = false;

    if (in != null) {
      try {
        if (fontFile.exists()) {
          log.info("Deleting existing font file " + fontFile.getAbsolutePath());
          fontFile.delete();
        }

        log.info("Extracting font file to: " + fontFile.getAbsolutePath());

        fontFile.getParentFile().mkdirs();
        fontFile.createNewFile();

        final OutputStream out = new FileOutputStream(fontFile);

        try {
          copy(in, out);
          success = true;
          extractedFiles.add(fontFile);
        } finally {
          out.close();
        }
      } catch (IOException e) {
        log.log(Level.WARNING, "Exception loading font from file: " + fontFile.getAbsolutePath(), e);
      }
    } else {
      log.warning("Unable to get font file resource as stream: " + fontFile.getAbsolutePath());
    }

    return success;
  }

  /**
   * Remove any font files extracted to the file system
   */
  public void deleteExtractedFiles() {
    for (File file : extractedFiles) {
      if (file != null && file.exists()) {
        log.info("Deleting extracted font file: " + file);
        file.delete();
      }
    }

    extractedFiles.clear();
  }

  /**
   * Copy bytes from an InputStream to an OutputStream
   *
   * @param input  the InputStream to read from
   * @param output the OutputStream to write to
   * @return the number of bytes copied
   */
  @VisibleForTesting
  static long copy(InputStream input, OutputStream output) throws IOException {
    final byte[] buffer = new byte[BUFFER_SIZE];
    long count = 0;
    int n;

    while (-1 != (n = input.read(buffer))) {
      output.write(buffer, 0, n);
      count += n;
    }

    return count;
  }
}
