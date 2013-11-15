package com.readytalk.swt.widgets.text.tokenizer;

import java.io.IOException;
import java.util.List;

public class ParserTest {

	public static void main(String[] args) throws IOException {
		List<TextToken> tokens = new WikiTextTokenizer()
		    .tokenize("'asdasd' '''Bold Text''' \n" + "'''''Bold and Italic''''' \n"
		        + " asd ''ITALIC''\n" + "[http://www.google.com Google]\n");
		for (TextToken token : tokens) {
//			System.out.println(token);
		}
	}
}
