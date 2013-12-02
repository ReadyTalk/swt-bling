package com.readytalk.swt.text.tokenizer;

/**
 * TextTokenizerType is an enumeration of the types of TextTokenizers which
 * can be constructed by the TextTokenizerFactory.
 */
public enum TextTokenizerType {
  PLAIN("com.readytalk.swt.text.tokenizer.PlainTextTokenizer"), 
  WIKI("com.readytalk.swt.text.tokenizer.WikiTextTokenizer");
  String classname;

  private TextTokenizerType(String classname) {
    this.classname = classname;
  }
}
