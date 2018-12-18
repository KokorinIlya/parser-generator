package generators.syntax

import input._

object ParserGenerator {
  def generateParser(rulesHolder: RulesHolder, grammarName: String, first: Map[NonTerminal, Set[FirstEntry]],
                     follow: Map[NonTerminal, Set[FollowEntry]]) = {
    println(first)
    println(follow)
    val methodsDescription = rulesHolder.rules.map { rule =>
      val params = rule.parameters.parameters.map { parameter =>
        s"${parameter.name}: ${parameter.paramType}"
      }.mkString(", ")

      val alternatives = rule.alternatives.alternatives
      val methodName = s"parse${rule.name}"

      val curFirst = first(NonTerminal(rule.name))
      val curFollow = follow(NonTerminal(rule.name))

      val alternativesDescription = alternatives.map { currentBody =>
        val firstEntry = currentBody.entries.entries.head
        val startString = firstEntry match {
          case EpsilonRuleBodyEntry() =>
            val followEntries = curFollow.map {
              case Dollar => s"${grammarName}Eof"
              case Terminal(name) => s"$grammarName$name"
            }.mkString(" | ")

            val finalCode = currentBody.resultCode.code.getOrElse("")

            s"case $followEntries => $finalCode"

          case OrdinaryRuleBodyEntry(codeHolder, assignment) =>
            val neededFirst= assignment match {
              case TokenAssignment(_, grammarEntryName) => Set(Terminal(grammarEntryName))
              case RuleAssignment(_, grammarEntryName, _) =>
                (first(NonTerminal(grammarEntryName)) - Epsilon).map(_.asInstanceOf[Terminal])
            }
            val tokensEnumeration = neededFirst.toList.map { token =>
              s"$grammarName${token.name}"
            }.mkString(" | ")
            s"case $tokensEnumeration => ???"
        }

        startString
      }

      val cases = alternativesDescription.mkString("\n\n\t")

      val errorMessage = "case y => throw new ParseException(s\"In " + methodName + ", unexpected token $y\")"

      val methodText =
        s"""def $methodName($params): ${rule.result.paramType} = {
          |	var ${rule.result.name}: ${rule.result.paramType} = null
          |
          |	curToken match {
          |    $cases
          |
          |    $errorMessage
          |  }
          |}""".stripMargin

      methodText
    }
    println(methodsDescription)
  }
}
