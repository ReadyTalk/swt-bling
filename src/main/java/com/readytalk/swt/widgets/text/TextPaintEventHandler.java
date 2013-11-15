package com.readytalk.swt.widgets.text;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
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
import com.readytalk.swt.widgets.text.tokenizer.TextTokenizer;

public class TextPaintEventHandler {
  
  private Color boundaryColor;
  private Rectangle bounds;
  private boolean clipping;
  private boolean drawBounds;
  private Font font;
  private Font italicFont;
  private Font boldFont;
  private Font boldAndUnderlineFont;
  private Font underlineFont;
  private Font headerFont;
  private Color foregroundColor;
  private Color hyperlinkColor;
  private List<NavigationListener> navigationListeners;
  private Composite parent;
  private String text;
  private TextTokenizer textTokenizer;
  private List<TextToken> tokens;
  private boolean wrapping;
  

  public TextPaintEventHandler(final Composite parent) {
    this.parent = parent;
    this.text = "";
    this.clipping = true;
    this.wrapping = true;
    this.textTokenizer = new PlainTextTokenizer();
    this.foregroundColor = parent.getForeground();
    this.hyperlinkColor = parent.getForeground();
    this.boundaryColor = new Color(foregroundColor.getDevice(), 255, 30, 30);
    
    FontData[] fontData = parent.getFont().getFontData();
    System.out.println(parent.getFont().getFontData()[0].getName());
    this.font = new Font(parent.getFont().getDevice(), fontData);
    this.boldFont = new Font(parent.getFont().getDevice(), parent.getFont().getFontData());
    this.italicFont = new Font(parent.getFont().getDevice(), parent.getFont().getFontData());
    
    Point size = parent.getSize();
    this.bounds = new Rectangle(0,0, size.x/2, size.y);
    navigationListeners = new ArrayList<NavigationListener>();
    
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
  }
  
  public void dispose() {
    foregroundColor.dispose();
    hyperlinkColor.dispose();
    font.dispose();
    boldFont.dispose();
    underlineFont.dispose();
  }
  
  public TextPaintEventHandler setBoundaryColor(int r, int g, int b) {
    this.boundaryColor = boundaryColor;
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
  
  public TextPaintEventHandler setForeground(final Color foregroundColor) {
    this.foregroundColor = foregroundColor;
    return this;
  }
  
  public TextPaintEventHandler setHyperlinkColor(final Color hyperlinkColor) {
    this.hyperlinkColor = hyperlinkColor;
    return this;
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
  
  void tokenizeText() {
    tokens = textTokenizer.tokenize(text);
  }
  
  public TextPaintEventHandler setWrapping(final boolean wrapping) {
    this.wrapping = wrapping;
    return this;
  }
  
  public TextPaintEventHandler addNavigationListener(final NavigationListener listener) {
    if (!navigationListeners.contains(listener)) {
      navigationListeners.add(listener);
    }
    return this;
  }

  private void notifyNavigationListeners() {
    NavigationEvent event = new NavigationEvent() {};
    for (int i = navigationListeners.size() - 1; i >= 0; i--) {
      navigationListeners.get(i).navigate(event);
    }
  }

  public void removeNavigationListener(final NavigationListener listener) {
    navigationListeners.remove(listener);
  }
  
  private void loadStyle(GC gc, TextToken token) {
    switch(token.getType()) {
    case BOLD:
      gc.setFont(boldFont);
      break;
    case BOLD_AND_ITALIC:
      gc.setFont(italicFont);
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
      gc.setFont(font);
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
    
    Point drawPosition = new Point(bounds.x, bounds.y);
    
    for (TextToken token:tokens) {
      loadStyle(gc, token); 
      Point nextPosition = gc.textExtent(token.getText());
      if (wrapping && (drawPosition.y + nextPosition.y > bounds.width + bounds.y)) {
        drawPosition.y += nextPosition.y + 5;
        drawPosition.x = bounds.x;
      }
      
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
