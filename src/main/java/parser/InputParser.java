// Generated from src/main/java/parser/Input.g4 by ANTLR 4.7.1

package parser;
import input.*;
import utils.ScalaUtils;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class InputParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, JAVA=2, LEFT_BRACKET=3, RIGHT_BRACKET=4, HEADER=5, TOKENS=6, SEMI=7, 
		TOKEN_NAME=8, TOKEN_REGEXP=9, EQ=10;
	public static final int
		RULE_inputfile = 0, RULE_tokenslist = 1, RULE_token = 2, RULE_javacode = 3;
	public static final String[] ruleNames = {
		"inputfile", "tokenslist", "token", "javacode"
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

	@Override
	public String getGrammarFileName() { return "Input.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public InputParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class InputfileContext extends ParserRuleContext {
		public FileDescription desc;
		public JavacodeContext headercode;
		public TokenslistContext tokensfromfile;
		public TerminalNode HEADER() { return getToken(InputParser.HEADER, 0); }
		public List<TerminalNode> LEFT_BRACKET() { return getTokens(InputParser.LEFT_BRACKET); }
		public TerminalNode LEFT_BRACKET(int i) {
			return getToken(InputParser.LEFT_BRACKET, i);
		}
		public List<TerminalNode> RIGHT_BRACKET() { return getTokens(InputParser.RIGHT_BRACKET); }
		public TerminalNode RIGHT_BRACKET(int i) {
			return getToken(InputParser.RIGHT_BRACKET, i);
		}
		public TerminalNode TOKENS() { return getToken(InputParser.TOKENS, 0); }
		public JavacodeContext javacode() {
			return getRuleContext(JavacodeContext.class,0);
		}
		public TokenslistContext tokenslist() {
			return getRuleContext(TokenslistContext.class,0);
		}
		public InputfileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inputfile; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof InputListener ) ((InputListener)listener).enterInputfile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof InputListener ) ((InputListener)listener).exitInputfile(this);
		}
	}

	public final InputfileContext inputfile() throws RecognitionException {
		InputfileContext _localctx = new InputfileContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_inputfile);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(8);
			match(HEADER);
			setState(9);
			match(LEFT_BRACKET);
			setState(10);
			((InputfileContext)_localctx).headercode = javacode();
			setState(11);
			match(RIGHT_BRACKET);
			setState(12);
			match(TOKENS);
			setState(13);
			match(LEFT_BRACKET);
			setState(14);
			((InputfileContext)_localctx).tokensfromfile = tokenslist();
			setState(15);
			match(RIGHT_BRACKET);

			        ((InputfileContext)_localctx).desc =  new FileDescription(new Header(((InputfileContext)_localctx).headercode.code), ((InputfileContext)_localctx).tokensfromfile.holder);
			    
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TokenslistContext extends ParserRuleContext {
		public TokensHolder holder;
		public TokenContext currenttoken;
		public TokenslistContext tokenslisttail;
		public TerminalNode SEMI() { return getToken(InputParser.SEMI, 0); }
		public TokenContext token() {
			return getRuleContext(TokenContext.class,0);
		}
		public TokenslistContext tokenslist() {
			return getRuleContext(TokenslistContext.class,0);
		}
		public TokenslistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tokenslist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof InputListener ) ((InputListener)listener).enterTokenslist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof InputListener ) ((InputListener)listener).exitTokenslist(this);
		}
	}

	public final TokenslistContext tokenslist() throws RecognitionException {
		TokenslistContext _localctx = new TokenslistContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_tokenslist);
		try {
			setState(27);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(18);
				((TokenslistContext)_localctx).currenttoken = token();
				setState(19);
				match(SEMI);
				setState(20);
				((TokenslistContext)_localctx).tokenslisttail = tokenslist();

				        ((TokenslistContext)_localctx).holder =  new TokensHolder(
				            ScalaUtils.appendToList(
				                ((TokenslistContext)_localctx).tokenslisttail.holder.tokens(),
				                ((TokenslistContext)_localctx).currenttoken.holder.name(),
				                ((TokenslistContext)_localctx).currenttoken.holder.regexp()
				            )
				        );
				    
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(23);
				((TokenslistContext)_localctx).currenttoken = token();

				        ((TokenslistContext)_localctx).holder =  new TokensHolder(
				            ScalaUtils.singleElementList(
				                ((TokenslistContext)_localctx).currenttoken.holder.name(),
				                ((TokenslistContext)_localctx).currenttoken.holder.regexp()
				            )
				        );
				    
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{

				        ((TokenslistContext)_localctx).holder =  new TokensHolder(
				            ScalaUtils.emptyList()
				        );
				    
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TokenContext extends ParserRuleContext {
		public TokenHolder holder;
		public Token TOKEN_NAME;
		public Token TOKEN_REGEXP;
		public TerminalNode TOKEN_NAME() { return getToken(InputParser.TOKEN_NAME, 0); }
		public TerminalNode EQ() { return getToken(InputParser.EQ, 0); }
		public TerminalNode TOKEN_REGEXP() { return getToken(InputParser.TOKEN_REGEXP, 0); }
		public TokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_token; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof InputListener ) ((InputListener)listener).enterToken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof InputListener ) ((InputListener)listener).exitToken(this);
		}
	}

	public final TokenContext token() throws RecognitionException {
		TokenContext _localctx = new TokenContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_token);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			((TokenContext)_localctx).TOKEN_NAME = match(TOKEN_NAME);
			setState(30);
			match(EQ);
			setState(31);
			((TokenContext)_localctx).TOKEN_REGEXP = match(TOKEN_REGEXP);

			        ((TokenContext)_localctx).holder =  new TokenHolder((((TokenContext)_localctx).TOKEN_NAME!=null?((TokenContext)_localctx).TOKEN_NAME.getText():null), (((TokenContext)_localctx).TOKEN_REGEXP!=null?((TokenContext)_localctx).TOKEN_REGEXP.getText():null));
			    
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JavacodeContext extends ParserRuleContext {
		public String code;
		public Token JAVA;
		public TerminalNode JAVA() { return getToken(InputParser.JAVA, 0); }
		public JavacodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_javacode; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof InputListener ) ((InputListener)listener).enterJavacode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof InputListener ) ((InputListener)listener).exitJavacode(this);
		}
	}

	public final JavacodeContext javacode() throws RecognitionException {
		JavacodeContext _localctx = new JavacodeContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_javacode);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(34);
			((JavacodeContext)_localctx).JAVA = match(JAVA);

			        ((JavacodeContext)_localctx).code =  (((JavacodeContext)_localctx).JAVA!=null?((JavacodeContext)_localctx).JAVA.getText():null);
			    
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\f(\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\5\3\36\n\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3"+
		"\5\2\2\6\2\4\6\b\2\2\2%\2\n\3\2\2\2\4\35\3\2\2\2\6\37\3\2\2\2\b$\3\2\2"+
		"\2\n\13\7\7\2\2\13\f\7\5\2\2\f\r\5\b\5\2\r\16\7\6\2\2\16\17\7\b\2\2\17"+
		"\20\7\5\2\2\20\21\5\4\3\2\21\22\7\6\2\2\22\23\b\2\1\2\23\3\3\2\2\2\24"+
		"\25\5\6\4\2\25\26\7\t\2\2\26\27\5\4\3\2\27\30\b\3\1\2\30\36\3\2\2\2\31"+
		"\32\5\6\4\2\32\33\b\3\1\2\33\36\3\2\2\2\34\36\b\3\1\2\35\24\3\2\2\2\35"+
		"\31\3\2\2\2\35\34\3\2\2\2\36\5\3\2\2\2\37 \7\n\2\2 !\7\f\2\2!\"\7\13\2"+
		"\2\"#\b\4\1\2#\7\3\2\2\2$%\7\4\2\2%&\b\5\1\2&\t\3\2\2\2\3\35";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}