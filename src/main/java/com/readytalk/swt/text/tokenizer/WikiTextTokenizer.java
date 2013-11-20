
// line 1 "WikiTextTokenizer.java.rl"
package com.readytalk.swt.text.tokenizer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.Charset;

import org.eclipse.swt.SWT;

import com.readytalk.swt.text.painter.TextType;

/*
 * To rebuild this parser, run the following command:
 * ragel -J WikiTextTokenizer.java.rl -o WikiTextTokenizer.java
 */
public class WikiTextTokenizer implements TextTokenizer {
	
	private Charset encoding = Charset.defaultCharset();
	private List<TextToken> tokens = new ArrayList<TextToken>();
	
	private int styleState = 0x00;
	
	
	public WikiTextTokenizer setEncoding(final Charset encoding) {
		this.encoding = encoding;
		return this;
	}
	
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
    
    public List<TextToken> tokenize(final String text) {
      byte[] data = text.getBytes(encoding);
      int eof = data.length;
      
      // the names of these variables are specified by the ragel parser generator
      int p = 0, pe = data.length, te, ts, cs, act;
       
      
// line 62 "WikiTextTokenizer.java"
	{
	cs = WikiTextScanner_start;
	ts = -1;
	te = -1;
	act = 0;
	}

// line 70 "WikiTextTokenizer.java"
	{
	int _klen;
	int _trans = 0;
	int _acts;
	int _nacts;
	int _keys;
	int _goto_targ = 0;

	_goto: while (true) {
	switch ( _goto_targ ) {
	case 0:
	if ( p == pe ) {
		_goto_targ = 4;
		continue _goto;
	}
	if ( cs == 0 ) {
		_goto_targ = 5;
		continue _goto;
	}
case 1:
	_acts = _WikiTextScanner_from_state_actions[cs];
	_nacts = (int) _WikiTextScanner_actions[_acts++];
	while ( _nacts-- > 0 ) {
		switch ( _WikiTextScanner_actions[_acts++] ) {
	case 1:
// line 1 "NONE"
	{ts = p;}
	break;
// line 99 "WikiTextTokenizer.java"
		}
	}

	_match: do {
	_keys = _WikiTextScanner_key_offsets[cs];
	_trans = _WikiTextScanner_index_offsets[cs];
	_klen = _WikiTextScanner_single_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + _klen - 1;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + ((_upper-_lower) >> 1);
			if ( data[p] < _WikiTextScanner_trans_keys[_mid] )
				_upper = _mid - 1;
			else if ( data[p] > _WikiTextScanner_trans_keys[_mid] )
				_lower = _mid + 1;
			else {
				_trans += (_mid - _keys);
				break _match;
			}
		}
		_keys += _klen;
		_trans += _klen;
	}

	_klen = _WikiTextScanner_range_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + (_klen<<1) - 2;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + (((_upper-_lower) >> 1) & ~1);
			if ( data[p] < _WikiTextScanner_trans_keys[_mid] )
				_upper = _mid - 2;
			else if ( data[p] > _WikiTextScanner_trans_keys[_mid+1] )
				_lower = _mid + 2;
			else {
				_trans += ((_mid - _keys)>>1);
				break _match;
			}
		}
		_trans += _klen;
	}
	} while (false);

	_trans = _WikiTextScanner_indicies[_trans];
case 3:
	cs = _WikiTextScanner_trans_targs[_trans];

	if ( _WikiTextScanner_trans_actions[_trans] != 0 ) {
		_acts = _WikiTextScanner_trans_actions[_trans];
		_nacts = (int) _WikiTextScanner_actions[_acts++];
		while ( _nacts-- > 0 )
	{
			switch ( _WikiTextScanner_actions[_acts++] )
			{
	case 2:
// line 1 "NONE"
	{te = p+1;}
	break;
	case 3:
// line 68 "WikiTextTokenizer.java.rl"
	{act = 1;}
	break;
	case 4:
// line 69 "WikiTextTokenizer.java.rl"
	{act = 2;}
	break;
	case 5:
// line 77 "WikiTextTokenizer.java.rl"
	{act = 3;}
	break;
	case 6:
// line 92 "WikiTextTokenizer.java.rl"
	{te = p+1;{ styleState ^= SWT.BOLD|SWT.ITALIC; }}
	break;
	case 7:
// line 95 "WikiTextTokenizer.java.rl"
	{te = p+1;{ emit(TextType.WHITESPACE, data, ts, te); }}
	break;
	case 8:
// line 68 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ scanLink(splice(data, ts, te)); }}
	break;
	case 9:
// line 69 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ 
    		  String url = spliceToString(data, ts, te);
    		  try {
    			tokens.add(new TextToken(TextType.NAKED_URL, url).setUrl(new URL(url)));
			  } catch (MalformedURLException exception) {
				tokens.add(new TextToken(TextType.TEXT, text));
			  }
    	  }}
	break;
	case 10:
// line 77 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ 
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
    	  }}
	break;
	case 11:
