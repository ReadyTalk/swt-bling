package com.readytalk.swt.widgets.notifications;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 */
public class BubbleRegistry {

  private static final Logger log = Logger.getLogger(BubbleRegistry.class.getName());

  final Map<String, List<BubbleRegistrant>> tagMap;
  final List<BubbleRegistrant> registrants;

  static final BubbleRegistry instance = new BubbleRegistry();

  BubbleRegistry() {
    tagMap = new HashMap<String, List<BubbleRegistrant>>();
    registrants = new ArrayList<BubbleRegistrant>();
  }

  public static BubbleRegistry getInstance() {
    return instance;
  }

  public void register(Widget widget, Bubble bubble, BubbleTag ... tags) {
    BubbleRegistrant registrant = findRegistrant(widget);

    if(registrant == null) {
      registrant = new WidgetBubbleRegistrant(widget, bubble, tags);
      registrant.addMouseListener();
      registrants.add(registrant);
    }

    addTags(widget, tags);
  }


  public void addTags(Object target, BubbleTag ... tags) {
    BubbleRegistrant registrant = findRegistrant(target);
    if(registrant == null) {
      log.warning("Instance of " + target.getClass() + " has not been registered.");
    } else {
      registrant.addTags(tags);
      for(BubbleTag tag : registrant.getTags()) {
        List<BubbleRegistrant> tagList = getTagList(tag);
        if(!tagList.contains(registrant)) {
          tagList.add(registrant);
        }
      }
    }
  }

  public void removeTags(BubbleRegistrant registrant, BubbleTag ... tagsToBeRemoved) {
    registrant.removeTags(tagsToBeRemoved);
    for(BubbleTag tag : tagsToBeRemoved) {
      List<BubbleRegistrant> tagList = getTagList(tag);
      if(tagList.contains(registrant)) {
        tagList.remove(registrant);
      }
    }
  }

  /**
   * Show all bubbles corresponding to list of tags.
   *
   * @param tags
   */
  public void showBubblesByTags(BubbleTag ... tags) {
    for(BubbleTag tag : tags) {
      List<BubbleRegistrant> registrants = getTagList(tag);
      for(BubbleRegistrant registrant : registrants) {
        registrant.bubble.setDisableAutoHide(true);
        registrant.showBubble();
      }
    }
  }

  public void showAllBubbles() {
    for(BubbleRegistrant registrant : registrants) {
      registrant.bubble.setDisableAutoHide(true);
      registrant.showBubble();
    }
  }


  /**
   * Dismiss all bubbles corresponding to the list of tags.
   *
   * @param tags
   */
  public void dismissBubblesByTag(BubbleTag ... tags) {
    for(BubbleTag tag : tags) {
      List<BubbleRegistrant> registrants = getTagList(tag);
      for(BubbleRegistrant registrant : registrants) {
        registrant.dismissBubble();
        registrant.bubble.setDisableAutoHide(false);
      }
    }
  }

  public void dismissAllBubbles() {
    for(BubbleRegistrant registrant : registrants) {
      registrant.dismissBubble();
      registrant.bubble.setDisableAutoHide(false);
    }
  }

  BubbleRegistrant findRegistrant(Object target) {
    for(BubbleRegistrant br : registrants) {
      if(br.getTarget() == target) {
        return br;
      }
    }
    return null;
  }

  List<BubbleRegistrant> getTagList(BubbleTag tag) {
    List<BubbleRegistrant> tagList = tagMap.get(tag.getText());
    if(tagList == null) {
      tagList = new ArrayList<BubbleRegistrant>();
      tagMap.put(tag.getText(), tagList);
    }
    return tagList;
  }

  public void unregister(Object target) {
    BubbleRegistrant registrant = findRegistrant(target);
    if(registrant != null) {
      removeTags(registrant,
        registrant.getTags().toArray(new BubbleTag[registrant.getTags().size()]));
      registrant.removeMouseListener();
      registrants.remove(registrant);
    }
  }

  static abstract class BubbleRegistrant {
    List<BubbleTag> tags;
    Bubble bubble;

    BubbleRegistrant(Bubble bubble, BubbleTag ... tags) {
      this.bubble = bubble;
      addTags(tags);

    }

    void addTags(BubbleTag ... tags) {
      if(this.tags == null) {
        this.tags = new ArrayList<BubbleTag>();
      }
      for(BubbleTag tag : tags) {
        this.tags.add(tag);
      }
    }

    void removeTags(BubbleTag ... tags) {
      if(this.tags != null) {
        for(BubbleTag tag : tags) {
          this.tags.remove(tag);
        }
      }
    }

    List<BubbleTag> getTags() {
      return tags;
    }

    abstract Object getTarget();
    abstract void addMouseListener();
    abstract void removeMouseListener();
    void showBubble() {
      bubble.show();             // i may pull these out again
    }

    void dismissBubble() {
      bubble.fadeOut();
    }
  }

  static class WidgetBubbleRegistrant extends BubbleRegistrant {
    final Widget widget;
    Listener mouseHoverListener;
    Listener mouseOutListener;

    WidgetBubbleRegistrant(Widget widget, Bubble bubble, BubbleTag ... tags) {
      super(bubble, tags);
      this.widget = widget;
    }

    Object getTarget() {
      return widget;
    }

    public void addMouseListener() {
      if (mouseHoverListener == null) {
        mouseHoverListener = new Listener() {
          public void handleEvent(Event event) {
            bubble.show();
          }
        };
      }
      if (mouseOutListener == null) {
        mouseOutListener = new Listener() {
          public void handleEvent(Event event) {
            if(bubble.isDisableAutoHide() != true) {
              bubble.fadeOut();
            }
          }
        };
      }
      widget.addListener(SWT.MouseHover, mouseHoverListener);
      widget.addListener(SWT.MouseExit, mouseOutListener);
    }

    public void removeMouseListener() {
      widget.removeListener(SWT.MouseHover, mouseHoverListener);
      widget.removeListener(SWT.MouseExit, mouseOutListener);
    }
  }
}
