
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
case 1:
	_acts = _WikiTextScanner_from_state_actions[cs];
	_nacts = (int) _WikiTextScanner_actions[_acts++];
	while ( _nacts-- > 0 ) {
		switch ( _WikiTextScanner_actions[_acts++] ) {
	case 1:
// line 1 "NONE"
	{ts = p;}
	break;
// line 88 "WikiTextTokenizer.java"
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
// line 63 "WikiTextTokenizer.java.rl"
	{act = 3;}
	break;
	case 6:
// line 64 "WikiTextTokenizer.java.rl"
	{te = p+1;{ emit(TextToken.Type.BOLD_AND_ITALIC, data, ts+5, te-5); }}
	break;
	case 7:
// line 65 "WikiTextTokenizer.java.rl"
	{te = p+1;{ emit(TextToken.Type.BOLD, data, ts+3, te-3); }}
	break;
	case 8:
// line 66 "WikiTextTokenizer.java.rl"
	{te = p+1;{ emit(TextToken.Type.ITALIC, data, ts+2, te-2); }}
	break;
	case 9:
// line 67 "WikiTextTokenizer.java.rl"
	{te = p+1;{ emit(TextToken.Type.TEXT, data, ts, te); }}
	break;
	case 10:
// line 61 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ scanLink(splice(data, ts, te)); }}
	break;
	case 11:
// line 63 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ emit(TextToken.Type.TEXT, data, ts, te); }}
	break;
	case 12:
// line 63 "WikiTextTokenizer.java.rl"
	{{p = ((te))-1;}{ emit(TextToken.Type.TEXT, data, ts, te); }}
	break;
	case 13:
// line 1 "NONE"
	{	switch( act ) {
	case 1:
	{{p = ((te))-1;} scanLink(splice(data, ts, te)); }
	break;
	case 2:
	{{p = ((te))-1;} emit(TextToken.Type.NAKED_URL, data, ts, te); }
	break;
	case 3:
	{{p = ((te))-1;} emit(TextToken.Type.TEXT, data, ts, te); }
	break;
	}
	}
	break;
// line 211 "WikiTextTokenizer.java"
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
// line 225 "WikiTextTokenizer.java"
		}
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

// line 72 "WikiTextTokenizer.java.rl"

      
      return tokens;
    }
    
// line 254 "WikiTextTokenizer.java"
private static byte[] init__WikiTextScanner_actions_0()
{
	return new byte [] {
	    0,    1,    0,    1,    1,    1,    2,    1,    6,    1,    7,    1,
	    8,    1,    9,    1,   10,    1,   11,    1,   12,    1,   13,    2,
	    2,    3,    2,    2,    4,    2,    2,    5
	};
}

private static final byte _WikiTextScanner_actions[] = init__WikiTextScanner_actions_0();


private static short[] init__WikiTextScanner_key_offsets_0()
{
	return new short [] {
	    0,    1,    2,    3,    4,    5,    6,    7,    8,    9,   10,   15,
	   19,   25,   28,   32,   36,   40,   43,   47,   51,   55,   58,   62,
	   66,   70,   74,   78,   82,   85,   89,   93,   97,  101,  106,  110,
	  114,  117,  122,  126,  130,  134,  138,  142,  147,  151,  155,  158
	};
}

private static final short _WikiTextScanner_key_offsets[] = init__WikiTextScanner_key_offsets_0();


private static char[] init__WikiTextScanner_trans_keys_0()
{
	return new char [] {
	   39,   39,   39,   39,   39,   39,   39,   39,   39,   39,    9,   32,
	   93,   10,   13,   32,   93,    9,   13,   32,   39,   91,  104,    9,
	   13,   32,    9,   13,   32,   39,    9,   13,   32,   39,    9,   13,
	   32,   39,    9,   13,   32,    9,   13,   32,   39,    9,   13,   32,
	   39,    9,   13,   32,   39,    9,   13,   32,    9,   13,   32,   39,
	    9,   13,   32,   39,    9,   13,   32,   39,    9,   13,   32,   39,
	    9,   13,   32,   39,    9,   13,   32,   39,    9,   13,   32,    9,
	   13,   32,  104,    9,   13,   32,  116,    9,   13,   32,  116,    9,
	   13,   32,  112,    9,   13,   32,   58,  115,    9,   13,   32,   47,
	    9,   13,   32,   47,    9,   13,   32,    9,   13,    9,   32,   93,
	   10,   13,   32,   93,    9,   13,   32,   58,    9,   13,   32,  116,
	    9,   13,   32,  116,    9,   13,   32,  112,    9,   13,   32,   58,
	  115,    9,   13,   32,   47,    9,   13,   32,   47,    9,   13,   32,
	    9,   13,   32,   58,    9,   13,    0
	};
}

