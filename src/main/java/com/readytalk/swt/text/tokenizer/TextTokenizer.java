package com.readytalk.swt.text.tokenizer;

//import java.nio.charset.Charset;
import java.util.List;

/**
 * TextTokenizer is an interface to be implemented by text tokenizers wanting
 * to be constructed by the TextTokenizerFactory.
 */
public interface TextTokenizer {

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
