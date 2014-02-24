package com.readytalk.swt.text.tokenizer;

import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TextTokenizerTest {
	
	private static final String PLAIN_TEXT = "ABC 123 abc ` ~ ! @ # $ % ^ & * ( \n \t  ) - = _ + { } | \\ ] [ : \" ' ; < > ? / . ,";
	TextTokenizer textTokenizer;
	
	@Before
	public void setup() throws Exception {
		textTokenizer = TextTokenizerFactory.createDefault();
	}
	
	@Test
	public void tokenize_MultipleTokenizeCalls_TokenListContainsMoreElements() {
		textTokenizer.tokenize(PLAIN_TEXT);
		textTokenizer.tokenize(PLAIN_TEXT);
		textTokenizer.tokenize(PLAIN_TEXT);
		Assert.assertEquals(284, textTokenizer.tokenize(PLAIN_TEXT).size());
	}
	
	@Test
	public void reset_MultipleTokenizeCalls_ResetClearsInternalList() {
		textTokenizer.tokenize(PLAIN_TEXT);
		textTokenizer.tokenize(PLAIN_TEXT);
		textTokenizer.tokenize(PLAIN_TEXT);
		textTokenizer.reset();
		Assert.assertEquals(71, textTokenizer.tokenize(PLAIN_TEXT).size());
	}
}
