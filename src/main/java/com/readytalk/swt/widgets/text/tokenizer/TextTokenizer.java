package com.readytalk.swt.widgets.text.tokenizer;

import java.nio.charset.Charset;
import java.util.List;

public interface TextTokenizer { 
  TextTokenizer setEncoding(Charset encoding);
  TextTokenizer reset();
  List<TextToken> tokenize(String text);
}