private static final char _WikiTextScanner_trans_keys[] = init__WikiTextScanner_trans_keys_0();


private static byte[] init__WikiTextScanner_single_lengths_0()
{
	return new byte [] {
	    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,    3,    2,
	    4,    1,    2,    2,    2,    1,    2,    2,    2,    1,    2,    2,
	    2,    2,    2,    2,    1,    2,    2,    2,    2,    3,    2,    2,
	    1,    3,    2,    2,    2,    2,    2,    3,    2,    2,    1,    2
	};
}

private static final byte _WikiTextScanner_single_lengths[] = init__WikiTextScanner_single_lengths_0();


private static byte[] init__WikiTextScanner_range_lengths_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,    1,
	    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
	    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
	    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,    1
	};
}

private static final byte _WikiTextScanner_range_lengths[] = init__WikiTextScanner_range_lengths_0();


private static short[] init__WikiTextScanner_index_offsets_0()
{
	return new short [] {
	    0,    2,    4,    6,    8,   10,   12,   14,   16,   18,   20,   25,
	   29,   35,   38,   42,   46,   50,   53,   57,   61,   65,   68,   72,
	   76,   80,   84,   88,   92,   95,   99,  103,  107,  111,  116,  120,
	  124,  127,  132,  136,  140,  144,  148,  152,  157,  161,  165,  168
	};
}

private static final short _WikiTextScanner_index_offsets[] = init__WikiTextScanner_index_offsets_0();


private static byte[] init__WikiTextScanner_indicies_0()
{
	return new byte [] {
	    2,    1,    3,    0,    5,    4,    6,    0,    7,    0,    9,    8,
	   10,    0,   11,    0,   12,    0,   13,    0,   16,   16,   17,   14,
	   15,   14,   17,   14,   15,   19,   20,   21,   22,   19,   18,   23,
	   23,   18,   23,   24,   23,   18,    1,   26,    1,   25,    1,   27,
	    1,   25,   23,   23,   18,    4,   29,    4,   28,    4,   30,    4,
	   28,   23,   31,   23,   18,   23,   23,   18,   23,   32,   23,   18,
	    8,   18,    8,   33,    8,   34,    8,   33,   23,   35,   23,   18,
	   23,   36,   23,   18,   23,   37,   23,   18,   23,   23,   18,   23,
	   38,   23,   18,   23,   39,   23,   18,   23,   40,   23,   18,   23,
	   41,   23,   18,   23,   42,   43,   23,   18,   23,   44,   23,   18,
	   23,   45,   23,   18,   23,   23,   46,   16,   16,   47,   14,   46,
	   48,   17,   48,   15,   23,   42,   23,   18,   23,   49,   23,   18,
	   23,   50,   23,   18,   23,   51,   23,   18,   23,   52,   53,   23,
	   18,   23,   54,   23,   18,   23,   55,   23,   18,   14,   14,   56,
	   23,   52,   23,   18,    0
	};
}

private static final byte _WikiTextScanner_indicies[] = init__WikiTextScanner_indicies_0();


private static byte[] init__WikiTextScanner_trans_targs_0()
{
	return new byte [] {
	   12,    0,    1,   12,    2,    3,    4,   12,    5,    6,    7,    8,
	    9,   12,   12,   11,   10,   38,   13,   12,   14,   29,   40,   12,
	   15,   16,   18,   17,   19,   22,   20,   21,   23,   24,   25,   26,
	   27,   28,   30,   31,   32,   33,   34,   39,   35,   36,   37,   37,
	   12,   41,   42,   43,   44,   47,   45,   46,   46
	};
}

