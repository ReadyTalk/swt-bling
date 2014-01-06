package com.readytalk.swt.widgets.notifications;

import com.readytalk.swt.text.painter.TextPainter;
import com.readytalk.swt.text.tokenizer.TextTokenizerFactory;
import com.readytalk.swt.text.tokenizer.TextTokenizerType;
import com.readytalk.swt.widgets.CustomElementDataProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import java.util.logging.Logger;

/**
 * Instances of this class represent contextual information about a UI element.<br/>
 * <br/>
 * Bubble will attempt to always be visible on screen.<br/>
 * If the default Bubble would appear off-screen, we will calculate a suitable location to appear.<br/>
 * <br/>
 * Bubble utilizes the system font, and provides a constructor to indicate use of a bolded font.<br/>
 * <br/>
 * Bubble will also break up lines that would be longer than 400 pixels when drawn.<br/>
 * You can short-circuit this logic by providing your own line-breaks with <code>\n</code> characters in the text.<br/>
 * We will never format your text if your provide your own formatting.<br/>
 */
public class Bubble extends PopOverShell {
  private static final Logger LOG = Logger.getLogger(Bubble.class.getName());

  private static final RGB BORDER_COLOR = new RGB(204, 204, 204);
  private static final int BORDER_THICKNESS = 1; //pixels

  static final int MAX_STRING_LENGTH = 400; //pixels
  private static final RGB TEXT_COLOR = new RGB(204, 204, 204);
  private static final int TEXT_TOP_AND_BOTTOM_PADDING = 2; //pixels
  private static final int TEXT_LEFT_AND_RIGHT_PADDING = 5; //pixels

  private boolean disableAutoHide;

  private Listener listener;
  private String tooltipText;
  private Rectangle borderRectangle;
  private Point textSize;

  private Color borderColor;
  private Color textColor;
  private Font boldFont;

  private TextPainter textPainter;

  /**
   * Creates and attaches a bubble to a component that implements <code>CustomElementDataProvider</code>.
   * This is a convenience constructor which assumes you do not want the Bubble text to appear
   * in a Bold font.
   *
   * @param customElementDataProvider The CustomElementDataProvider element that the Bubble provides contextual help about
   * @param text The text you want to appear in the Bubble
   * @throws IllegalArgumentException Thrown if the parentControl or text is <code>null</code>
   */
  public static Bubble createBubbleForCustomWidget(CustomElementDataProvider customElementDataProvider, String text, BubbleTag ... tags)
          throws IllegalArgumentException {
    return new Bubble(customElementDataProvider.getPaintedElement(), customElementDataProvider, text, tags);
  }

  /**
   * Creates and attaches a Bubble to a component that implements <code>CustomElementDataProvider</code>
   *
   * @param customElementDataProvider The CustomElementDataProvider element that the Bubble provides contextual help about
   * @param text The text you want to appear in the Bubble
   * @param useBoldFont Whether or not we should draw the Bubble's text in a bold version of the system font
   * @throws IllegalArgumentException Thrown if the parentControl or text is <code>null</code>
   */
  public static Bubble createBubbleForCustomWidget(CustomElementDataProvider customElementDataProvider, String text, boolean useBoldFont, BubbleTag ... tags)
          throws IllegalArgumentException {
    return new Bubble(customElementDataProvider.getPaintedElement(), customElementDataProvider, text, useBoldFont, tags);
  }

  /**
   * Creates and attaches a Bubble to any SWT Control (or descendant).
   * This is a convenience constructor which assumes you do not want the Bubble text to appear
   * in a Bold font.
   *
   * @param parentControl The parent element that the Bubble provides contextual help about
   * @param text The text you want to appear in the Bubble
   * @throws IllegalArgumentException Thrown if the parentControl or text is <code>null</code>
   */
  public static Bubble createBubble(Control parentControl, String text, BubbleTag ... tags) {
    return new Bubble(parentControl, null, text, false, tags);
  }

