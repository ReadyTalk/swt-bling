package com.readytalk.swt.widgets.notifications;

import org.eclipse.swt.widgets.Composite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 */
public class BubbleRegistry {

  private static final Logger log = Logger.getLogger(BubbleRegistry.class.getName());

  private Map<String, List<BubbleRegistrant>> tagMap;
  private List<BubbleRegistrant> registrants;

  static final BubbleRegistry instance = new BubbleRegistry();

  BubbleRegistry() {
    tagMap = new HashMap<String, List<BubbleRegistrant>>();
    registrants = new ArrayList<BubbleRegistrant>();
  }

  public static BubbleRegistry getInstance() {
    return instance;
  }

  public void register(Composite composite, Bubble bubble,BubbleTag ... tags) {
    BubbleRegistrant registrant = findRegistrant(composite);

    if(registrant == null) {
      registrant = new CompositeBubbleRegistrant(composite, bubble, tags);
      registrants.add(registrant);
    }

    updateTags(registrant);
  }

  /**
   * In the event that a target attempts to register twice or needs to add more
   * tags, this method is called.
   * @param registrant
   */
  void updateTags(BubbleRegistrant registrant) {
    updateTags(registrant, null);
  }

  void updateTags(BubbleRegistrant registrant, BubbleTag ... tagsToBeRemoved) {
    for(BubbleTag tag : registrant.getTags()) {
      List<BubbleRegistrant> tagList = getTagList(tag);
      if(!tagList.contains(registrant)) {
        tagList.add(registrant);
      }
    }
    if(tagsToBeRemoved != null) {
      registrant.removeTags(tagsToBeRemoved);
      for(BubbleTag tag : tagsToBeRemoved) {
        List<BubbleRegistrant> tagList = getTagList(tag);
        if(tagList.contains(registrant)) {
          tagList.remove(registrant);
        }
      }
    }
  }

  public void addTags(Object target, BubbleTag ... tags) {
    BubbleRegistrant registrant = findRegistrant(target);
    if(registrant == null) {
      log.warning("Instance of " + target.getClass() + " has not been registered.");
    } else {
      registrant.addTags(tags);
      updateTags(registrant);
    }
  }

  public void removeTags(BubbleRegistrant registrant, BubbleTag ... tagsToBeRemoved) {
    updateTags(registrant, tagsToBeRemoved);
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
        registrant.showBubble();
      }
    }
  }

  public void showAllBubbles() {
    for(BubbleRegistrant registrant : registrants) {
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
      }
    }
  }

  public void dismissAllBubbles() {
    for(BubbleRegistrant registrant : registrants) {
      registrant.dismissBubble();
    }
  }

  BubbleRegistrant findRegistrant(Object target) {
    for(BubbleRegistrant br : registrants) {
      if(br.getTarget().equals(target)) {
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
      updateTags(registrant,
        registrant.getTags().toArray(new BubbleTag[registrant.getTags().size()]));
      registrants.remove(registrant);
    }
  }

  abstract class BubbleRegistrant {
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
    abstract void showBubble();
    abstract void dismissBubble();
  }

  class CompositeBubbleRegistrant extends BubbleRegistrant {
    final Composite composite;

    CompositeBubbleRegistrant(Composite composite, Bubble bubble, BubbleTag ... tags) {
      super(bubble, tags);
      this.composite = composite;
    }

    Object getTarget() {
      return composite;
    }

    void showBubble() {

    }

    void dismissBubble() {

    }
  }
}
