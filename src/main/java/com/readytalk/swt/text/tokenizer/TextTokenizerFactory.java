package com.readytalk.swt.text.tokenizer;

/**
 * TextTokenizerFactory abstracts the construction of text tokenizers
 * such that they only need to be present at runtime.  This is done
 * to enable the use of various parser generators which may output
 * Java classes dynamically during the build process.
 */
public class TextTokenizerFactory {
  
  /**
   * Constructs a Text tokenizer of the given TextTokenizerType.
   * 
   * @param  type {@link TextTokenizerType}
   * @return {@link TextTokenizer}
   */
  public static TextTokenizer createTextTokenizer(TextTokenizerType type) 
      throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    return (TextTokenizer) Class.forName(type.classname).newInstance();
  }
  
  /**
   * Create the default TextTokenizer; which is currently a PlainTextTokenizer.
   * 
   * @return {@link TextTokenizer}
   */
  public static TextTokenizer createDefault() {
    return new PlainTextTokenizer();
  }
}
