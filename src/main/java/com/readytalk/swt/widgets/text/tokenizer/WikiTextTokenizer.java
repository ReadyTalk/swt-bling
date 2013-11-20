
// line 1 "WikiTextTokenizer.java.rl"
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
       
      
// line 55 "WikiTextTokenizer.java"
	{
	cs = WikiTextScanner_start;
	ts = -1;
	te = -1;
	act = 0;
	}

// line 63 "WikiTextTokenizer.java"
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
// line 92 "WikiTextTokenizer.java"
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
// line 61 "WikiTextTokenizer.java.rl"
	{act = 1;}
	break;
	case 4:
// line 62 "WikiTextTokenizer.java.rl"
	{act = 2;}
	break;
	case 5:
// line 71 "WikiTextTokenizer.java.rl"
	{act = 3;}
	break;
	case 6:
// line 72 "WikiTextTokenizer.java.rl"
	{te = p+1;{ emit(TextToken.Type.BOLD_AND_ITALIC, data, ts+5, te-5); }}
	break;
	case 7:
// line 73 "WikiTextTokenizer.java.rl"
	{te = p+1;{ emit(TextToken.Type.BOLD, data, ts+3, te-3); }}
	break;
	case 8:
// line 74 "WikiTextTokenizer.java.rl"
	{te = p+1;{ emit(TextToken.Type.ITALIC, data, ts+2, te-2); }}
	break;
	case 9:
// line 75 "WikiTextTokenizer.java.rl"
	{te = p+1;{ emit(TextToken.Type.TEXT, data, ts, te); }}
	break;
	case 10:
// line 61 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ scanLink(splice(data, ts, te)); }}
	break;
	case 11:
// line 62 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ 
    		  String url = spliceToString(data, ts, te);
    		  try {
    			tokens.add(new TextToken(
    				TextToken.Type.NAKED_URL, url).setUrl(new URL(url)));
			  } catch (MalformedURLException exception) {
				tokens.add(new TextToken(TextToken.Type.TEXT, text));
			  }
    	  }}
	break;
	case 12:
// line 71 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ emit(TextToken.Type.TEXT, data, ts, te); }}
	break;
	case 13:
// line 1 "NONE"
	{	switch( act ) {
	case 1:
	{{p = ((te))-1;} scanLink(splice(data, ts, te)); }
	break;
	case 2:
	{{p = ((te))-1;} 
    		  String url = spliceToString(data, ts, te);
    		  try {
    			tokens.add(new TextToken(
    				TextToken.Type.NAKED_URL, url).setUrl(new URL(url)));
			  } catch (MalformedURLException exception) {
				tokens.add(new TextToken(TextToken.Type.TEXT, text));
			  }
    	  }
	break;
	case 3:
	{{p = ((te))-1;} emit(TextToken.Type.TEXT, data, ts, te); }
	break;
	}
	}
	break;
// line 231 "WikiTextTokenizer.java"
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
// line 245 "WikiTextTokenizer.java"
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

// line 80 "WikiTextTokenizer.java.rl"

      
      return tokens;
    }
    
// line 278 "WikiTextTokenizer.java"
private static byte[] init__WikiTextScanner_actions_0()
{
	return new byte [] {
	    0,    1,    0,    1,    1,    1,    6,    1,    7,    1,    8,    1,
	    9,    1,   10,    1,   11,    1,   12,    1,   13,    2,    2,    3,
	    2,    2,    4,    2,    2,    5
	};
}

private static final byte _WikiTextScanner_actions[] = init__WikiTextScanner_actions_0();


private static short[] init__WikiTextScanner_key_offsets_0()
{
	return new short [] {
	    0,    0,    1,    2,    3,    4,    5,    6,    7,    8,    9,   10,
	   11,   12,   13,   14,   15,   21,   26,   31,   37,   41,   46,   51,
	   56,   61,   67,   72,   77,   81,   87,   92,   97,  102,  107,  112,
	  117,  123,  128,  133,  137,  140
	};
}

private static final short _WikiTextScanner_key_offsets[] = init__WikiTextScanner_key_offsets_0();