private static final byte _WikiTextScanner_trans_targs[] = init__WikiTextScanner_trans_targs_0();


private static byte[] init__WikiTextScanner_trans_actions_0()
{
	return new byte [] {
	   19,    0,    0,   11,    0,    0,    0,    9,    0,    0,    0,    0,
	    0,    7,   21,    0,    0,   23,    0,   13,    0,    0,    0,   17,
	    5,    5,    5,    0,    5,    0,    0,    0,    5,    5,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,   29,   23,
	   15,    0,    0,    0,    0,    0,    0,   29,   26
	};
}

private static final byte _WikiTextScanner_trans_actions[] = init__WikiTextScanner_trans_actions_0();


private static byte[] init__WikiTextScanner_to_state_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    1,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0
	};
}

private static final byte _WikiTextScanner_to_state_actions[] = init__WikiTextScanner_to_state_actions_0();


private static byte[] init__WikiTextScanner_from_state_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    3,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0
	};
}

private static final byte _WikiTextScanner_from_state_actions[] = init__WikiTextScanner_from_state_actions_0();


private static short[] init__WikiTextScanner_eof_trans_0()
{
	return new short [] {
	    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,   15,   15,
	    0,   24,   24,   24,   24,   24,   24,   24,   24,   24,   24,   24,
	   24,   24,   24,   24,   24,   24,   24,   24,   24,   24,   24,   24,
	   24,   15,   49,   24,   24,   24,   24,   24,   24,   24,   15,   24
	};
}

private static final short _WikiTextScanner_eof_trans[] = init__WikiTextScanner_eof_trans_0();


static final int WikiTextScanner_start = 12;
static final int WikiTextScanner_first_final = 12;
static final int WikiTextScanner_error = -1;

static final int WikiTextScanner_en_main = 12;


// line 77 "WikiTextTokenizer.java.rl"
    
    private void scanLink(byte[] data) {
      int eof = data.length;
      int p = 0, pe = data.length, te, ts, cs, act;
      String url = "";
      String text = "";
      
// line 448 "WikiTextTokenizer.java"
	{
	cs = URLParser_start;
	ts = -1;
	te = -1;
	act = 0;
	}

// line 456 "WikiTextTokenizer.java"
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
// line 481 "WikiTextTokenizer.java"
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
// line 93 "WikiTextTokenizer.java.rl"
	{act = 3;}
	break;
	case 4:
// line 94 "WikiTextTokenizer.java.rl"
	{act = 4;}
	break;
	case 5:
// line 91 "WikiTextTokenizer.java.rl"
	{te = p+1;}
	break;
	case 6:
// line 92 "WikiTextTokenizer.java.rl"
	{te = p+1;}
	break;
	case 7:
// line 95 "WikiTextTokenizer.java.rl"
	{te = p+1;{ text += spliceToString(data, ts, te); }}
	break;
	case 8:
// line 93 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ url  += spliceToString(data, ts, te); }}
	break;
	case 9:
// line 94 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ text += spliceToString(data, ts, te); }}
	break;
	case 10:
// line 95 "WikiTextTokenizer.java.rl"
	{te = p;p--;{ text += spliceToString(data, ts, te); }}
	break;
	case 11:
// line 95 "WikiTextTokenizer.java.rl"
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
// line 597 "WikiTextTokenizer.java"
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
// line 611 "WikiTextTokenizer.java"
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

// line 100 "WikiTextTokenizer.java.rl"

		
	  try {
		tokens.add(new TextToken(
			TextToken.Type.LINK_AND_NAMED_URL, text).setUrl(new URL(url)));
	  } catch (MalformedURLException exception) {
		tokens.add(new TextToken(TextToken.Type.TEXT, text + " (" + url + ") "));
	  }
    }
    
// line 645 "WikiTextTokenizer.java"
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


// line 110 "WikiTextTokenizer.java.rl"
}