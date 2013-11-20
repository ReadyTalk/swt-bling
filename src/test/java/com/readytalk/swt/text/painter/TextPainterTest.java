package com.readytalk.swt.text.painter;

import static org.junit.Assert.assertEquals;

import java.awt.event.MouseEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.readytalk.swt.text.navigation.NavigationListener;
import com.readytalk.swt.text.tokenizer.WikiTextTokenizer;

public class TextPainterTest {
  
  private static final String PLAIN_TEXT = "ABC 123 abc ` ~ ! @ # $ % ^ & * ( \n \t  ) - = _ + { } | \\ ] [ : \" ' ; < > ? / . ,";
  private static final String WIKI_TEXT = "[http://www.readytalk.com ReadyTalk] NORMAL ''ITALIC'' '''BOLD'''";
  private TextPainter painter;
  private Shell shell;
  
  @Mock
  NavigationListener navigationListener;
  
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    shell = new Shell();
    painter = new TextPainter(shell).setWrapping(true);
  }
  
  @Test
  public void setText_ConfiguredToTokenizePlainText_OneToken () {
    painter.setText(PLAIN_TEXT);
    assertEquals(1, painter.getTokens().size());
  }
  
  @Test
  public void setText_ConfiguredToTokenizeWikiText_SevenTokens () {
    painter.setTokenizer(new WikiTextTokenizer()).setText(WIKI_TEXT);
    assertEquals(7, painter.getTokens().size());
  }
  
//  public void mouseMoveListener_MouseUpHyperlink_NavigationEventSent() {
//    painter.setTokenizer(new WikiTextTokenizer()).setText(WIKI_TEXT).addNavigationListener(navigationListener);
//    shell.notifyListeners(SWT.MouseUp, new Event());
//    
//  }
}
