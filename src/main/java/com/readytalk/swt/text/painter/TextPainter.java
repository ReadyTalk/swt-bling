package com.readytalk.swt.text.painter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.readytalk.swt.text.navigation.Hyperlink;
import com.readytalk.swt.text.navigation.NavigationEvent;
import com.readytalk.swt.text.navigation.NavigationListener;
import com.readytalk.swt.text.tokenizer.TextToken;
import com.readytalk.swt.text.tokenizer.TextTokenizer;
import com.readytalk.swt.text.tokenizer.TextTokenizerFactory;

/**
 * TextPainter is a utility class used to render text onto a Composite within a 
 * given boundary with a call to its handlePaint method.  It can render the 
 * following TextTypes:
 * <p>
 * <li>TEXT</li>
 * <li>BOLD</li>
 * <li>ITALIC</li>
 * <li>BOLD_AND_ITALIC</li>
 * <li>PLAIN_URL</li>
 * <li>LINK_URL</li>
 * <li>LINK_AND_NAMED_URL</li>
 * <li>NAMED_URL</li>
 * </p>
 * <p>
 * TextPainter works by tokenizing text into a TextToken list of the 
 * aforementioned TextTypes and then painting them into the given boundary using 
 * the TextPainter options for guidance.  By default, TextPainter uses a 
 * PlainText tokenizer which does not recognize any text styling rules such as 
 * BOLD or ITALIC.
 * </p>
 * <pre>
 * Example:
 * {@code
 * final TextPainter painter = new TextPainter(this)
 *         .setTokenizer(TextTokenizerFactory.createTextTokenizer(TextTokenizerType.WIKI))
 *         .setText(
 *             "This is '''wiki text''' is auto-wrapped and can display "
 *                 + "''Italic Text,'' '''Bold Text,''' and "
 *                 + "'''''Bold and Italic Text'''''"
 *                 + " naked url: http://www.google.com"
 *                 + " wiki url: [http://www.readytalk.com ReadyTalk]"  
 *                 + " url: [http://www.readytalk.com]")
 *         .setClipping(false).setBounds(wikiTextBounds).setDrawBounds(true)
 *         .setWrapping(true).addNavigationListener(new NavigationListener() {
 *           public void navigate(NavigationEvent event) {
 *             System.out.println("Navigate to: " + event.getUrl());
 *           }
 *         });
 * }
 * </pre>
 */
public class TextPainter {
  
  private Color boundaryColor;
  private Color foregroundColor;
  private Color hyperlinkColor;
  
  private Cursor handCursor;
  
  private Font boldAndItalicFont;
  private Font boldFont;
  private Font font;
  private Font headerFont;
  private Font italicFont;
  private Font underlineFont;
  
  private Hyperlink activeHyperlink;
  private Rectangle bounds;
  private boolean drawCalculatedBounds;
  private boolean clipping;
  private boolean drawBounds;
  private List<Hyperlink> hyperlinks;
  private int justification;
  private int lineSpacing;
  private List<NavigationListener> navigationListeners;
  private Composite parent;
  private String text;
  private TextTokenizer textTokenizer;
  private List<TextToken> tokens;
  private int verticalAlignment;
  private boolean wrapping;

