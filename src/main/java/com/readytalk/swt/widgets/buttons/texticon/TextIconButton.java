package com.readytalk.swt.widgets.buttons.texticon;

import com.readytalk.swt.color.ColorPalette;
import com.readytalk.swt.widgets.buttons.texticon.style.ButtonStyle;
import com.readytalk.swt.widgets.buttons.texticon.style.ButtonStyles;
import lombok.extern.java.Log;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A custom drawn button utilizing font icons for scalable button graphics
 */
@Log
public class TextIconButton extends Canvas {
  private final List<SelectionListener> selectionListeners = new LinkedList<SelectionListener>();
  private final ColorPalette colorPalette;
  private final ButtonRenderer buttonRenderer;

  private final List<TextIcon> textIcons = new LinkedList<TextIcon>();

  private ButtonStyles buttonStyles;
  private String text;
  private String accessibilityName;
  private int width = -1;
  private int height = -1;

  private ButtonStyle currentStyle;

  private boolean selected;
  private boolean hovered;
  private boolean clicked;

  public TextIconButton(Composite parent, int style, ColorPalette colorPalette, ButtonRenderer buttonRenderer,
      ButtonStyles buttonStyles) {
    super(parent, style | SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND);
    this.colorPalette = colorPalette;
    this.buttonRenderer = buttonRenderer;
    this.buttonStyles = buttonStyles;
    this.currentStyle = buttonStyles.getNormal();

    initializeBackground();
    addListeners();
  }

  private void initializeBackground() {
    setBackgroundMode(SWT.INHERIT_DEFAULT);

    if (getParent().getBackground() != null) {
      setBackground(getParent().getBackground());
    }
  }

