package generators.tokens

import java.nio.file.Path
import java.util.regex.Pattern

import input.{Header, TokensHolder}

object TokenGenerator {
  def createTokens(tokensHolder: TokensHolder, grammarName: String): TokensInfo = {
    val tokens = tokensHolder.tokens
    val mainTokenName = s"Abstract${grammarName}Token"
    val mainTokenDescription = s"sealed trait $mainTokenName extends runtime.TextHolder"
    val tokensDescriptions = for ((tokenName, _) <- tokens) yield {
      val tokenClassName = s"$grammarName$tokenName"
      s"case class $tokenClassName(override val text: String) extedns $mainTokenName"
    }
    val regexps = tokens.map {
      case (tokenName, tokenRegexp) => (tokenName, Pattern.compile(tokenRegexp))
    }.toMap
    TokensInfo(mainTokenName, mainTokenDescription, tokensDescriptions, regexps)
  }
}
