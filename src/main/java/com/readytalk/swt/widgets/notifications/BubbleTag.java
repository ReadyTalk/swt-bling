package com.readytalk.swt.widgets.notifications;

/**
 */
public enum BubbleTag {
  NEW, FREE_FORM;

  private String text;

  private BubbleTag() {
    this.text = this.name();
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
