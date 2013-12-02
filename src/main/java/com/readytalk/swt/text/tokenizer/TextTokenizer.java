package com.readytalk.swt.text.tokenizer;

import java.nio.charset.Charset;
import java.util.List;

public interface TextTokenizer { 
  TextTokenizer setEncoding(Charset encoding);
  Charset getEncoding();
  TextTokenizer reset();
  List<TextToken> tokenize(String text);
}
