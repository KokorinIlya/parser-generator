// Generated from src/main/java/parser/Input.g4 by ANTLR 4.7.1

package parser;
import input.*;
import utils.ScalaUtils;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class InputLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, JAVA=2, LEFT_BRACKET=3, RIGHT_BRACKET=4, HEADER=5, TOKENS=6, SEMI=7, 
		TOKEN_NAME=8, TOKEN_REGEXP=9, EQ=10;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"WS", "JAVA", "LEFT_BRACKET", "RIGHT_BRACKET", "HEADER", "TOKENS", "SEMI", 
		"TOKEN_NAME", "TOKEN_REGEXP", "EQ"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'hh'", "'{'", "'}'", "'header'", "'tokens'", "';'", null, 
		null, "'='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "WS", "JAVA", "LEFT_BRACKET", "RIGHT_BRACKET", "HEADER", "TOKENS", 
		"SEMI", "TOKEN_NAME", "TOKEN_REGEXP", "EQ"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public InputLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Input.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\fC\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\3\2\6\2\31\n\2\r\2\16\2\32\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\t\3\t\7"+
		"\t8\n\t\f\t\16\t;\13\t\3\n\6\n>\n\n\r\n\16\n?\3\13\3\13\2\2\f\3\3\5\4"+
		"\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\3\2\6\5\2\13\f\17\17\"\"\4\2C\\"+
		"c|\5\2\62;C\\c|\6\2,-\62;C\\c|\2E\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2"+
		"\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3"+
		"\2\2\2\2\25\3\2\2\2\3\30\3\2\2\2\5\36\3\2\2\2\7!\3\2\2\2\t#\3\2\2\2\13"+
		"%\3\2\2\2\r,\3\2\2\2\17\63\3\2\2\2\21\65\3\2\2\2\23=\3\2\2\2\25A\3\2\2"+
		"\2\27\31\t\2\2\2\30\27\3\2\2\2\31\32\3\2\2\2\32\30\3\2\2\2\32\33\3\2\2"+
		"\2\33\34\3\2\2\2\34\35\b\2\2\2\35\4\3\2\2\2\36\37\7j\2\2\37 \7j\2\2 \6"+
		"\3\2\2\2!\"\7}\2\2\"\b\3\2\2\2#$\7\177\2\2$\n\3\2\2\2%&\7j\2\2&\'\7g\2"+
		"\2\'(\7c\2\2()\7f\2\2)*\7g\2\2*+\7t\2\2+\f\3\2\2\2,-\7v\2\2-.\7q\2\2."+
		"/\7m\2\2/\60\7g\2\2\60\61\7p\2\2\61\62\7u\2\2\62\16\3\2\2\2\63\64\7=\2"+
		"\2\64\20\3\2\2\2\659\t\3\2\2\668\t\4\2\2\67\66\3\2\2\28;\3\2\2\29\67\3"+
		"\2\2\29:\3\2\2\2:\22\3\2\2\2;9\3\2\2\2<>\t\5\2\2=<\3\2\2\2>?\3\2\2\2?"+
		"=\3\2\2\2?@\3\2\2\2@\24\3\2\2\2AB\7?\2\2B\26\3\2\2\2\6\2\329?\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}