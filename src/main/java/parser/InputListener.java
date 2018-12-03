// Generated from src/main/java/parser/Input.g4 by ANTLR 4.7.1

package parser;
import input.*;
import utils.ScalaUtils;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link InputParser}.
 */
public interface InputListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link InputParser#inputfile}.
	 * @param ctx the parse tree
	 */
	void enterInputfile(InputParser.InputfileContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputParser#inputfile}.
	 * @param ctx the parse tree
	 */
	void exitInputfile(InputParser.InputfileContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputParser#tokenslist}.
	 * @param ctx the parse tree
	 */
	void enterTokenslist(InputParser.TokenslistContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputParser#tokenslist}.
	 * @param ctx the parse tree
	 */
	void exitTokenslist(InputParser.TokenslistContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputParser#token}.
	 * @param ctx the parse tree
	 */
	void enterToken(InputParser.TokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputParser#token}.
	 * @param ctx the parse tree
	 */
	void exitToken(InputParser.TokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputParser#javacode}.
	 * @param ctx the parse tree
	 */
	void enterJavacode(InputParser.JavacodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputParser#javacode}.
	 * @param ctx the parse tree
	 */
	void exitJavacode(InputParser.JavacodeContext ctx);
}