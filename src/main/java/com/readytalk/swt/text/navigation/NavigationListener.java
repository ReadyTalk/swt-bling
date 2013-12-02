package com.readytalk.swt.text.navigation;

/**
 * NavigationListener is an interface to be implemented by objects
 * wanting to be notified of NavigationEvents.
 */
public interface NavigationListener {

  /**
   * Called when a navigation event occurs.
   */
  void navigate(NavigationEvent event);
}
