package com.readytalk.swt.widgets.text.tokenizer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.Charset;

/*
 * To rebuild this parser, run the following command:
 * ragel -J WikiTextTokenizer.java.rl -o WikiTextTokenizer.java
 */
public class WikiTextTokenizer implements TextTokenizer {
	
	private Charset encoding = Charset.defaultCharset();
	private List<TextToken> tokens = new ArrayList<TextToken>();
	
	public WikiTextTokenizer setEncoding(final Charset encoding) {
		this.encoding = encoding;
		return this;
	}
	
	public WikiTextTokenizer reset() {
	  tokens.clear();
	  return this;
	}
	
    void emit(final TextToken.Type type, final byte[] data, final int start, final int end) {
      String text = spliceToString(data, start, end);
      tokens.add(new TextToken(type, text));
    }
    
    byte[] splice(final byte[] data, final int start, final int end) {
      int length = end-start;
      byte[] splicedData = new byte[length];
      System.arraycopy(data, start, splicedData, 0, length);
      return splicedData;
    }
    
    String spliceToString(final byte[] data, final int start, final int end) {
      return new String(splice(data, start, end), encoding);
    }
    
    public List<TextToken> tokenize(final String text) {
      byte[] data = text.getBytes(encoding);
      int eof = data.length;
      
      // the names of these variables are specified by the ragel parser generator
      int p = 0, pe = data.length, te, ts, cs, act;
       
      %%{ 
        machine WikiTextScanner;   
    	word              = (any - space)+;
    	url               = ('http'|'https') '://' (any - space)+;
    	link              = '[' url ((' '|'\t')+ word* )? ']';
    	boldAndItalicText = '\'\'\'\'\'' (any - ('\''))+ '\'\'\'\'\'';
    	boldText          = ('\'\'\'') (any - ('\''))+ ('\'\'\'');
    	italicText        = '\'\'' (any - ('\''))+ '\'\'';

    	main := |*
    	  link              => { scanLink(splice(data, ts, te)); };
    	  url               => { 
    		  String url = spliceToString(data, ts, te);
    		  try {
    			tokens.add(new TextToken(
    				TextToken.Type.NAKED_URL, text).setUrl(new URL(url)));
			  } catch (MalformedURLException exception) {
				tokens.add(new TextToken(TextToken.Type.TEXT, text));
			  }
    	  };
    	  word              => { emit(TextToken.Type.TEXT, data, ts, te); };
    	  boldAndItalicText => { emit(TextToken.Type.BOLD_AND_ITALIC, data, ts+5, te-5); };
    	  boldText          => { emit(TextToken.Type.BOLD, data, ts+3, te-3); };
    	  italicText        => { emit(TextToken.Type.ITALIC, data, ts+2, te-2); };
    	  space             => { emit(TextToken.Type.TEXT, data, ts, te); };
    	*|;
    	   
    	write init;
    	write exec;
      }%%
      
      return tokens;
    }
    %% write data;
    
    private void scanLink(byte[] data) {
      int eof = data.length;
      int p = 0, pe = data.length, te, ts, cs, act;
      String url = "";
      String text = "";
      %%{ 
	    machine URLParser;
		lbrace   = '[';
		rbrace   = ']';
		url      = (' ')* ('http'|'https') '://' (any - (' '|'\t'))+;
		linkText = (any - (' ' | '\n' | '[' | ']'))+;
	
		main := |*
		  lbrace;
		  rbrace;
		  url        => { url  += spliceToString(data, ts, te); };
		  linkText   => { text += spliceToString(data, ts, te); };
		  space      => { text += spliceToString(data, ts, te); };
		*|;
			
		write init;
		write exec;
	  }%%
		
	  try {
		tokens.add(new TextToken(
			TextToken.Type.LINK_AND_NAMED_URL, text).setUrl(new URL(url)));
	  } catch (MalformedURLException exception) {
		tokens.add(new TextToken(TextToken.Type.TEXT, text + " (" + url + ") "));
	  }
    }
    %% write data;
}