  private void addListeners() {
    addPaintListener(new PaintListener() {
      @Override
      public void paintControl(PaintEvent event) {
        handlePaintEvent(event);
      }
    });

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseDown(MouseEvent event) {
        if (event.button == 1) {
          handleButtonDown();
        }
      }

      @Override
      public void mouseUp(MouseEvent event) {
        if (event.button == 1) {
          handleButtonUp();
        }
      }
    });

    addMouseTrackListener(new MouseTrackAdapter() {
      @Override
      public void mouseEnter(MouseEvent mouseEvent) {
        hovered = true;
        redraw();
        update();
      }

      @Override
      public void mouseExit(MouseEvent mouseEvent) {
        hovered = false;
        redraw();
        update();
      }
    });

    // Add a mouse listener on the parent as well to partially work around an SWT bug where mouse exit events don't
    // fire. See https://bugs.eclipse.org/bugs/show_bug.cgi?id=302956
    getParent().addMouseTrackListener(new MouseTrackAdapter() {
      @Override
      public void mouseEnter(MouseEvent mouseEvent) {
        hovered = false;
        redraw();
        update();
      }

      @Override
      public void mouseExit(MouseEvent mouseEvent) {
        hovered = false;
        redraw();
        update();
      }
    });

    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent event) {
        if (event.keyCode == SWT.TAB) {
          if (event.stateMask == SWT.SHIFT) {
            traverse(SWT.TRAVERSE_TAB_PREVIOUS);
          } else {
            traverse(SWT.TRAVERSE_TAB_NEXT);
          }
        } else {
          switch (event.character) {
          case ' ':
          case '\r':
          case '\n':
            handleButtonUp();
            break;
          default:
            break;
          }
        }
      }
    });

    getAccessible().addAccessibleListener(new AccessibleAdapter() {
      @Override
      public void getName(AccessibleEvent e) {
        e.result = accessibilityName;
      }
    });

    getAccessible().addAccessibleListener(new AccessibleAdapter() {
      @Override
      public void getDescription(AccessibleEvent event) {
        event.result = "enabled:" + TextIconButton.this.isEnabled();
      }
    });
  }

  protected void fireSelectionEvent() {
    final Event event = new Event();
    event.widget = this;
    event.display = getDisplay();
    event.type = SWT.Selection;

    for (final SelectionListener selectionListener : selectionListeners) {
      selectionListener.widgetSelected(new SelectionEvent(event));
    }
  }

  protected void handlePaintEvent(PaintEvent event) {
    if (!isEnabled()) {
      currentStyle = buttonStyles.getDisabled();
      buttonRenderer.drawDisabled(event.gc, this);
    } else if (clicked) {
      currentStyle = buttonStyles.getClicked();
      buttonRenderer.drawClicked(event.gc, this);
    } else if (isToggleButton() && selected) {
      if (hovered) {
        currentStyle = buttonStyles.getSelectedHovered();
        buttonRenderer.drawSelectedHovered(event.gc, this);
      } else {
        currentStyle = buttonStyles.getSelected();
        buttonRenderer.drawSelected(event.gc, this);
      }
    } else if (hovered) {
      currentStyle = buttonStyles.getHover();
      buttonRenderer.drawHovered(event.gc, this);
    } else {
      currentStyle = buttonStyles.getNormal();
      buttonRenderer.drawNormal(event.gc, this);
    }
  }

  protected void handleButtonDown() {
    if (isEnabled()) {
      clicked = true;
      redraw();
      update();
    }
  }

  protected void handleButtonUp() {
    if (isEnabled()) {
      clicked = false;
      selected = isToggleButton() ? !selected : false;
      redraw();
      update();
      fireSelectionEvent();
    }
  }

  protected boolean isToggleButton() {
    return (getStyle() & SWT.TOGGLE) == SWT.TOGGLE;
  }

  /**
   * Returns the ColorPalette for this TextIconButton
   *
   * @return an instance of ColorPalette used to obtain the colors for the button by the ButtonRenderer
   */
  public ColorPalette getColorPalette() {
    return colorPalette;
  }

  /**
   * Adds the listener to the collection of listeners who will be notified when the control is selected by the user, by sending it one of the messages defined in the <code>SelectionListener</code> interface.
   * <p>
   * <code>widgetSelected</code> is called when the control is selected by the user. <code>widgetDefaultSelected</code> is not called.
   * </p>
   *
   * @param listener the listener which should be notified
   * @throws IllegalArgumentException <ul>
   *                                  <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   *                                  </ul>
   * @throws SWTException             <ul>
   *                                  <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *                                  <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *                                  </ul>
   * @see SelectionListener
   * @see #removeSelectionListener
   * @see SelectionEvent
   */
  public void addSelectionListener(SelectionListener listener) {
    checkWidget();

    if (listener == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }

    selectionListeners.add(listener);
  }

  /**
   * @see org.eclipse.swt.widgets.Composite#computeSize(int, int, boolean)
   */
  @Override
  public Point computeSize(int wHint, int hHint, boolean changed) {
    checkWidget();

    final Point computedSize = buttonRenderer.computeSize(this);

    if (wHint != SWT.DEFAULT) {
      computedSize.x = wHint;
    }

    if (hHint != SWT.DEFAULT) {
      computedSize.y = hHint;
    }

    return computedSize;
  }

  /**
   * @return the associated button renderer
   */
  public ButtonRenderer getButtonRenderer() {
    checkWidget();
    return buttonRenderer;
  }

  /**
   * Returns the whole height of the widget.
   *
   * @return the receiver's height
   * @throws SWTException <ul>
   *                      <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *                      <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *                      </ul>
   */
  public int getHeight() {
    checkWidget();

    if (height == -1) {
      return computeSize(SWT.DEFAULT, SWT.DEFAULT, false).y;
    }

    return height;
  }

  /**
   * Returns <code>true</code> if the receiver is selected, and false otherwise.
   *
   * @return the selection state
   * @throws SWTException <ul>
   *                      <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *                      <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *                      </ul>
   */
  public boolean getSelection() {
    checkWidget();
    return this.selected;
  }

  /**
   * Returns the receiver's text, which will be an empty string if it has never been set or if the receiver is an <code>ARROW</code> button.
   *
   * @return the receiver's text
   * @throws SWTException <ul>
   *                      <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *                      <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *                      </ul>
   */
  public String getText() {
    checkWidget();
    return text;
  }

  /**
   * Returns the List of TextIcons used as a scalable font icon using a library such as FontAwesome.
   *
   * @return
   */
  public List<TextIcon> getTextIcons() {
    checkWidget();
    return textIcons;
  }

  /**
   * Returns the whole width of the widget.
   *
   * @return the receiver's height
   * @throws SWTException <ul>
   *                      <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *                      <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *                      </ul>
   */
  public int getWidth() {
    checkWidget();

    if (width == -1) {
      return computeSize(SWT.DEFAULT, SWT.DEFAULT, false).x;
    }

    return width;
  }

  /**
   * Returns the ButtonStyles
   *
   * @return
   */
  public ButtonStyles getButtonStyles() {
    return buttonStyles;
  }

  /**
   * Returns the current style based on the state of the button (eg. disabled, hovered, etc.)
   *
   * @return
   */
  public ButtonStyle getCurrentStyle() {
    return currentStyle;
  }

  /**
   * Removes the listener from the collection of listeners who will be notified when the control is selected by the user.
   *
   * @param listener the listener which should no longer be notified
   * @throws IllegalArgumentException <ul>
   *                                  <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   *                                  </ul>
   * @throws SWTException             <ul>
   *                                  <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *                                  <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *                                  </ul>
   * @see SelectionListener
   * @see #addSelectionListener
   */
  public void removeSelectionListener(SelectionListener listener) {
    checkWidget();

    if (listener == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }

    this.selectionListeners.remove(listener);
  }

  /**
   * Sets the height of the receiver.
   * <p>
   * Note: Attempting to set the width or height of the receiver to a negative number will cause that value to be set to zero instead.
   * </p>
   *
   * @param height the new width
   * @throws SWTException <ul>
   *                      <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *                      <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *                      </ul>
   */
  public void setHeight(int height) {
    checkWidget();
    this.height = Math.max(height, 0);
    redraw();
    update();
  }

  /**
   * Sets the selection state of the receiver, if it is of type <code>TOGGLE</code>.
   *
   * @param selected the new selection state
   * @throws SWTException <ul>
   *                      <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *                      <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *                      </ul>
   */
  public void setSelection(boolean selected) {
    checkWidget();
    this.selected = selected;
    redraw();
    update();
  }

  /**
   * Sets the receiver's text.
   *
   * @param text the new text
   * @throws IllegalArgumentException <ul>
   *                                  <li>ERROR_NULL_ARGUMENT - if the text is null</li>
   *                                  </ul>
   * @throws SWTException             <ul>
   *                                  <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *                                  <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *                                  </ul>
   */
  public void setText(String text) {
    checkWidget();
    this.text = text;
    redraw();
    update();
  }

  /**
   * Sets the text that will be used as a scalable font icon using a library such as FontAwesome.
   *
   * @param textIcons
   */
  public void setTextIcons(TextIcon... textIcons) {
    checkWidget();
    this.textIcons.clear();
    this.textIcons.addAll(Arrays.asList(textIcons));
    redraw();
    update();
  }

  /**
   * Sets the ButtonStyles used by the ButtonRenderer to paint the button
   *
   * @param buttonStyles
   */
  public void setButtonStyles(ButtonStyles buttonStyles) {
    checkWidget();
    this.buttonStyles = buttonStyles;
    redraw();
    update();
  }

  /**
   * Set a name for the button that will be visible by screen readers and automation.
   *
   * @param accessibilityName
   */
  public void setAccessibilityName(String accessibilityName) {
    checkWidget();
    this.accessibilityName = accessibilityName;
  }

  /**
   * Sets the width of the receiver.
   * <p>
   * Note: Attempting to set the width or height of the receiver to a negative number will cause that value to be set to zero instead.
   * </p>
   *
   * @param width the new width
   * @throws SWTException <ul>
   *                      <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *                      <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *                      </ul>
   */
  public void setWidth(int width) {
    checkWidget();
    this.width = Math.max(0, width);
    redraw();
    update();
  }

  /**
   * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
   */
  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    redraw();
    update();
  }

  /**
   * @see org.eclipse.swt.widgets.Control#setSize(int, int)
   */
  @Override
  public void setSize(int width, int height) {
    super.setSize(width, height);
    redraw();
    update();
  }

  /**
   * @see org.eclipse.swt.widgets.Control#setSize(org.eclipse.swt.graphics.Point)
   */
  @Override
  public void setSize(Point size) {
    super.setSize(size);
    redraw();
    update();
  }

  /**
   * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
   */
  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
    redraw();
    update();
  }
}
