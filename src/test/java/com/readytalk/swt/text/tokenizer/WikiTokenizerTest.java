package com.readytalk.swt.text.tokenizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;

import com.readytalk.swt.text.painter.TextType;

public class WikiTokenizerTest {

  private static final String WIKI_NAKED_URL = "http://www.readytalk.com";
  private static final String WIKI_LINK_AND_NAMED_URL = "[http://www.readytalk.com ReadyTalk]";
  private static final String WIKI_LINK_URL = "[http://www.readytalk.com]";
  private static final String BOLD_TEXT = "'''bold'''";
  private static final String ITALIC_TEXT = "''italic''";
  private static final String BOLD_AND_ITALIC_TEXT = "'''''bold and italic'''''";
  TextTokenizer textTokenizer;

  @Before
  public void setup() throws Exception {
    textTokenizer = TextTokenizerFactory
        .createTextTokenizer(TextTokenizerType.WIKI);
  }

  @Test
  public void setEncoding_PassValidEncoding_getEncodingReturnsSameValue() {
    textTokenizer.setEncoding(Charset.defaultCharset());
    assertSame(Charset.defaultCharset(), textTokenizer.getEncoding());
  }

  @Test
  public void getEncoding_UponConstructionDefaultEncodingSet_returnDefaultEncoding() {
    assertSame(Charset.defaultCharset(), textTokenizer.getEncoding());
  }

  @Test
  public void tokenize_MultipleTokenizeCalls_TokenListContainsMoreElements() {
    textTokenizer.tokenize(WIKI_NAKED_URL);
    textTokenizer.tokenize(WIKI_NAKED_URL);
    textTokenizer.tokenize(WIKI_NAKED_URL);
    assertEquals(4, textTokenizer.tokenize(WIKI_NAKED_URL).size());
  }

  @Test
  public void tokenize_PassNull_TokenListContainsMoreElements() {
    assertEquals(0, textTokenizer.tokenize(null).size());
  }

  @Test
  public void tokenize_PassNakedUrl_ReceiveListContainingNakedUrlToken() {
    TextToken url = textTokenizer.tokenize(WIKI_NAKED_URL).get(0);
    assertEquals(TextType.NAKED_URL, url.getType());
  }
  
  @Test
  public void tokenize_PassLinkAndNamedUrl_ReceiveListContainingLinkAndNamedUrlToken() {
    TextToken url = textTokenizer.tokenize(WIKI_LINK_AND_NAMED_URL).get(0);
    assertEquals(TextType.LINK_AND_NAMED_URL, url.getType());
  }
  
  @Test
  public void tokenize_PassLinkUrl_ReceiveListContainingLinkUrlToken() {
    TextToken url = textTokenizer.tokenize(WIKI_LINK_URL).get(0);
    assertEquals(TextType.LINK_URL, url.getType());
  }
  
  @Test
  public void tokenize_PassBoldText_ReceiveListContainingBoldTextToken() {
    TextToken url = textTokenizer.tokenize(BOLD_TEXT).get(0);
    assertEquals(TextType.BOLD, url.getType());
  }
  
  @Test
  public void tokenize_PassItalicText_ReceiveListContainingItalicTextToken() {
    TextToken url = textTokenizer.tokenize(ITALIC_TEXT).get(0);
    assertEquals(TextType.ITALIC, url.getType());
  }
  
  @Test
  public void tokenize_PassBoldAndItalicText_ReceiveListContainingBoldAndItalicTextToken() {
    TextToken url = textTokenizer.tokenize(BOLD_AND_ITALIC_TEXT).get(0);
    assertEquals(TextType.BOLD_AND_ITALIC, url.getType());
  }

  @Test
  public void reset_MultipleTokenizeCalls_ResetClearsInternalList() {
    textTokenizer.tokenize(WIKI_NAKED_URL);
    textTokenizer.reset();
    assertEquals(1, textTokenizer.tokenize(WIKI_NAKED_URL).size());
  }
}
