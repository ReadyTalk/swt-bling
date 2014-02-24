package com.readytalk.swt.text.tokenizer;

import java.util.ArrayList;
import java.util.List;

import com.readytalk.swt.text.painter.TextType;

class PlainTextTokenizer implements TextTokenizer {

  private static final String NEWLINE_SAFETY_TOKEN = "<:br:>";
  private static final String SPACE_SAFETY_TOKEN = "<:sp:>";
  private static final String SPACE_SPLITABLE_TOKEN = "<:ssp:>";
  private static final String NEWLINE = "\n";
  private static final String WHITESPACE_REGEX = "\\s+";

  private List<TextToken> tokens = new ArrayList<TextToken>();

  @Override
  public TextTokenizer reset() {
    tokens.clear();
    return this;
  }

  public List<TextToken> tokenize(String text) {

    if (text == null || "".equals(text)) {
      return tokens;
    }

    String str = text.replace(NEWLINE, NEWLINE_SAFETY_TOKEN);
    str = str.replaceAll(WHITESPACE_REGEX, SPACE_SPLITABLE_TOKEN);
    for (String s : str.split(SPACE_SPLITABLE_TOKEN)) {
      if (s.contains(NEWLINE_SAFETY_TOKEN)) {
        tokens.add(new TextToken(TextType.TEXT, s.replace(NEWLINE_SAFETY_TOKEN, NEWLINE)));
      } else {
        tokens.add(new TextToken(TextType.TEXT, s));
        tokens.add(new TextToken(TextType.WHITESPACE, " "));
      }
    }

    return tokens;
  }
}
