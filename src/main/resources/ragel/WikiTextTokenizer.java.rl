package com.readytalk.swt.text.tokenizer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.Charset;

import org.eclipse.swt.SWT;

import com.readytalk.swt.text.painter.TextType;

/*
 * This parser will automatically build via the gradle build process.  If you want to 
 * rebuild this parser by hand, run the following command:
 * 
 *    ragel -J WikiTextTokenizer.java.rl -o WikiTextTokenizer.java
 */
public class WikiTextTokenizer implements TextTokenizer {
	
	private Charset encoding = Charset.defaultCharset();
	private List<TextToken> tokens = new ArrayList<TextToken>();
	
	private int styleState = 0x00;
	
	@Override  
	public WikiTextTokenizer setEncoding(final Charset encoding) {
		this.encoding = encoding;
		return this;
	}
	
	@Override
	public Charset getEncoding() {
		return encoding;
	}
	
	@Override
	public WikiTextTokenizer reset() {
	  tokens.clear();
	  return this;
	}
	
    void emit(final TextType type, final byte[] data, final int start, final int end) {
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
    
    @Override
    public List<TextToken> tokenize(final String text) {
	  
      if(text == null || "".equals(text)) {
        return tokens;
      }
	  
      byte[] data = text.getBytes(encoding);
      int eof = data.length;
      
      // the names of these variables are specified by the ragel parser generator
      int p = 0, pe = data.length, te, ts, cs, act;
       
      %%{ 
      machine WikiTextScanner;
      word              = (any - (space|'\''))+;
      apostrophe        = '\'';
    	url               = ('http'|'https'|'file') '://' (any - space)+;
    	link              = '[' url ((' '|'\t')+ word* )? ']';
    	boldAndItalicText = '\'\'\'\'\'';
    	boldText          = '\'\'\'';
    	italicText        = '\'\'';

    	main := |*
    	  link              => { scanLink(splice(data, ts, te)); };
    	  url               => { 
    		  String url = spliceToString(data, ts, te);
    		  try {
    			  tokens.add(new TextToken(TextType.NAKED_URL, url).setUrl(new URL(url)));
			    } catch (MalformedURLException exception) {
				    tokens.add(new TextToken(TextType.TEXT, text));
			    }
    	  };
        apostrophe       => {emit(TextType.TEXT, data, ts, te);};
    	  word              => { 
    		switch(styleState) {
    			case SWT.BOLD:
    				emit(TextType.BOLD, data, ts, te);
    				break;
    			case SWT.ITALIC:
    				emit(TextType.ITALIC, data, ts, te);
    				break;
    			case SWT.BOLD|SWT.ITALIC:
    				emit(TextType.BOLD_AND_ITALIC, data, ts, te);
    				break;
    			default:
    				emit(TextType.TEXT, data, ts, te);
    		}
    	  };
    	  boldAndItalicText => { styleState ^= SWT.BOLD|SWT.ITALIC; };
    	  boldText          => { styleState ^= SWT.BOLD; };
    	  italicText        => { styleState ^= SWT.ITALIC; };
    	  space             => { emit(TextType.WHITESPACE, data, ts, te); };
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
		url      = (' ')* ('http'|'https'|'file') '://' (any - (' '|'\t'| '[' | ']'))+;
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
		if (text == null || "".equals(text)){
          tokens.add(new TextToken(TextType.LINK_URL, url).setUrl(new URL(url)));
		} else {
          tokens.add(new TextToken(TextType.LINK_AND_NAMED_URL, text).setUrl(new URL(url)));
		}
	  } catch (MalformedURLException exception) {
		tokens.add(new TextToken(TextType.TEXT, text + " (" + url + ") "));
	  }
    }
    %% write data;
}