// line 93 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ styleState ^= SWT.BOLD; }}
	break;
	case 12:
// line 94 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ styleState ^= SWT.ITALIC; }}
	break;
	case 13:
// line 93 "WikiTextTokenizer.java.rl"
	{{p = ((te))-1;}{ styleState ^= SWT.BOLD; }}
	break;
	case 14:
// line 1 "NONE"
	{	switch( act ) {
	case 1:
	{{p = ((te))-1;} scanLink(splice(data, ts, te)); }
	break;
	case 2:
	{{p = ((te))-1;} 
    		  String url = spliceToString(data, ts, te);
    		  try {
    			tokens.add(new TextToken(TextType.NAKED_URL, url).setUrl(new URL(url)));
			  } catch (MalformedURLException exception) {
				tokens.add(new TextToken(TextType.TEXT, text));
			  }
    	  }
	break;
	case 3:
	{{p = ((te))-1;} 
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
    	  }
	break;
	}
	}
	break;
// line 268 "WikiTextTokenizer.java"
			}
		}
	}

case 2:
	_acts = _WikiTextScanner_to_state_actions[cs];
	_nacts = (int) _WikiTextScanner_actions[_acts++];
	while ( _nacts-- > 0 ) {
		switch ( _WikiTextScanner_actions[_acts++] ) {
	case 0:
// line 1 "NONE"
	{ts = -1;}
	break;
// line 282 "WikiTextTokenizer.java"
		}
	}

	if ( cs == 0 ) {
		_goto_targ = 5;
		continue _goto;
	}
	if ( ++p != pe ) {
		_goto_targ = 1;
		continue _goto;
	}
case 4:
	if ( p == eof )
	{
	if ( _WikiTextScanner_eof_trans[cs] > 0 ) {
		_trans = _WikiTextScanner_eof_trans[cs] - 1;
		_goto_targ = 3;
		continue _goto;
	}
	}

case 5:
	}
	break; }
	}

// line 100 "WikiTextTokenizer.java.rl"

      
      return tokens;
    }
    
// line 315 "WikiTextTokenizer.java"
private static byte[] init__WikiTextScanner_actions_0()
{
	return new byte [] {
	    0,    1,    0,    1,    1,    1,    2,    1,    6,    1,    7,    1,
	    8,    1,    9,    1,   10,    1,   11,    1,   12,    1,   13,    1,
	   14,    2,    2,    3,    2,    2,    4,    2,    2,    5
	};
}

private static final byte _WikiTextScanner_actions[] = init__WikiTextScanner_actions_0();


private static short[] init__WikiTextScanner_key_offsets_0()
{
	return new short [] {
	    0,    0,    1,    2,    8,   13,   18,   25,   29,   30,   31,   37,
	   42,   47,   52,   57,   62,   67,   71,   77,   82,   87,   92,   97,
	  102,  108,  113,  118,  123,  128,  133,  138,  142,  145,  150,  155,
	  160
	};
}

private static final short _WikiTextScanner_key_offsets[] = init__WikiTextScanner_key_offsets_0();


