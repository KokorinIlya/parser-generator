package generators.syntax

import input.{EpsilonRuleBodyEntry, OrdinaryRuleBodyEntry, RulesHolder}

object ParserGenerator {
  def generateParser(rulesHolder: RulesHolder, first: Map[NonTerminal, Set[FirstEntry]],
                     follow: Map[NonTerminal, Set[FollowEntry]]) = {
    val methodsDescription = rulesHolder.rules.map { rule =>
      val params = rule.parameters.parameters.map { parameter =>
        s"${parameter.name}: ${parameter.paramType}"
      }.mkString(", ")

      val alternatives = rule.alternatives.alternatives
      val methodName = s"parse${rule.name}"

      val errorMessage = "case y => throw new ParseException(s\"In " + methodName + ", unexpected token $y\")"



      val methodText =
        s"""def $methodName($params): ${rule.result.paramType} = {
          |	var ${rule.result.name}: ${rule.result.paramType} = null
          |
          |	curToken match {
          |
          |    $errorMessage
          |  }
          |}""".stripMargin

      methodText
    }
    println(methodsDescription)
  }
}
/*
def parseA(): Unit = {
	var u: Unit = null

	curToken match {

    case y => throw new ParseException(s"In $methodName, unexpected token $y")
  }
}
 */
