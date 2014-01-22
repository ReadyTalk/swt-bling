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

  /**
   * You can add any SquareButtons to a SquareButtonGroup.  If they are toggleable then the group will
   * link them together so they can act like radio buttons.  The first button in the list will be set on by default.
   *
   * @param buttons
   */
  public SquareButtonGroup(SquareButton ... buttons) {
    this.buttons = new ArrayList<SquareButton>();
    this.toggleableButtons = new ArrayList<SquareButton>();
    for(SquareButton button : buttons) {
       addButton(button);
    }
  }

  /**
   * Call this to explicitly pick which button is toggled in the group.
   *
   * @param button
   */
  public void setCurrentlyToggledButton(SquareButton button) {
    setCurrentlyToggledButton(button, true);
  }

  private void setCurrentlyToggledButton(SquareButton button, boolean simulateClick) {
    if(simulateClick) {
      if(isStrategyValid(button)) {
        executeStrategy(button);
      }
    } else {
      currentlyToggledButton = button;
      button.setToggled(true);
    }
  }

  public SquareButton getCurrentlyToggledButton() {
    return currentlyToggledButton;
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
        setCurrentlyToggledButton(button, false);
      }
      toggleable = true;
    }
    return toggleable;
  }

  void injectStrategy(final SquareButton button) {
    button.addStrategy(new SquareButton.ButtonClickStrategy() {
      @Override
      public boolean isStrategyValid() {
        return SquareButtonGroup.this.isStrategyValid(button);
      }

      @Override
      public void executeStrategy() {
        SquareButtonGroup.this.executeStrategy(button);
      }
    });
  }

  boolean isStrategyValid(SquareButton button) {
    return (currentlyToggledButton != button);
  }


  void executeStrategy(SquareButton button) {
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

}
