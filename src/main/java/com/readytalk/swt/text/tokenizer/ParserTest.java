package com.readytalk.swt.text.tokenizer;

import java.io.IOException;
import java.util.List;

public class ParserTest {

	public static void main(String[] args) throws IOException {
		List<TextToken> tokens = new WikiTextTokenizer()
		    .tokenize("This is wiki '''text''' d '''BOLD TEXT''' ''italic'' http://www.google.com [http://www.d.com DD]");
		for (TextToken token : tokens) {
			System.out.println(token);
		}
	}
}