private static char[] init__WikiTextScanner_trans_keys_0()
{
	return new char [] {
	   39,   39,    9,   32,   39,   93,   10,   13,   32,   39,   93,    9,
	   13,    9,   32,   93,   10,   13,   32,   39,   91,  102,  104,    9,
	   13,   32,   39,    9,   13,   39,   39,   32,   39,  102,  104,    9,
	   13,   32,   39,  105,    9,   13,   32,   39,  108,    9,   13,   32,
	   39,  101,    9,   13,   32,   39,   58,    9,   13,   32,   39,   47,
	    9,   13,   32,   39,   47,    9,   13,   32,   39,    9,   13,    9,
	   32,   39,   93,   10,   13,   32,   39,   93,    9,   13,    9,   32,
	   93,   10,   13,   32,   39,  116,    9,   13,   32,   39,  116,    9,
	   13,   32,   39,  112,    9,   13,   32,   39,   58,  115,    9,   13,
	   32,   39,  105,    9,   13,   32,   39,  108,    9,   13,   32,   39,
	  101,    9,   13,   32,   39,   58,    9,   13,   32,   39,   47,    9,
	   13,   32,   39,   47,    9,   13,   32,   39,    9,   13,   32,    9,
	   13,   32,   39,  116,    9,   13,   32,   39,  116,    9,   13,   32,
	   39,  112,    9,   13,   32,   39,   58,  115,    9,   13,    0
	};
}

private static final char _WikiTextScanner_trans_keys[] = init__WikiTextScanner_trans_keys_0();


private static byte[] init__WikiTextScanner_single_lengths_0()
{
	return new byte [] {
	    0,    1,    1,    4,    3,    3,    5,    2,    1,    1,    4,    3,
	    3,    3,    3,    3,    3,    2,    4,    3,    3,    3,    3,    3,
	    4,    3,    3,    3,    3,    3,    3,    2,    1,    3,    3,    3,
	    4
	};
}

private static final byte _WikiTextScanner_single_lengths[] = init__WikiTextScanner_single_lengths_0();


private static byte[] init__WikiTextScanner_range_lengths_0()
{
	return new byte [] {
	    0,    0,    0,    1,    1,    1,    1,    1,    0,    0,    1,    1,
	    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
	    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
	    1
	};
}

private static final byte _WikiTextScanner_range_lengths[] = init__WikiTextScanner_range_lengths_0();


private static short[] init__WikiTextScanner_index_offsets_0()
{
	return new short [] {
	    0,    0,    2,    4,   10,   15,   20,   27,   31,   33,   35,   41,
	   46,   51,   56,   61,   66,   71,   75,   81,   86,   91,   96,  101,
	  106,  112,  117,  122,  127,  132,  137,  142,  146,  149,  154,  159,
	  164
	};
}

private static final short _WikiTextScanner_index_offsets[] = init__WikiTextScanner_index_offsets_0();


private static byte[] init__WikiTextScanner_indicies_0()
{
	return new byte [] {
	    0,    1,    3,    2,    6,    6,    4,    7,    4,    5,    4,    4,
	    7,    4,    5,    6,    6,    9,    4,    8,   11,   12,   13,   14,
	   15,   11,   10,   16,   16,   16,   10,   18,   17,   20,   19,   16,
	   16,   21,   22,   16,   10,   16,   16,   23,   16,   10,   16,   16,
	   24,   16,   10,   16,   16,   25,   16,   10,   16,   16,   26,   16,
	   10,   16,   16,   27,   16,   10,   16,   16,   28,   16,   10,   16,
	    8,   16,   29,    6,    6,    8,   30,    4,   29,   31,   31,    7,
	   31,    5,    6,    6,    9,   31,    8,   16,   16,   32,   16,   10,
	   16,   16,   33,   16,   10,   16,   16,   34,   16,   10,   16,   16,
	   26,   25,   16,   10,   16,   16,   35,   16,   10,   16,   16,   36,
	   16,   10,   16,   16,   37,   16,   10,   16,   16,   38,   16,   10,
	   16,   16,   39,   16,   10,   16,   16,   40,   16,   10,    4,   42,
	    4,   41,   43,   43,   42,   16,   16,   44,   16,   10,   16,   16,
	   45,   16,   10,   16,   16,   46,   16,   10,   16,   16,   38,   37,
	   16,   10,    0
	};
}

private static final byte _WikiTextScanner_indicies[] = init__WikiTextScanner_indicies_0();


