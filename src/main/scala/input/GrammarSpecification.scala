package input

import input.TokensHolder.{TokenName, TokenRegexp}

case class TokensHolder(tokens: List[(TokenName, TokenRegexp)])

object TokensHolder {
  type TokenName = String
  type TokenRegexp = String
}

case class Header(header: String)

case class FileDescription(header: Header,
                           tokensHolder: TokensHolder,
                           skipTokensHolder: SkipTokensHolder,
                           rulesHolder: RulesHolder)

case class TokenHolder(name: TokenName, regexp: TokenRegexp)

case class SkipTokensHolder(tokens: List[TokenName])

case class SkipTokenHolder(token: TokenName)

sealed trait Assignment {
  val variableName: String
  val grammarEntryName: String
}

case class TokenAssignment(override val variableName: String, override val grammarEntryName: String) extends Assignment

case class RuleAssignment(override val variableName: String,
                          override val grammarEntryName: String,
                          arguments: ArgumentsHolder) extends Assignment

case class ArgumentsHolder(arguments: List[String])

case class CodeHolder(code: Option[String])

case class RuleBodyEntry(codeHolder: CodeHolder, assignment: Assignment)

case class BodyEntries(entries: List[RuleBodyEntry])

case class RuleBody(entries: BodyEntries, resultCode: CodeHolder)

case class Parameter(name: String, paramType: String)

case class AlternativesHolder(alternatives: List[RuleBody])

case class Rule(alternatives: AlternativesHolder, parameters: ParametersHolder, result: Parameter)

case class ParametersHolder(parameters: List[Parameter])

case class RulesHolder(rules: List[Rule])