  /**
   * Creates and attaches a Bubble to any SWT Control (or descendant).
   *
   * @param parentControl The parent element that the Bubble provides contextual help about
   * @param text The text you want to appear in the Bubble
   * @param useBoldFont Whether or not we should draw the Bubble's text in a bold version of the system font
   * @throws IllegalArgumentException Thrown if the parentControl or text is <code>null</code>
   */
  public static Bubble createBubble(Control parentControl, String text, boolean useBoldFont, BubbleTag ... tags)
          throws IllegalArgumentException {
    return new Bubble(parentControl, null, text, useBoldFont, tags);
  }

  private Bubble(Control parentControl, CustomElementDataProvider customElementDataProvider, String text, BubbleTag ... tags) {
    this(parentControl, customElementDataProvider, text, false, tags);
  }

  private Bubble(Control parentControl, CustomElementDataProvider customElementDataProvider, String text, boolean useBoldFont, BubbleTag ... tags)
          throws IllegalArgumentException {
    super(parentControl, customElementDataProvider);

    if (text == null) {
      throw new IllegalArgumentException("Bubble text cannot be null.");
    }

    this.tooltipText = maybeBreakLines(text);

    textPainter = new TextPainter(getPopOverShell())
            .setText(tooltipText)
            .setTextColor(TEXT_COLOR)
            .setTokenizer(TextTokenizerFactory.createTextTokenizer(TextTokenizerType.FORMATTED))
            .setPadding(TEXT_TOP_AND_BOTTOM_PADDING, TEXT_TOP_AND_BOTTOM_PADDING, TEXT_LEFT_AND_RIGHT_PADDING, TEXT_LEFT_AND_RIGHT_PADDING);

    // Remember to clean up after yourself onDispose.
    borderColor = new Color(getDisplay(), BORDER_COLOR);
    textColor = new Color(getDisplay(), TEXT_COLOR);
    if (useBoldFont) {
      Font font = getDisplay().getSystemFont();
      FontData fontData = font.getFontData()[0];
      boldFont = new Font(getDisplay(), fontData.getName(), fontData.getHeight(), SWT.BOLD);
    }

    attachListeners();
    registerBubble(getPoppedOverItem(), tags);
  }

  Point getAppropriatePopOverSize() {
    if (textSize == null) {
      textSize = getTextExtent(textPainter);
    }

    return textSize;
  }

  private void registerBubble(PoppedOverItem poppedOverItem, BubbleTag ... tags) {
    BubbleRegistry bubbleRegistry = BubbleRegistry.getInstance();

    if (poppedOverItem.getCustomElementDataProvider() != null) {
      bubbleRegistry.register(poppedOverItem.getCustomElementDataProvider(), this, tags);
    } else {
      bubbleRegistry.register(poppedOverItem.getControl(), this, tags);
    }
  }

  /**
   * Add tags to a previously created Bubble. <br/>
   * <br/>
   * Tags can be shown by calling <code>BubbleRegistry.getInstance.showBubblesByTag(BubbleTag tag)</code>
   * @param bubbleTags Tags to associate with this Bubble
   */
  public void addTags(BubbleTag ... bubbleTags) {
    BubbleRegistry.getInstance().addTags(getPoppedOverItem().getControlOrCustomElement(), bubbleTags);
  }

  /**
   * Remove tags from a previously created Bubble.
   * @param bubbleTags Tags to un-associate with this Bubble
   */
  public void removeTags(BubbleTag ... bubbleTags) {
    BubbleRegistry bubbleRegistry = BubbleRegistry.getInstance();
    BubbleRegistry.BubbleRegistrant registrant = bubbleRegistry.findRegistrant(getPoppedOverItem().getControlOrCustomElement());
    BubbleRegistry.getInstance().removeTags(registrant, bubbleTags);
  }

  /**
   * Remove the Bubble from the Registry.
   */
  public void deactivateBubble() {
    BubbleRegistry.getInstance().unregister(getPoppedOverItem().getControlOrCustomElement());
  }