private static byte[] init__WikiTextScanner_trans_targs_0()
{
	return new byte [] {
	    8,    0,    6,    6,    6,    4,    3,   19,    5,   20,    7,    6,
	    1,   10,   25,   33,    6,    6,    9,    6,    2,   11,   21,   12,
	   13,   14,   15,   16,   17,   18,   18,    6,   22,   23,   24,   26,
	   27,   28,   29,   30,   31,   31,   32,    6,   34,   35,   36
	};
}

private static final byte _WikiTextScanner_trans_targs[] = init__WikiTextScanner_trans_targs_0();


private static byte[] init__WikiTextScanner_trans_actions_0()
{
	return new byte [] {
	    0,    0,   21,    7,   23,    0,    0,   25,    0,   25,    0,    9,
	    0,    0,    0,    0,   15,   19,    5,   17,    0,    0,    0,    0,
	    0,    0,    0,    0,   31,   31,   25,   11,    0,    0,    0,    0,
	    0,    0,    0,    0,   31,   28,    0,   13,    0,    0,    0
	};
}

private static final byte _WikiTextScanner_trans_actions[] = init__WikiTextScanner_trans_actions_0();


private static byte[] init__WikiTextScanner_to_state_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    1,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0
	};
}

private static final byte _WikiTextScanner_to_state_actions[] = init__WikiTextScanner_to_state_actions_0();


private static byte[] init__WikiTextScanner_from_state_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    3,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0
	};
}

private static final byte _WikiTextScanner_from_state_actions[] = init__WikiTextScanner_from_state_actions_0();


private static short[] init__WikiTextScanner_eof_trans_0()
{
	return new short [] {
	    0,    0,    3,    5,    5,    5,    0,   17,   18,   20,   17,   17,
	   17,   17,   17,   17,   17,   17,    5,   32,   32,   17,   17,   17,
	   17,   17,   17,   17,   17,   17,   17,    5,   44,   17,   17,   17,
	   17
	};
}

private static final short _WikiTextScanner_eof_trans[] = init__WikiTextScanner_eof_trans_0();


static final int WikiTextScanner_start = 6;
static final int WikiTextScanner_first_final = 6;
static final int WikiTextScanner_error = 0;

static final int WikiTextScanner_en_main = 6;


// line 105 "WikiTextTokenizer.java.rl"
    
    private void scanLink(byte[] data) {
      int eof = data.length;
      int p = 0, pe = data.length, te, ts, cs, act;
      String url = "";
      String text = "";
      
// line 507 "WikiTextTokenizer.java"
	{
	cs = URLParser_start;
	ts = -1;
	te = -1;
	act = 0;
	}

// line 515 "WikiTextTokenizer.java"
	{
	int _klen;
	int _trans = 0;
	int _acts;
	int _nacts;
	int _keys;
	int _goto_targ = 0;

	_goto: while (true) {
	switch ( _goto_targ ) {
	case 0:
	if ( p == pe ) {
		_goto_targ = 4;
		continue _goto;
	}
case 1:
	_acts = _URLParser_from_state_actions[cs];
	_nacts = (int) _URLParser_actions[_acts++];
	while ( _nacts-- > 0 ) {
		switch ( _URLParser_actions[_acts++] ) {
	case 1:
// line 1 "NONE"
	{ts = p;}
	break;
// line 540 "WikiTextTokenizer.java"
		}
	}

	_match: do {
	_keys = _URLParser_key_offsets[cs];
	_trans = _URLParser_index_offsets[cs];
	_klen = _URLParser_single_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + _klen - 1;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + ((_upper-_lower) >> 1);
			if ( data[p] < _URLParser_trans_keys[_mid] )
				_upper = _mid - 1;
			else if ( data[p] > _URLParser_trans_keys[_mid] )
				_lower = _mid + 1;
			else {
				_trans += (_mid - _keys);
				break _match;
			}
		}
		_keys += _klen;
		_trans += _klen;
	}

	_klen = _URLParser_range_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + (_klen<<1) - 2;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + (((_upper-_lower) >> 1) & ~1);
			if ( data[p] < _URLParser_trans_keys[_mid] )
				_upper = _mid - 2;
			else if ( data[p] > _URLParser_trans_keys[_mid+1] )
				_lower = _mid + 2;
			else {
				_trans += ((_mid - _keys)>>1);
				break _match;
			}
		}
		_trans += _klen;
	}
	} while (false);

	_trans = _URLParser_indicies[_trans];
