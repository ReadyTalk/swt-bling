package com.readytalk.swt.widgets.notifications;

/**
 * An enumeration to add metadata tags to <code>Bubbles</code> for use with the BubbleRegistry.<br/>
 * <br/>
 * For instance, you can tag all <code>Bubbles</code> that describe new features you want to expose your users to by
 * applying the <code>BubbleTag.NEW</code> tag to the <code>Bubble</code> and invoking
 * <code>BubbleRegistry.getInstance().showBubbleByTag(BubbleTag.NEW)</code>.<br/>
 * For more information, see the JavaDoc for <code>BubbleRegistry</code> or the BubbleExample.
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

  /**
   * Sets the text of the <code>FREE_FORM</code> BubbleTag.
   * @param text The custom tag you want associated with the tag.
   */
  public void setText(String text) {
    this.text = text;
  }
}
