package com.readytalk.swt.text.navigation;

import java.net.URL;

/**
 * NavigationEvent is passed to a NavigationListener when the navigate
 * call is triggered (via a mouseup event in TextPainter falling within 
 * a Hyperlink boundary).  Its sole purpose is to notify the listener
 * of which URL was clicked enabling the listener to respond.  
 */
public class NavigationEvent {
	
  Hyperlink hyperlink;
	
  /**
   * Creates a NavigationEvent containing the given Hyperlink.
   */
  public NavigationEvent(Hyperlink hyperlink) {
    this.hyperlink = hyperlink;
  }
	
  /**
   * Gets the URL from the internal Hyperlink.
   * @return URL 
   */
  public URL getUrl() {
	return hyperlink.getURL();
  }
}