private static char[] init__WikiTextScanner_trans_keys_0()
{
	return new char [] {
	   39,   39,   39,   39,   39,   39,   39,   39,   39,   39,   39,   39,
	   39,   39,   39,    9,   32,   39,   93,   10,   13,   32,   39,   93,
	    9,   13,    9,   32,   93,   10,   13,   32,   39,   91,  104,    9,
	   13,   32,   39,    9,   13,   32,   39,  104,    9,   13,   32,   39,
	  116,    9,   13,   32,   39,  116,    9,   13,   32,   39,  112,    9,
	   13,   32,   39,   58,  115,    9,   13,   32,   39,   47,    9,   13,
	   32,   39,   47,    9,   13,   32,   39,    9,   13,    9,   32,   39,
	   93,   10,   13,   32,   39,   93,    9,   13,    9,   32,   93,   10,
	   13,   32,   39,   58,    9,   13,   32,   39,  116,    9,   13,   32,
	   39,  116,    9,   13,   32,   39,  112,    9,   13,   32,   39,   58,
	  115,    9,   13,   32,   39,   47,    9,   13,   32,   39,   47,    9,
	   13,   32,   39,    9,   13,   32,    9,   13,   32,   39,   58,    9,
	   13,    0
	};
}

private static final char _WikiTextScanner_trans_keys[] = init__WikiTextScanner_trans_keys_0();


private static byte[] init__WikiTextScanner_single_lengths_0()
{
	return new byte [] {
	    0,    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
	    1,    1,    1,    1,    4,    3,    3,    4,    2,    3,    3,    3,
	    3,    4,    3,    3,    2,    4,    3,    3,    3,    3,    3,    3,
	    4,    3,    3,    2,    1,    3
	};
}

private static final byte _WikiTextScanner_single_lengths[] = init__WikiTextScanner_single_lengths_0();


private static byte[] init__WikiTextScanner_range_lengths_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    1,    1,    1,    1,    1,    1,    1,    1,
	    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
	    1,    1,    1,    1,    1,    1
	};
}

private static final byte _WikiTextScanner_range_lengths[] = init__WikiTextScanner_range_lengths_0();


private static short[] init__WikiTextScanner_index_offsets_0()
{
	return new short [] {
	    0,    0,    2,    4,    6,    8,   10,   12,   14,   16,   18,   20,
	   22,   24,   26,   28,   30,   36,   41,   46,   52,   56,   61,   66,
	   71,   76,   82,   87,   92,   96,  102,  107,  112,  117,  122,  127,
	  132,  138,  143,  148,  152,  155
	};
}

private static final short _WikiTextScanner_index_offsets[] = init__WikiTextScanner_index_offsets_0();


private static byte[] init__WikiTextScanner_indicies_0()
{
	return new byte [] {
	    0,    1,    3,    2,    4,    2,    5,    1,    7,    6,    8,    6,
	    9,    1,   10,    1,   11,    1,    1,   12,   13,   12,   14,    1,
	   15,    1,   16,    1,   17,    1,   20,   20,   18,   21,   18,   19,
	   18,   18,   21,   18,   19,   20,   20,   23,   18,   22,   25,   26,
	   27,   28,   25,   24,   29,   29,   29,   24,   29,   29,   30,   29,
	   24,   29,   29,   31,   29,   24,   29,   29,   32,   29,   24,   29,
	   29,   33,   29,   24,   29,   29,   34,   35,   29,   24,   29,   29,
	   36,   29,   24,   29,   29,   37,   29,   24,   29,   22,   29,   38,
	   20,   20,   22,   39,   18,   38,   40,   40,   21,   40,   19,   20,
	   20,   23,   40,   22,   29,   29,   34,   29,   24,   29,   29,   41,
	   29,   24,   29,   29,   42,   29,   24,   29,   29,   43,   29,   24,
	   29,   29,   44,   45,   29,   24,   29,   29,   46,   29,   24,   29,
	   29,   47,   29,   24,   18,   49,   18,   48,   50,   50,   49,   29,
	   29,   44,   29,   24,    0
	};
}

private static final byte _WikiTextScanner_indicies[] = init__WikiTextScanner_indicies_0();


