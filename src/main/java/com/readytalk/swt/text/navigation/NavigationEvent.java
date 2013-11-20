package com.readytalk.swt.text.navigation;

import java.net.URL;

public class NavigationEvent {
	
  Hyperlink hyperlink;
	
	public NavigationEvent(Hyperlink hyperlink) {
    this.hyperlink = hyperlink;
  }
	
	public URL getUrl() {
	  return hyperlink.getURL();
	}
}
