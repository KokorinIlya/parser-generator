package generators.tokens

import java.util.regex.Pattern

import input.TokensHolder.TokenName

case class TokensInfo(mainTokenName: String, mainTokenDescription: String, tokensDescription: List[String],
                      regexps: Map[TokenName, Pattern])
