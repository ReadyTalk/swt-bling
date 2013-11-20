package com.readytalk.swt.widgets.text;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
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

import com.readytalk.swt.widgets.text.tokenizer.PlainTextTokenizer;
import com.readytalk.swt.widgets.text.tokenizer.TextToken;
import com.readytalk.swt.widgets.text.tokenizer.TextToken.Type;
import com.readytalk.swt.widgets.text.tokenizer.TextTokenizer;

public class TextPaintEventHandler {
  
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

  public TextPaintEventHandler(final Composite parent) {
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
    textTokenizer = new PlainTextTokenizer();
    foregroundColor = parent.getForeground();
    hyperlinkColor = buildColor(100, 50, 200);
    boundaryColor = buildColor(255, 30, 30);
    FontData fontData = parent.getFont().getFontData()[0];
    setFont(fontData.getName(), fontData.getHeight());
    Point size = parent.getSize();
    bounds = new Rectangle(0,0, size.x, size.y);
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
    
    parent.addMouseListener(new MouseListener() {
      @Override
      public void mouseUp(MouseEvent e) {
        if (activeHyperlink != null) {
          notifyNavigationListeners(activeHyperlink);
        }
      }
      @Override public void mouseDown(MouseEvent e) {}
      @Override public void mouseDoubleClick(MouseEvent e) {}
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
  
  private Font buildFont(final String name, final int height, final int style) {
    return new Font(parent.getDisplay(), name, height, style);
  }
  
  private TextPaintEventHandler setFont(final String name, final int height) {
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
  
  public TextPaintEventHandler setBoundaryColor(final int r, final int g, final int b) {
    boundaryColor.dispose();
    boundaryColor = buildColor(r, g, b);
    return this;
  }
  
  public TextPaintEventHandler setForeground(final int r, final int g, final int b) {
    foregroundColor.dispose();
    foregroundColor = buildColor(r, g, b);
    return this;
  }
  
  public TextPaintEventHandler setHyperlinkColor(final int r, final int g, final int b) {
    hyperlinkColor.dispose();
    hyperlinkColor = buildColor(r, g, b);
    return this;
  }
  
  public TextPaintEventHandler setBounds (final Rectangle bounds) {
    this.bounds = bounds;
    return this;
  }
  
  public TextPaintEventHandler setClipping(final boolean clipping) {
    this.clipping = clipping;
    return this;
  }
  
  public TextPaintEventHandler setDrawBounds(boolean drawBounds) {
    this.drawBounds = drawBounds;
    return this;
  }
  
  void tokenizeText() {
    tokens = textTokenizer.tokenize(text);
  }
  
  public TextPaintEventHandler setText(final String text) {
    this.text = text;
    tokenizeText();
    return this;
  }
  
  public TextPaintEventHandler setTokenizer(final TextTokenizer textTokenizer){
    this.textTokenizer = textTokenizer;
    tokenizeText();
    return this;
  }
  
  public TextPaintEventHandler setWrapping(final boolean wrapping) {
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
  
  public TextPaintEventHandler addNavigationListener(final NavigationListener listener) {
    if (!navigationListeners.contains(listener)) {
      navigationListeners.add(listener);
    }
    return this;
  }

  private void notifyNavigationListeners(Hyperlink hyperlink) {
    NavigationEvent event = new NavigationEvent(hyperlink);
    for (int i = navigationListeners.size() - 1; i >= 0; i--) {
      navigationListeners.get(i).navigate(event);
    }
  }

  public void removeNavigationListener(final NavigationListener listener) {
    navigationListeners.remove(listener);
  }
  
  private void addHyperlink(TextToken token, Point start, Point end) {
    if (token.getType().equals(Type.LINK_AND_NAMED_URL) 
        || token.getType().equals(Type.LINK_URL)
        || token.getType().equals(Type.NAKED_URL)
        || token.getType().equals(Type.PLAIN_URL)) {
      hyperlinks.add(
          new Hyperlink(token, 
              new Rectangle(start.x, start.y, end.x,  end.y)));
    }
  }
  
  private void configureForStyle(GC gc, TextToken token) {
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
      configureForStyle(gc, token); 
      Point nextPosition = gc.textExtent(token.getText());
      
      int startX = drawPosition.x + nextPosition.x;
      if (wrapping && (drawPosition.x + nextPosition.x > bounds.width + bounds.x)) {
        drawPosition.y += nextPosition.y + 5;
        drawPosition.x = bounds.x;
      }
      addHyperlink(token, drawPosition, nextPosition);
      
      gc.drawText(token.getText(), drawPosition.x, drawPosition.y, true);
      drawPosition.x += nextPosition.x;
      
    }
    
    if (drawBounds) {
      gc.setForeground(boundaryColor);
      gc.drawRectangle(bounds);
    }
    
    gc.setClipping(clip);
    gc.setBackground(bg);
  }
}
