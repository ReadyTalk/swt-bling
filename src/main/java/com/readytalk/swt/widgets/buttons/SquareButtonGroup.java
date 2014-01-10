package com.readytalk.swt.widgets.buttons;

import java.util.ArrayList;
import java.util.List;

/**
 * Currently this is primarily used for square buttons used like a toggle.  It does nothing for layouts, but acts as a point of control.
 */
public class SquareButtonGroup {
  private List<SquareButton> buttons;
  private List<SquareButton> toggleableButtons;
  private SquareButton currentlyToggledButton;

  public SquareButtonGroup(SquareButton ... buttons) {
    this.buttons = new ArrayList<SquareButton>();
    this.toggleableButtons = new ArrayList<SquareButton>();
    for(SquareButton button : buttons) {
       addButton(button);
    }
  }

  /**
   * Internal method, will return true if the button is toggleable.
   *
   * @param button
   * @return
   */
  boolean addButton(SquareButton button) {
    boolean toggleable = false;
    buttons.add(button);
    if(button.isToggleable()) {
      button.setDisableDefaultToggleClickHandler(true);
      toggleableButtons.add(button);
      injectStrategy(button);
      if(currentlyToggledButton == null) {
        currentlyToggledButton = button;
        button.setToggled(true);
      }
      toggleable = true;
    }
    return toggleable;
  }

  void injectStrategy(final SquareButton button) {
      button.addStrategy(new SquareButton.ButtonClickStrategy() {
        @Override
        public boolean isStrategyValid() {
          boolean test = (currentlyToggledButton != button);
          return test;
        }

        @Override
        public void executeStrategy() {
          button.setToggled(true);
          currentlyToggledButton = button;
          for(SquareButton otherButton : toggleableButtons) {
            if(currentlyToggledButton == otherButton) {
              continue;
            }
            if(otherButton.isToggleable()) {
              otherButton.setToggled(false);
            }
          }
        }
      });
  }

}
