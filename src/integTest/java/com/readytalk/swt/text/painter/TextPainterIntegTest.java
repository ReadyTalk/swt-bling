package com.readytalk.swt.text.painter;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.readytalk.swt.text.navigation.NavigationListener;
import com.readytalk.swt.text.painter.TextPainter;
import com.readytalk.swt.text.tokenizer.TextTokenizer;
import com.readytalk.swt.text.tokenizer.TextTokenizerFactory;
import com.readytalk.swt.text.tokenizer.TextTokenizerType;

public class TextPainterIntegTest {
  
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
  
  @Ignore
  @Test
  public void setText_ConfiguredToTokenizePlainText_OneToken () {
    painter.setText(PLAIN_TEXT);
    assertEquals(1, painter.getTokens().size());
  }
  
  @Ignore
  @Test
  public void setText_ConfiguredToTokenizeWikiText_SevenTokens () 
      throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    
    TextTokenizer tokenizer = TextTokenizerFactory.createTextTokenizer(TextTokenizerType.WIKI);
    painter.setTokenizer(tokenizer).setText(WIKI_TEXT);
    assertEquals(7, painter.getTokens().size());
  }
  
}
