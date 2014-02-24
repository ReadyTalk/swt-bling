package com.readytalk.swt.text.tokenizer;

import java.util.ArrayList;
import java.util.List;

import com.readytalk.swt.text.painter.TextType;

class PlainTextTokenizer implements TextTokenizer {

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

    StringBuffer sb = new StringBuffer();
    char[] chars = text.toCharArray();
    for (char c : chars) {
      if (!Character.isWhitespace(c)) {
        sb.append(c);
      } else {
        if (sb.length() > 0) {
          tokens.add(new TextToken(TextType.TEXT, sb.toString()));
          tokens.add(new TextToken(TextType.WHITESPACE, " "));
          sb = new StringBuffer();
        }
      }
    }

    if (sb.length() > 0) {
      tokens.add(new TextToken(TextType.TEXT, sb.toString()));
    }
    return tokens;
  }
}
