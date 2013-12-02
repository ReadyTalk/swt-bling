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
   * 
   * @param {@link TextType}
   * @param {@link String}
   */
  public TextToken(final TextType type, final String text) {
    this.type = type;
    this.text = text;
  }

  /**
   * Sets the TextType of this TextToken.
   * 
   * @param {@link TextType}
   * @return {@link TextToken}
   */
  public TextToken setType(final TextType type) {
    this.type = type;
    return this;
  }

  /**
   * Sets the text of this TextToken.
   * 
   * @param {@link String}
   * @return {@link TextToken}
   */
  public TextToken setText(final String text) {
    this.text = text;
    return this;
  }
  
  /**
   * Set the URL that this token represents, if any.
   * 
   * @param {@link URL}
   * @return {@link TextToken}
   */
  public TextToken setUrl(final URL url) {
    this.url = url;
    return this;
  }
	
  /**
   * Returns the TextType of this token.
   * @return {@link TextType}}
   */
  public TextType getType() {
    return type;
  }
	
  /**
   * Returns the String this token represents.
   * @return {@link String}
   */
  public String getText() {
    return text;
  }
	
  /**
   * Returns the URL this token represents, if any.
   * @return {@link URL}
   */
  public URL getURL() {
    return url;
  } 

  @Override
  public String toString() {
    return this.type + " :: " + this.text
         + (url != null ? " {url -> " + url + "}" : "");
  }
}
