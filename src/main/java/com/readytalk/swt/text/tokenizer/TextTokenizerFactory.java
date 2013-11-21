package com.readytalk.swt.text.tokenizer;

public class TextTokenizerFactory {
  
  public static TextTokenizer createTextTokenizer(TextTokenizerType type) 
      throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    return (TextTokenizer) Class.forName(type.classname).newInstance();
  }
  
  /**
   * @return TextTokenizer : of type PlainTextTokenizer
   */
  public static TextTokenizer createDefault() {
    return new PlainTextTokenizer();
  }
}
