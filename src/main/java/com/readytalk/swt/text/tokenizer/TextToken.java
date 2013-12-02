package com.readytalk.swt.text.tokenizer;

import java.net.URL;

import com.readytalk.swt.text.painter.TextType;

/**
 * TextToken represents a section of contiguous text that shares a common 
 * styling as specified by the TextType contained within. 
 */
public class TextToken {
  
  private TextType type;
  private String text;
  private URL url;

  /**
   * Creates a new TextToken of the given type to describe the given text.
   */
  public TextToken(final TextType type, final String text) {
    this.type = type;
    this.text = text;
  }

  /**
   * Sets the TextType of this TextToken.
   */
  public TextToken setType(final TextType type) {
    this.type = type;
    return this;
  }

  /**
   * Sets the text of this TextToken.
   */
  public TextToken setText(final String text) {
    this.text = text;
    return this;
  }
  
  /**
   * Set the URL that this token represents, if any.
   */
  public TextToken setUrl(final URL url) {
    this.url = url;
    return this;
  }
	
  public TextType getType() {
    return type;
  }
	
  public String getText() {
    return text;
  }
	
  public URL getURL() {
    return url;
  } 

  public String toString() {
    return this.type + " :: " + this.text
         + (url != null ? " {url -> " + url + "}" : "");
  }
}