case 3:
	cs = _URLParser_trans_targs[_trans];

	if ( _URLParser_trans_actions[_trans] != 0 ) {
		_acts = _URLParser_trans_actions[_trans];
		_nacts = (int) _URLParser_actions[_acts++];
		while ( _nacts-- > 0 )
	{
			switch ( _URLParser_actions[_acts++] )
			{
	case 2:
// line 1 "NONE"
	{te = p+1;}
	break;
	case 3:
// line 121 "WikiTextTokenizer.java.rl"
	{act = 3;}
	break;
	case 4:
// line 122 "WikiTextTokenizer.java.rl"
	{act = 4;}
	break;
	case 5:
// line 119 "WikiTextTokenizer.java.rl"
	{te = p+1;}
	break;
	case 6:
// line 120 "WikiTextTokenizer.java.rl"
	{te = p+1;}
	break;
	case 7:
// line 123 "WikiTextTokenizer.java.rl"
	{te = p+1;{ text += spliceToString(data, ts, te); }}
	break;
	case 8:
// line 121 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ url  += spliceToString(data, ts, te); }}
	break;
	case 9:
// line 122 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ text += spliceToString(data, ts, te); }}
	break;
	case 10:
// line 123 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ text += spliceToString(data, ts, te); }}
	break;
	case 11:
// line 123 "WikiTextTokenizer.java.rl"
	{{p = ((te))-1;}{ text += spliceToString(data, ts, te); }}
	break;
	case 12:
// line 1 "NONE"
	{	switch( act ) {
	case 3:
	{{p = ((te))-1;} url  += spliceToString(data, ts, te); }
	break;
	case 4:
	{{p = ((te))-1;} text += spliceToString(data, ts, te); }
	break;
	}
	}
	break;
// line 656 "WikiTextTokenizer.java"
			}
		}
	}

case 2:
	_acts = _URLParser_to_state_actions[cs];
	_nacts = (int) _URLParser_actions[_acts++];
	while ( _nacts-- > 0 ) {
		switch ( _URLParser_actions[_acts++] ) {
	case 0:
// line 1 "NONE"
	{ts = -1;}
	break;
// line 670 "WikiTextTokenizer.java"
		}
	}

	if ( ++p != pe ) {
		_goto_targ = 1;
		continue _goto;
	}
case 4:
	if ( p == eof )
	{
	if ( _URLParser_eof_trans[cs] > 0 ) {
		_trans = _URLParser_eof_trans[cs] - 1;
		_goto_targ = 3;
		continue _goto;
	}
	}

case 5:
	}
	break; }
	}

// line 128 "WikiTextTokenizer.java.rl"

		
	  try {
		tokens.add(new TextToken(TextType.LINK_AND_NAMED_URL, text).setUrl(new URL(url)));
	  } catch (MalformedURLException exception) {
		tokens.add(new TextToken(TextType.TEXT, text + " (" + url + ") "));
	  }
    }
    
// line 703 "WikiTextTokenizer.java"
private static byte[] init__URLParser_actions_0()
{
	return new byte [] {
	    0,    1,    0,    1,    1,    1,    2,    1,    5,    1,    6,    1,
	    7,    1,    8,    1,    9,    1,   10,    1,   11,    1,   12,    2,
	    2,    3,    2,    2,    4
	};
}

private static final byte _URLParser_actions[] = init__URLParser_actions_0();


private static byte[] init__URLParser_key_offsets_0()
{
	return new byte [] {
	    0,    3,    4,    5,    6,    7,    8,    9,   11,   12,   13,   14,
	   16,   22,   26,   29,   31,   36,   41,   46,   51,   56,   61,   66,
	   71,   76,   81
	};
}

private static final byte _URLParser_key_offsets[] = init__URLParser_key_offsets_0();


