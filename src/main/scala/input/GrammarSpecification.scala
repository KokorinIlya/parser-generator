package input

import input.TokensHolder.{TokenName, TokenRegexp}

sealed trait GrammarSpecification

case class TokensHolder(tokens: List[(TokenName, TokenRegexp)]) extends GrammarSpecification

object TokensHolder {
  type TokenName = String
  type TokenRegexp = String
}

case class Header(header: String) extends GrammarSpecification

case class FileDescription(header: Header,
                           tokensHolder: TokensHolder,
                           skipTokensHolder: SkipTokensHolder) extends GrammarSpecification

case class TokenHolder(name: TokenName, regexp: TokenRegexp) extends GrammarSpecification

case class SkipTokensHolder(tokens: List[TokenName]) extends GrammarSpecification

case class SkipTokenHolder(token: TokenName) extends GrammarSpecification