private static byte[] init__WikiTextScanner_trans_targs_0()
{
	return new byte [] {
	    2,    0,    3,    5,    4,   19,    6,    9,    7,    8,   19,   10,
	   11,   12,   13,   14,   15,   19,   19,   17,   16,   30,   18,   31,
	   20,   19,    1,   21,   33,   19,   22,   23,   24,   25,   26,   32,
	   27,   28,   29,   29,   19,   34,   35,   36,   37,   41,   38,   39,
	   39,   40,   19
	};
}

private static final byte _WikiTextScanner_trans_targs[] = init__WikiTextScanner_trans_targs_0();


private static byte[] init__WikiTextScanner_trans_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    9,    0,    0,    0,    0,    7,    0,
	    0,    0,    0,    0,    0,    5,   19,    0,    0,   21,    0,   21,
	    0,   11,    0,    0,    0,   17,    0,    0,    0,    0,    0,    0,
	    0,   27,   27,   21,   13,    0,    0,    0,    0,    0,    0,   27,
	   24,    0,   15
	};
}

private static final byte _WikiTextScanner_trans_actions[] = init__WikiTextScanner_trans_actions_0();


private static byte[] init__WikiTextScanner_to_state_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    1,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0
	};
}

private static final byte _WikiTextScanner_to_state_actions[] = init__WikiTextScanner_to_state_actions_0();


private static byte[] init__WikiTextScanner_from_state_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    3,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0
	};
}

private static final byte _WikiTextScanner_from_state_actions[] = init__WikiTextScanner_from_state_actions_0();


private static short[] init__WikiTextScanner_eof_trans_0()
{
	return new short [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,   19,   19,   19,    0,   30,   30,   30,   30,
	   30,   30,   30,   30,   30,   19,   41,   41,   30,   30,   30,   30,
	   30,   30,   30,   19,   51,   30
	};
}

private static final short _WikiTextScanner_eof_trans[] = init__WikiTextScanner_eof_trans_0();


static final int WikiTextScanner_start = 19;
static final int WikiTextScanner_first_final = 19;
static final int WikiTextScanner_error = 0;

static final int WikiTextScanner_en_main = 19;


// line 85 "WikiTextTokenizer.java.rl"
    
    private void scanLink(byte[] data) {
      int eof = data.length;
      int p = 0, pe = data.length, te, ts, cs, act;
      String url = "";
      String text = "";
      
// line 470 "WikiTextTokenizer.java"
	{
	cs = URLParser_start;
	ts = -1;
	te = -1;
	act = 0;
	}

// line 478 "WikiTextTokenizer.java"
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
// line 503 "WikiTextTokenizer.java"
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
// line 101 "WikiTextTokenizer.java.rl"
	{act = 3;}
	break;
	case 4:
// line 102 "WikiTextTokenizer.java.rl"
	{act = 4;}
	break;
	case 5:
// line 99 "WikiTextTokenizer.java.rl"
	{te = p+1;}
	break;
	case 6:
// line 100 "WikiTextTokenizer.java.rl"
	{te = p+1;}
	break;
	case 7:
// line 103 "WikiTextTokenizer.java.rl"
	{te = p+1;{ text += spliceToString(data, ts, te); }}
	break;
	case 8:
// line 101 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ url  += spliceToString(data, ts, te); }}
	break;
	case 9:
// line 102 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ text += spliceToString(data, ts, te); }}
	break;
	case 10:
// line 103 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ text += spliceToString(data, ts, te); }}
	break;
	case 11:
// line 103 "WikiTextTokenizer.java.rl"
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
// line 619 "WikiTextTokenizer.java"
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
// line 633 "WikiTextTokenizer.java"
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

// line 108 "WikiTextTokenizer.java.rl"

		
	  try {
		tokens.add(new TextToken(
			TextToken.Type.LINK_AND_NAMED_URL, text).setUrl(new URL(url)));
	  } catch (MalformedURLException exception) {
		tokens.add(new TextToken(TextToken.Type.TEXT, text + " (" + url + ") "));
	  }
    }
    
// line 667 "WikiTextTokenizer.java"
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
	    0,    2,    3,    4,    5,    7,    8,    9,   11,   12,   17,   21,
	   23,   25,   30,   35,   40,   46,   51,   56,   61
	};
}

