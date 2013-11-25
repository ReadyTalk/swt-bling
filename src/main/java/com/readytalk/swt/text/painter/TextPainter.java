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
  private boolean clipping;
  private boolean drawBounds;
  private List<Hyperlink> hyperlinks;
  private List<NavigationListener> navigationListeners;
  private Composite parent;
  private String text;
  private TextTokenizer textTokenizer;
  private List<TextToken> tokens;
  private boolean wrapping;

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
  
  public TextPainter setBoundaryColor(final int r, final int g, final int b) {
    boundaryColor.dispose();
    boundaryColor = buildColor(r, g, b);
    return this;
  }
  
  public TextPainter setForeground(final int r, final int g, final int b) {
    foregroundColor.dispose();
    foregroundColor = buildColor(r, g, b);
    return this;
  }
  
  public TextPainter setHyperlinkColor(final int r, final int g, final int b) {
    hyperlinkColor.dispose();
    hyperlinkColor = buildColor(r, g, b);
    return this;
  }
  
  public TextPainter setBounds (final Rectangle bounds) {
    this.bounds = bounds;
    return this;
  }
  
  public TextPainter setClipping(final boolean clipping) {
    this.clipping = clipping;
    return this;
  }
  
  public TextPainter setDrawBounds(boolean drawBounds) {
    this.drawBounds = drawBounds;
    return this;
  }
  
  void tokenizeText() {
    tokens = textTokenizer.tokenize(text);
  }
  
  public TextPainter setText(final String text) {
    this.text = text;
    tokenizeText();
    return this;
  }
  
  public TextPainter setTokenizer(final TextTokenizer textTokenizer){
    this.textTokenizer = textTokenizer;
    tokenizeText();
    return this;
  }
  
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

  public void removeNavigationListener(final NavigationListener listener) {
    navigationListeners.remove(listener);
  }
  
  void addHyperlink(TextToken token, Point start, Point end) {
    if (token.getType().equals(TextType.LINK_AND_NAMED_URL) 
        || token.getType().equals(TextType.LINK_URL)
        || token.getType().equals(TextType.NAKED_URL)
        || token.getType().equals(TextType.PLAIN_URL)) {
      hyperlinks.add(
          new Hyperlink(token, 
              new Rectangle(start.x, start.y, end.x,  end.y)));
    }
  }
  
  void configureForStyle(GC gc, TextToken token) {
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
    default:
      gc.setForeground(foregroundColor);
      break;
    }
  }

  public void handlePaint(final PaintEvent e) {
    final GC gc = e.gc;
    final Rectangle clip = gc.getClipping();
    final Color bg = gc.getBackground();
    
    if (clipping) {
      gc.setClipping(this.bounds);
    }
    
    hyperlinks.clear();
    Point drawPosition = new Point(bounds.x, bounds.y);
    
    for (TextToken token:tokens) {
      boolean drawWhitespace = true;
      configureForStyle(gc, token); 
      Point nextPosition = gc.textExtent(token.getText());
      
      if (wrapping && (drawPosition.x + nextPosition.x > bounds.width + bounds.x)) {
        drawPosition.y += nextPosition.y + 5;
        drawPosition.x = bounds.x;
        drawWhitespace = false;
      }
      
      if (token.getType() != TextType.WHITESPACE) {
        addHyperlink(token, drawPosition, nextPosition);
        gc.drawText(token.getText(), drawPosition.x, drawPosition.y, true);
        drawPosition.x += nextPosition.x;
      } else if (drawWhitespace) {
        gc.drawText(token.getText(), drawPosition.x, drawPosition.y, true);
        drawPosition.x += nextPosition.x;
      }
    }
    
    if (drawBounds) {
      gc.setForeground(boundaryColor);
      gc.drawRectangle(bounds);
    }
    
    gc.setClipping(clip);
    gc.setBackground(bg);
  }
}