  /**
   * Returns a boolean describing whether or not auto-hide functionality is disabled for this Bubble.
   * @return Whether or not auto-hide functionality is disabled
   */
  protected boolean isDisableAutoHide() {
    return disableAutoHide;
  }

  /**
   * Tells the Bubble whether or not it should auto-hide when the user mouses off the Bubble'd item.
   * @param disableAutoHide whether or not auto-hide should be disabled
   */
  protected void setDisableAutoHide(boolean disableAutoHide) {
    this.disableAutoHide = disableAutoHide;
  }

  private void attachListeners() {
    listener = new Listener() {
      public void handleEvent(Event event) {
        switch (event.type) {
          case SWT.Paint:
            onPaint(event);
            break;
          case SWT.MouseDown:
            onMouseDown(event);
            break;
          default:
            break;
        }
      }
    };
    popOverShell.addListener(SWT.Paint, listener);
    popOverShell.addListener(SWT.MouseDown, listener);

    addAccessibilityHooks(parentControl);
  }

  void resetWidget() {
    textSize = null;
    disableAutoHide = false;
  }

  void runBeforeShowPopOverShell() {
    if (textSize == null) {
      textSize = getTextExtent(textPainter);
    }

    borderRectangle = calculateBorderRectangle(textSize);
  }

  void widgetDispose() {
    deactivateBubble();
    borderColor.dispose();
    textColor.dispose();
    if (boldFont != null) {
      boldFont.dispose();
    }
    boldFont = null;
  }

  private void onPaint(Event event) {
    GC gc = event.gc;

    gc.setForeground(borderColor);
    gc.setLineWidth(BORDER_THICKNESS);
    gc.drawRectangle(borderRectangle);


    textPainter.handlePaint(event.gc);
//    if (boldFont != null) {
//      gc.setFont(boldFont);
//    }
//
//    gc.setForeground(textColor);
//    gc.drawText(tooltipText, TEXT_LEFT_AND_RIGHT_PADDING / 2, TEXT_TOP_AND_BOTTOM_PADDING / 2);
  }

  private void onMouseDown(Event event) {
    if(!isDisableAutoHide()) {
      hide();
    }
  }

  private void addAccessibilityHooks(Control parentControl) {
    parentControl.getAccessible().addAccessibleListener(new AccessibleAdapter() {
      public void getHelp(AccessibleEvent e) {
        e.result = tooltipText;
      }
    });
  }

  private Rectangle calculateBorderRectangle(Point textSize) {
    return new Rectangle(0, 0,
            textSize.x - BORDER_THICKNESS,
            textSize.y - BORDER_THICKNESS);
  }

  String maybeBreakLines(String rawString) {
    GC gc = new GC(getDisplay());
    if (boldFont != null) {
      gc.setFont(boldFont);
    }

    String returnString;
    Point textExtent = gc.textExtent(rawString, SWT.DRAW_DELIMITER);
    if (textExtent.x > MAX_STRING_LENGTH && !rawString.contains("\n")) {
      StringBuilder sb = new StringBuilder();
      String[] words = rawString.split(" ");
      int spaceInPixels = gc.textExtent(" ").x;

      int currentPixelCount = 0;
      for (String word : words) {
        int wordPixelWidth = gc.textExtent(word).x;
        if (currentPixelCount + wordPixelWidth + spaceInPixels < MAX_STRING_LENGTH) {
          sb.append(word);
          sb.append(" ");
          currentPixelCount += wordPixelWidth + spaceInPixels;
        } else {
          sb.append("\n");
          sb.append(word);
          sb.append(" ");
          currentPixelCount = wordPixelWidth + spaceInPixels;
        }
      }

      returnString = sb.toString();
    } else {
      returnString = rawString;
    }

    gc.dispose();
    return returnString;
  }

  private Point getTextExtent(TextPainter textPainter) {
    GC gc = new GC(getDisplay());
    Rectangle textExtent = textPainter.precomputeSize(gc);
    gc.dispose();

    return new Point(textExtent.width, textExtent.height);
  }
}