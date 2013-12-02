package com.readytalk.swt.text.tokenizer;

import java.nio.charset.Charset;
import java.util.List;

/**
 * TextTokenizer is an interface to be implemented text tokenizers wanting
 * to be constructed by the TextTokenizerFactory.
 */
public interface TextTokenizer { 
  
  /**
   * Set the character encoding for the text tokenizer.  
   */
  TextTokenizer setEncoding(Charset encoding);

  /**
   * Get the character encoding of the text tokenizer.
   */
  Charset getEncoding();

  /**
   * Reset the tokenizer to its deafult state.
   */
  TextTokenizer reset();

  /**
   * Tokenize the given text and return a reference to the internal
   * token list.
   */
  List<TextToken> tokenize(String text);
}