private static char[] init__URLParser_trans_keys_0()
{
	return new char [] {
	   32,  102,  104,  105,  108,  101,   58,   47,   47,    9,   32,  116,
	  116,  112,   58,  115,   10,   32,   91,   93,  102,  104,   10,   32,
	   91,   93,   32,  102,  104,    9,   32,   10,   32,   91,   93,  105,
	   10,   32,   91,   93,  108,   10,   32,   91,   93,  101,   10,   32,
	   58,   91,   93,   10,   32,   47,   91,   93,   10,   32,   47,   91,
	   93,    9,   10,   32,   91,   93,   10,   32,   91,   93,  116,   10,
	   32,   91,   93,  116,   10,   32,   91,   93,  112,   10,   32,   58,
	   91,   93,  115,    0
	};
}

private static final char _URLParser_trans_keys[] = init__URLParser_trans_keys_0();


private static byte[] init__URLParser_single_lengths_0()
{
	return new byte [] {
	    3,    1,    1,    1,    1,    1,    1,    2,    1,    1,    1,    2,
	    6,    4,    3,    2,    5,    5,    5,    5,    5,    5,    5,    5,
	    5,    5,    6
	};
}

private static final byte _URLParser_single_lengths[] = init__URLParser_single_lengths_0();


private static byte[] init__URLParser_range_lengths_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0
	};
}

private static final byte _URLParser_range_lengths[] = init__URLParser_range_lengths_0();


private static short[] init__URLParser_index_offsets_0()
{
	return new short [] {
	    0,    4,    6,    8,   10,   12,   14,   16,   19,   21,   23,   25,
	   28,   35,   40,   44,   47,   53,   59,   65,   71,   77,   83,   89,
	   95,  101,  107
	};
}

private static final short _URLParser_index_offsets[] = init__URLParser_index_offsets_0();


private static byte[] init__URLParser_indicies_0()
{
	return new byte [] {
	    1,    2,    3,    0,    4,    0,    5,    0,    6,    0,    7,    0,
	    8,    0,    9,    0,    0,    0,   10,   11,    0,   12,    0,   13,
	    0,    7,    6,    0,   15,   16,   17,   18,   19,   20,   14,   21,
	   21,   21,   21,   14,    1,    2,    3,   22,   23,   23,   10,   21,
	   21,   21,   21,   24,   14,   21,   21,   21,   21,   25,   14,   21,
	   21,   21,   21,   26,   14,   21,   21,   27,   21,   21,   14,   21,
	   21,   28,   21,   21,   14,   21,   21,   29,   21,   21,   14,   14,
	   10,   30,   10,   10,   31,   21,   21,   21,   21,   32,   14,   21,
	   21,   21,   21,   33,   14,   21,   21,   21,   21,   34,   14,   21,
	   21,   27,   21,   21,   26,   14,    0
	};
}

private static final byte _URLParser_indicies[] = init__URLParser_indicies_0();


private static byte[] init__URLParser_trans_targs_0()
{
	return new byte [] {
	   12,    0,    1,    8,    2,    3,    4,    5,    6,    7,   15,    9,
	   10,   11,   13,   12,   14,   12,   12,   16,   23,   12,   12,   12,
	   17,   18,   19,   20,   21,   22,   12,   22,   24,   25,   26
	};
}

private static final byte _URLParser_trans_targs[] = init__URLParser_trans_targs_0();


private static byte[] init__URLParser_trans_actions_0()
{
	return new byte [] {
	   19,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,   11,    5,    7,    9,    0,    0,   15,   17,   13,
	    0,    0,    0,    0,    0,   26,   21,   23,    0,    0,    0
	};
}

private static final byte _URLParser_trans_actions[] = init__URLParser_trans_actions_0();


private static byte[] init__URLParser_to_state_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    1,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0
	};
}

private static final byte _URLParser_to_state_actions[] = init__URLParser_to_state_actions_0();


private static byte[] init__URLParser_from_state_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    3,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0
	};
}

private static final byte _URLParser_from_state_actions[] = init__URLParser_from_state_actions_0();


private static short[] init__URLParser_eof_trans_0()
{
	return new short [] {
	    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
	    0,   22,   23,   24,   22,   22,   22,   22,   22,   22,   31,   22,
	   22,   22,   22
	};
}

private static final short _URLParser_eof_trans[] = init__URLParser_eof_trans_0();


static final int URLParser_start = 12;
static final int URLParser_first_final = 12;
static final int URLParser_error = -1;

static final int URLParser_en_main = 12;


// line 137 "WikiTextTokenizer.java.rl"
}