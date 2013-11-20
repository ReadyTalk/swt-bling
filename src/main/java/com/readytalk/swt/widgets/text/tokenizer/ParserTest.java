package com.readytalk.swt.widgets.text.tokenizer;

import java.io.IOException;
import java.util.List;

public class ParserTest {

	public static void main(String[] args) throws IOException {
		List<TextToken> tokens = new WikiTextTokenizer()
		    .tokenize("This is wiki '''text''' d '''BOLD TEXT''' http://www.google.com");
		for (TextToken token : tokens) {
			System.out.println(token);
		}
	}
}
