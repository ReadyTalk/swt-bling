package com.readytalk.swt.widgets.text;

import java.net.URL;

import org.eclipse.swt.graphics.Rectangle;

import com.readytalk.swt.widgets.text.tokenizer.TextToken;

public class Hyperlink {
	private TextToken token;
	private Rectangle bounds;
	
	Hyperlink(final TextToken token, final Rectangle bounds) {
		this.token = token;
		this.bounds = bounds;
	}
	
	public URL getURL() {
		return token.getURL();
	}
	
	public boolean contains(final int x, final int y) {
		return bounds.contains(x, y);
	}
}
