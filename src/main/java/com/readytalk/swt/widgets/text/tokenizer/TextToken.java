package com.readytalk.swt.widgets.text.tokenizer;

import java.net.URL;

public class TextToken {

	public enum Type {
		TEXT, PLAIN_URL, LINK_URL, LINK_AND_NAMED_URL, NAKED_URL, HEADER, ITALIC, BOLD, BOLD_AND_ITALIC
	}

	private Type type;
	private String text;
	private URL url;

	public TextToken(final Type type, final String text) {
		this.type = type;
		this.text = text;
	}

	public TextToken setType(final Type type) {
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
	
	public Type getType() {
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
