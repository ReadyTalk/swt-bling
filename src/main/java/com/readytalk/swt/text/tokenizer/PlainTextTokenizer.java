package com.readytalk.swt.text.tokenizer;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.readytalk.swt.text.painter.TextType;

class PlainTextTokenizer implements TextTokenizer {

  private List<TextToken> tokens = new ArrayList<TextToken>();
  private Charset encoding = Charset.defaultCharset();

  @Override
  public TextTokenizer setEncoding(Charset encoding) {
    this.encoding = encoding;
    return this;
  }

  @Override
  public Charset getEncoding() {
    return encoding;
  }

  @Override
  public TextTokenizer reset() {
    tokens.clear();
    return this;
  }

  public List<TextToken> tokenize(String text) {

    if (text == null || "".equals(text)) {
      return tokens;
    }

    TextToken textToken = new TextToken(TextType.TEXT, text);
    tokens.add(textToken);
    return tokens;
  }
}
