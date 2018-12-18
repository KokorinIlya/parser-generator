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
                           startRuleName: String,
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

/*
Список параметров вида (name1, name2)
 */
case class ArgumentsHolder(arguments: List[String])

/*
Содержит либо код, либо ничего
 */
case class CodeHolder(code: Option[String])

sealed trait RuleBodyEntry

/*
Обыкновенное правило имеет вид
{code} a = rule_name(params)
 */
case class OrdinaryRuleBodyEntry(codeHolder: CodeHolder,
                                 assignment: Assignment) extends RuleBodyEntry

/*
Эпсилон-правило, имеет вид eps,
может быть только единственным в списке BodyEntries.
 */
case class EpsilonRuleBodyEntry() extends RuleBodyEntry

/*
Список entry.
 */
case class BodyEntries(entries: List[RuleBodyEntry])

/*
ResultBody имеет вид
entry1, entry2 {result}
 */
case class RuleBody(entries: BodyEntries, resultCode: CodeHolder)

/*
name : type
 */
case class Parameter(name: String, paramType: String)

/*
alternatives - лист альтернатив для текущего правила вида

a1 | a2 | a3
 */
case class AlternativesHolder(alternatives: List[RuleBody])

/*
alternatives - лист альтернатив для текущего правила вида

a1 | a2 | a3

parameters - список параметров вида (x1: T1, x2: T2)

result - один параметр вида res: T
 */
case class Rule(name: String, alternatives: AlternativesHolder, parameters: ParametersHolder, result: Parameter)

/*
список параметров вида (x1: T1, x2: T2)
 */
case class ParametersHolder(parameters: List[Parameter])

/*
rules - Все правила вида

A ::= ...;;

B ::= ...;;
 */
case class RulesHolder(rules: List[Rule])


