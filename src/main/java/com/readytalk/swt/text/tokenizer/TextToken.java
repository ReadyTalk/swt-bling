package com.readytalk.swt.text.tokenizer;

import java.net.URL;

import com.readytalk.swt.text.painter.TextType;

public class TextToken {
  
  private TextType type;
	private String text;
	private URL url;

	public TextToken(final TextType type, final String text) {
		this.type = type;
		this.text = text;
	}

	public TextToken setType(final TextType type) {
		this.type = type;
		return this;
	}

	public TextToken setText(final String text) {
		this.text = text;
		return this;
	}

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