  /**
   * Creates a new TextPainter to paint text onto the given Composite.  
   * 
   * @param parent : Composite
   */
  public TextPainter(final Composite parent) {
    this.parent = parent;
    Listener listener = new Listener() {
      public void handleEvent(Event event) {
        switch (event.type) {
          case SWT.Dispose:
            dispose();
            break;
        }
      }
    };
    parent.addListener(SWT.Dispose, listener);
    
    handCursor = new Cursor(parent.getDisplay(), SWT.CURSOR_HAND);
    text = "";
    clipping = true;
    wrapping = true;
    textTokenizer = TextTokenizerFactory.createDefault();
    foregroundColor = parent.getForeground();
    hyperlinkColor = buildColor(100, 50, 200);
    boundaryColor = buildColor(255, 30, 30);
    justification = SWT.LEFT;
    lineSpacing = 0;
    verticalAlignment = SWT.TOP;
    FontData fontData = parent.getFont().getFontData()[0];
    setFont(fontData.getName(), fontData.getHeight());
    Point size = parent.getSize();
    bounds = new Rectangle(0, 0, size.x, size.y);
    navigationListeners = new ArrayList<NavigationListener>(); 
    hyperlinks = new ArrayList<Hyperlink>();
  
    parent.addMouseMoveListener(new MouseMoveListener() {
      private Cursor defaultCursor = parent.getCursor();
      @Override
      public void mouseMove(MouseEvent e) {
        for (Hyperlink hyperlink : hyperlinks) {
          if (hyperlink.contains(e.x, e.y)) {
            activeHyperlink = hyperlink;
            break;
          } else {
            activeHyperlink = null;
          }
        }
        
        if (activeHyperlink != null) {
          parent.setCursor(handCursor);
        } else {
          parent.setCursor(defaultCursor);
        }
      }
    });
    
    parent.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseUp(MouseEvent e) {
        if (activeHyperlink != null) {
          notifyNavigationListeners(activeHyperlink);
        }
      }
    });
  }
  
  /**
   * Disposes of the operating system resources associated with the 
   * receiver and all its descendants.
   */
  public void dispose() {
    boundaryColor.dispose();
    foregroundColor.dispose();
    hyperlinkColor.dispose();
    font.dispose();
    boldFont.dispose();
    underlineFont.dispose();
    boldAndItalicFont.dispose();
    handCursor.dispose();
  }
  
  /**
   * Returns the list of TextTokens as parsed by the TextTokenizer
   * @return List<TextToken> : The list of TextTokens 
   */
  public List<TextToken> getTokens() {
    return tokens;
  }
  
  private Font buildFont(final String name, final int height, final int style) {
    return new Font(parent.getDisplay(), name, height, style);
  }
  
  private TextPainter setFont(final String name, final int height) {
    if (font != null) {
      font.dispose();
      boldFont.dispose();
      italicFont.dispose();
      underlineFont.dispose();
      boldAndItalicFont.dispose();
    }
 
    font = buildFont(name, height, SWT.NORMAL);
    boldFont = buildFont(name, height, SWT.BOLD);
    italicFont = buildFont(name, height, SWT.ITALIC);
    underlineFont = buildFont(name, height, SWT.UNDERLINE_LINK);
    boldAndItalicFont = buildFont(name, height, SWT.ITALIC|SWT.BOLD);
    return this;
  }
  
  private Color buildColor(final int r, final int g, final int b) {
    return new Color(parent.getDisplay(), r, g, b);
  }
  
  /**
   * Sets the boundary color.  By default, it is set to (255, 30, 30).
   * 
   * @param r : an int representing the red component
   * @param g : an int representing the green component
   * @param b : an int representing the blue component
   * @return {@link TextPainter}
   */
  public TextPainter setBoundaryColor(final int r, final int g, final int b) {
    boundaryColor.dispose();
    boundaryColor = buildColor(r, g, b);
    return this;
  }
  
  /**
   * Sets the text color.  By default, it clones the parent Composite's 
   * foreground color upon construction.
   * 
   * @param r : an int representing the red component
   * @param g : an int representing the green component
   * @param b : an int representing the blue component
   * @return {@link TextPainter}
   */
  public TextPainter setForeground(final int r, final int g, final int b) {
    foregroundColor.dispose();
    foregroundColor = buildColor(r, g, b);
    return this;
  }
  
  /**
   * Sets the hyperlink text color.  By default, it is set to (100, 50, 200).
   * 
   * @param r : an int representing the red component
   * @param g : an int representing the green component
   * @param b : an int representing the blue component
   * @return {@link TextPainter}
   */
  public TextPainter setHyperlinkColor(final int r, final int g, final int b) {
    hyperlinkColor.dispose();
    hyperlinkColor = buildColor(r, g, b);
    return this;
  }
  
  /**
   * Sets the text painting boundary.  By default, it is the size of the 
   * parent Composite.
   * 
   * @return {@link TextPainter}
   */
  public TextPainter setBounds (final Rectangle bounds) {
    this.bounds = bounds;
    return this;
  }

  /**
   * Sets the calculated boundary painting rule.  By default, set to false and the calculated boundary
   * will not be painted.
   *
   * @return {@link TextPainter}
   */
  public TextPainter setDrawCalculatedBounds(boolean drawCalculatedBounds) {
    this.drawCalculatedBounds = drawCalculatedBounds;
    return this;
  }

  /**
   * Sets the clipping paint rule.  By default, clipping is enabled.
   * 
   * @return {@link TextPainter}
   */
  public TextPainter setClipping(final boolean clipping) {
    this.clipping = clipping;
    return this;
  }
  
  /**
   * Sets the boundary painting rule.  By default, set to false and the boundary 
   * will not be painted.
   * 
   * @return {@link TextPainter}
   */
  public TextPainter setDrawBounds(boolean drawBounds) {
    this.drawBounds = drawBounds;
    return this;
  }

  /**
   * Sets the horizontal alignment of text.
   * @param justification
   * @return
   */
  public TextPainter setJustification(int justification) {
    this.justification = justification;
    return this;
  }

  /**
   * Sets the amount of space between lines.  The default is set to 0.
   * @param lineSpacing
   * @return
   */
  public TextPainter setLineSpacing(int lineSpacing) {
    this.lineSpacing = lineSpacing;
    return this;
  }
  
  void tokenizeText() {
    tokens = textTokenizer.tokenize(text);
  }
  
  /**
   * Sets the text to be painted.  By default, this is an empty string.
   * 
   * @return {@link TextPainter}
   */
  public TextPainter setText(final String text) {
    this.text = text;
    tokenizeText();
    return this;
  }
  
  /**
   * Sets the Tokenizer strategy.  By default, this is a PlainTextTokenizer.
   * 
   * @param textTokenizer : {@link TextTokenizer}
   * @return {@link TextPainter}
   */
  public TextPainter setTokenizer(final TextTokenizer textTokenizer){
    this.textTokenizer = textTokenizer;
    tokenizeText();
    return this;
  }
  
  /**
   * Sets the text wrapping paint rule.  By default, this is set to true and
   * text will be wrapped. 
   * 
   * @return {@link TextPainter}
   */
  public TextPainter setWrapping(final boolean wrapping) {
    this.wrapping = wrapping;
    return this;
  }
  
  boolean isOverHyperlink(final int x, final int y) {
    for(Hyperlink hyperlink : hyperlinks) {
      if (hyperlink.contains(x, y)) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Adds a NavigationListener to be called upon navigation events.  Navigation
   * events occur when a mouse up event falls within the boundary of Hyperlink
   * as discovered when painting TextTokens of TextTypes:
   * <p>
   * <li>PLAIN_URL</li>
   * <li>LINK_URL</li>
   * <li>LINK_AND_NAMED_URL</li>
   * <li>NAMED_URL</li>
   * </p>
   * 
   * @return {@link TextPainter}
   */
  public TextPainter addNavigationListener(final NavigationListener listener) {
    if (!navigationListeners.contains(listener)) {
      navigationListeners.add(listener);
    }
    return this;
  }

  void notifyNavigationListeners(Hyperlink hyperlink) {
    NavigationEvent event = new NavigationEvent(hyperlink);
    for (int i = navigationListeners.size() - 1; i >= 0; i--) {
      navigationListeners.get(i).navigate(event);
    }
  }

  /**
   * Removes the NavigationListener from the list of NavigationListeners.  The
   * given NavigationListener will no longer be notified of NavigationEvents.
   * 
   * @param listener : The {@link NavigationListener} to remove
   */
  public void removeNavigationListener(final NavigationListener listener) {
    navigationListeners.remove(listener);
  }
  
  void addIfHyperlink(DrawData drawData, int x, int y) {
    if (drawData.token.getType().equals(TextType.LINK_AND_NAMED_URL)
        || drawData.token.getType().equals(TextType.LINK_URL)
        || drawData.token.getType().equals(TextType.NAKED_URL)
        || drawData.token.getType().equals(TextType.PLAIN_URL)) {
      hyperlinks.add(
          new Hyperlink(drawData.token,
              new Rectangle(x, y, drawData.extent.x + x,  drawData.extent.y + y)));
    }
  }
  
  void configureForStyle(GC gc, TextToken token) {
    gc.setForeground(foregroundColor);
    switch(token.getType()) {
    case BOLD:
      gc.setFont(boldFont);
      break;
    case BOLD_AND_ITALIC:
      gc.setFont(boldAndItalicFont);
      break;
    case HEADER:
      gc.setFont(headerFont);
      break;
    case ITALIC:
      gc.setFont(italicFont);
      break;
    case LINK_AND_NAMED_URL:
    case LINK_URL:
    case NAKED_URL:
    case PLAIN_URL:
      gc.setForeground(hyperlinkColor);
      gc.setFont(underlineFont);
      break;
    case TEXT:
      gc.setForeground(foregroundColor);
      gc.setFont(font);
      break;
    }
  }

  /**
   * Calculates the rectangular bounds of the required to render the text
   * without overflowing the bounds.
   * @return Rectangle
   */
  public Rectangle calculateSize(GC gc) {
    Rectangle bounds = conditionallyPaintText(gc, false);
    return new Rectangle(0, 0, bounds.width - bounds.x, bounds.height - bounds.y);
  }

  class DrawData {
    TextToken token;
    Point extent;

    DrawData(GC gc, TextToken token) {
      configureForStyle(gc, token);
      this.token  = token;
      this.extent = gc.textExtent(token.getText());
    }
  }

  List<DrawData> buildDrawDataList(GC gc) {
    List<DrawData> list = new ArrayList<DrawData>();
    for (int i = 0; i < tokens.size(); i++) {
      list.add(new DrawData(gc, tokens.get(i)));
    }
    return list;
  }

  List<List<DrawData>> buildLines(GC gc) {
    List<List<DrawData>> lines = new ArrayList<List<DrawData>>();
    List<DrawData> line = new ArrayList<DrawData>();
    lines.add(line);
    List<DrawData> data = buildDrawDataList(gc);

    int lineWidth = 0;

    for (DrawData drawData:data) {
      lineWidth += drawData.extent.x;
      if (lineWidth > bounds.width) {
        List<DrawData> newline = new ArrayList<DrawData>();
        lines.add(newline);

        // if there is only one token on the line and it is too
        // wide to fit, force it onto the line
        if (line.size() == 0) {
          line.add(drawData);
        } else {
          newline.add(drawData);
        }
        line = newline;
        lineWidth = drawData.extent.x;
      } else {
        line.add(drawData);
      }
    }
    return lines;
  }

  /**
   * Paints the text using the GC found in the given PaintEvent.  For example,
   * one might call this from the parent Composite's paintControl event handler:
   * <pre>
   * {@code
   *   ...
   *   addPaintListener(new PaintListener() {
   *     public void paintControl(PaintEvent e) {
   *       textPainter.handlePaint(e);
   *     }
   *   });
   * }
   * </pre>
   *
   * @param e : {@link PaintEvent}
   */
  public void handlePaint(final PaintEvent e) {
    conditionallyPaintText(e.gc, true);
  }

  /**
   * Paints text and/or returns a Rectangle representing the computed bounds
   * of the text.  You may only want they rectangle if you are trying to lay the
   * text out.
   *
   * @param gc
   * @param paint
   * @return Rectangle representing the computed bounds
   */
  Rectangle conditionallyPaintText(GC gc, boolean paint) {
    final Rectangle clip = gc.getClipping();
    final Color bg = gc.getBackground();

    if (clipping) {
      gc.setClipping(this.bounds);
    }

    hyperlinks.clear();
    int y = bounds.y;
    List<List<DrawData>> lines = buildLines(gc);

    for (int i = 0; i < lines.size(); i++) {
      if(justification == SWT.RIGHT) {
        y += drawRightJustified(gc, lines.get(i), y);
      } else if (justification == SWT.LEFT) {
        y += drawLeftJustified(gc, lines.get(i), y);
      } else if (justification == SWT.CENTER) {
        y += drawCenterJustified(gc, lines.get(i), y);
      }
    }

    if (drawBounds) {
      gc.setForeground(boundaryColor);
      gc.drawRectangle(bounds);
    }

    Rectangle calculatedBounds = new Rectangle(bounds.x, bounds.y, bounds.width, y - bounds.y);
    if (drawCalculatedBounds) {
      gc.setForeground(new Color(gc.getDevice(), 0, 255, 0));
      gc.drawRectangle(calculatedBounds);
    }

    gc.setClipping(clip);
    gc.setBackground(bg);

    return calculatedBounds;
  }

  int drawRightJustified(GC gc, List<DrawData> line, int y) {
    int maxY = 0;
    if (line.size() > 0) {
      int startIndex = line.size() - 1;
      DrawData drawData = line.get(startIndex);
      if(drawData.token.getType() == TextType.WHITESPACE && line.size() > 1) {
        startIndex--;
      }

      int x = bounds.width + bounds.x;
      for (int i = startIndex; i >= 0; i--) {
        drawData = line.get(i);
        configureForStyle(gc, drawData.token);
        gc.drawText(drawData.token.getText(), x - drawData.extent.x, y, true);
        addIfHyperlink(drawData, x - drawData.extent.x, y);
        x -= drawData.extent.x;
        if (drawData.extent.y > maxY) {
          maxY = drawData.extent.y;
        }
      }
    }
    return maxY;
  }

  int drawCenterJustified(GC gc, List<DrawData> line, int y) {
    int maxY = 0;

    if (line.size() > 0) {
      int width = 0;

      int startIndex = getStartIndex(line);
      int endIndex = getEndIndex(line);

      // determine width of line while dropping the leading and trailing whitespace
      for (int i = startIndex; i <= endIndex; i++) {
        DrawData drawData = line.get(i);
        width += drawData.extent.x;
      }

      int x = bounds.x + ((bounds.width - width) / 2);

      for (int i = startIndex; i <= endIndex; i++) {
        DrawData drawData = line.get(i);
        configureForStyle(gc, drawData.token);
        gc.drawText(drawData.token.getText(), x, y, true);
        addIfHyperlink(drawData, x, y);
        x += drawData.extent.x;
        if (drawData.extent.y > maxY) {
          maxY = drawData.extent.y;
        }
      }
    }

    return maxY;
  }

  int drawLeftJustified(GC gc, List<DrawData> line, int y) {
    int maxY = 0;

    if (line.size() > 0) {
      int startIndex = getStartIndex(line);
      int x = bounds.x;
      for (int i = startIndex; i < line.size(); i++) {
        DrawData drawData = line.get(i);
        configureForStyle(gc, drawData.token);
        gc.drawText(drawData.token.getText(), x, y, true);
        addIfHyperlink(drawData, x, y);
        x += drawData.extent.x;
        if (drawData.extent.y > maxY) {
          maxY = drawData.extent.y;
        }
      }
    }

    return maxY;
  }

  int getStartIndex(List<DrawData> line) {
    int startIndex = 0;
    for (int i = 0; i < line.size(); i++) {
      DrawData drawData = line.get(i);
      if (startIndex==0 && drawData.token.getType() != TextType.WHITESPACE) {
        startIndex = i;
        break;
      }
    }
    return startIndex;
  }

  int getEndIndex(List<DrawData> line) {
    int endIndex = 0;
    for (int i = line.size()-1; i >= 0; i--) {
      DrawData drawData = line.get(i);
      if (endIndex==0 && drawData.token.getType() != TextType.WHITESPACE) {
        endIndex = i;
        break;
      }
    }
    return endIndex;
  }
}