private static final byte _URLParser_key_offsets[] = init__URLParser_key_offsets_0();


private static char[] init__URLParser_trans_keys_0()
{
	return new char [] {
	   32,  104,  116,  116,  112,   58,  115,   47,   47,    9,   32,   58,
	   10,   32,   91,   93,  104,   10,   32,   91,   93,   32,  104,    9,
	   32,   10,   32,   91,   93,  116,   10,   32,   91,   93,  116,   10,
	   32,   91,   93,  112,   10,   32,   58,   91,   93,  115,   10,   32,
	   47,   91,   93,   10,   32,   47,   91,   93,    9,   10,   32,   91,
	   93,   10,   32,   58,   91,   93,    0
	};
}

private static final char _URLParser_trans_keys[] = init__URLParser_trans_keys_0();


private static byte[] init__URLParser_single_lengths_0()
{
	return new byte [] {
	    2,    1,    1,    1,    2,    1,    1,    2,    1,    5,    4,    2,
	    2,    5,    5,    5,    6,    5,    5,    5,    5
	};
}

private static final byte _URLParser_single_lengths[] = init__URLParser_single_lengths_0();


private static byte[] init__URLParser_range_lengths_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0
	};
}

private static final byte _URLParser_range_lengths[] = init__URLParser_range_lengths_0();


private static byte[] init__URLParser_index_offsets_0()
{
	return new byte [] {
	    0,    3,    5,    7,    9,   12,   14,   16,   19,   21,   27,   32,
	   35,   38,   44,   50,   56,   63,   69,   75,   81
	};
}

private static final byte _URLParser_index_offsets[] = init__URLParser_index_offsets_0();


private static byte[] init__URLParser_indicies_0()
{
	return new byte [] {
	    1,    2,    0,    3,    0,    4,    0,    5,    0,    6,    7,    0,
	    8,    0,    9,    0,    0,    0,   10,    6,    0,   12,   13,   14,
	   15,   16,   11,   17,   17,   17,   17,   11,    1,    2,   18,   19,
	   19,   10,   17,   17,   17,   17,   20,   11,   17,   17,   17,   17,
	   21,   11,   17,   17,   17,   17,   22,   11,   17,   17,   23,   17,
	   17,   24,   11,   17,   17,   25,   17,   17,   11,   17,   17,   26,
	   17,   17,   11,   11,   10,   27,   10,   10,   28,   17,   17,   23,
	   17,   17,   11,    0
	};
}

private static final byte _URLParser_indicies[] = init__URLParser_indicies_0();


private static byte[] init__URLParser_trans_targs_0()
{
	return new byte [] {
	    9,    0,    1,    2,    3,    4,    5,    8,    6,    7,   12,   10,
	    9,   11,    9,    9,   13,    9,    9,    9,   14,   15,   16,   17,
	   20,   18,   19,    9,   19
	};
}

private static final byte _URLParser_trans_targs[] = init__URLParser_trans_targs_0();


private static byte[] init__URLParser_trans_actions_0()
{
	return new byte [] {
	   19,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	   11,    5,    7,    9,    0,   15,   17,   13,    0,    0,    0,    0,
	    0,    0,   26,   21,   23
	};
}

private static final byte _URLParser_trans_actions[] = init__URLParser_trans_actions_0();


private static byte[] init__URLParser_to_state_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0
	};
}

private static final byte _URLParser_to_state_actions[] = init__URLParser_to_state_actions_0();


private static byte[] init__URLParser_from_state_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    3,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0
	};
}

private static final byte _URLParser_from_state_actions[] = init__URLParser_from_state_actions_0();


private static byte[] init__URLParser_eof_trans_0()
{
	return new byte [] {
	    1,    1,    1,    1,    1,    1,    1,    1,    1,    0,   18,   19,
	   20,   18,   18,   18,   18,   18,   18,   28,   18
	};
}

private static final byte _URLParser_eof_trans[] = init__URLParser_eof_trans_0();


static final int URLParser_start = 9;
static final int URLParser_first_final = 9;
static final int URLParser_error = -1;

static final int URLParser_en_main = 9;


// line 118 "WikiTextTokenizer.java.rl"
}