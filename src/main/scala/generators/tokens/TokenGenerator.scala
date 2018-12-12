package generators.tokens

import java.util.regex.Pattern

import input.TokensHolder

object TokenGenerator {
  def createTokens(tokensHolder: TokensHolder, grammarName: String): TokensInfo = {
    val mainTokenName = s"Abstract${grammarName}Token"
    val eofTokenName = s"${grammarName}Eof"
    val eofTokenDescription = s"case object $eofTokenName extends $mainTokenName {\n" +
      "\toverride val text: String = \"EOF\"\n" +
    "}"
    val tokens = tokensHolder.tokens
    val mainTokenDescription = s"sealed trait $mainTokenName extends runtime.TextHolder"
    val tokensDescriptions = for ((tokenName, _) <- tokens) yield {
      val tokenClassName = s"$grammarName$tokenName"
      s"case class $tokenClassName(override val text: String) extends $mainTokenName"
    }
    val regexps = tokens.map {
      case (tokenName, tokenRegexp) => (tokenName, Pattern.compile(tokenRegexp))
    }
    TokensInfo(mainTokenName, mainTokenDescription, eofTokenDescription :: tokensDescriptions, regexps, eofTokenName)
  }
}
