package generators.lexer

import generators.tokens.TokensInfo
import input.SkipTokensHolder

object LexerGenerator {
  def createLexer(tokensInfo: TokensInfo, skipTokensHolder: SkipTokensHolder, grammarName: String) = {
    val lexerName = s"${grammarName}Lexer"
  }
}

/*package generated

import java.io.InputStream
import java.util.regex.Pattern

class Lexer(override val inputStream: InputStream,
            override protected val nameToRegex: List[(String, Pattern)],
            override protected val skip: Set[String]) extends runtime.AbstractLexer[AbstractInputToken] {
  override protected def createEof: AbstractInputToken = InputEof

  override protected def nameToToken(name: String, content: String): AbstractInputToken = {
    name match {
      case "InputA" => InputA(content)
      case "InputB" => InputB(content)
      case "InputC" => InputC(content)
      case x => throw new runtime.LexingException(s"Unknown token $x")
    }
  }
}*/
