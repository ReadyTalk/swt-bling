package com.readytalk.swt.text.tokenizer;

public enum TextTokenizerType {
    PLAIN ("com.readytalk.swt.text.tokenizer.PlainTextTokenizer"),
    WIKI ("com.readytalk.swt.text.tokenizer.WikiTextTokenizer");
    String classname;  
    
    private TextTokenizerType(String classname) {
      this.classname = classname;
    }
}
