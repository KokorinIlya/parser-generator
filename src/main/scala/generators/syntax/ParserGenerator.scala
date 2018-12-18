package generators.syntax

import input._

object ParserGenerator {
  private def generateMethods(rulesHolder: RulesHolder, grammarName: String, first: Map[NonTerminal, Set[FirstEntry]],
                     follow: Map[NonTerminal, Set[FollowEntry]]) = {
    val methodsDescription = rulesHolder.rules.map { rule =>
      val params = rule.parameters.parameters.map { parameter =>
        s"${parameter.name}: ${parameter.paramType}"
      }.mkString(", ")

      val alternatives = rule.alternatives.alternatives
      val methodName = s"parse${rule.name}"

      val curFollow = follow(NonTerminal(rule.name))

      val alternativesDescription = alternatives.map { currentBody =>
        val entries = currentBody.entries.entries
        val firstEntry = entries.head
        firstEntry match {
          case EpsilonRuleBodyEntry() =>
            val followEntries = curFollow.map {
              case Dollar => s"${grammarName}Eof"
              case Terminal(name) => s"$grammarName$name(_)"
            }.mkString(" | ")

            val finalCode = currentBody.resultCode.code.getOrElse("")

            s"\tcase $followEntries => \n\t\t\t$finalCode\n\t\t\t${rule.result.name}"

          case OrdinaryRuleBodyEntry(_, assignment) =>
            val neededFirst= assignment match {
              case TokenAssignment(_, grammarEntryName) => Set(Terminal(grammarEntryName))
              case RuleAssignment(_, grammarEntryName, _) =>
                (first(NonTerminal(grammarEntryName)) - Epsilon).map(_.asInstanceOf[Terminal])
            }
            val tokensEnumeration = neededFirst.toList.map { token =>
              s"$grammarName${token.name}(_)"
            }.mkString(" | ")

            val actions = entries.map {
              case x@OrdinaryRuleBodyEntry(_, _) => x
            }.map { entry =>
              val assignmentString = entry.assignment match {
                case TokenAssignment(variableName, grammarEntryName) =>
                  val errorMessage = "case y => throw new runtime.ParseException(s\"In " + methodName + ", unexpected token $y\")"
                  s"""var $variableName = curToken match {
                     |  case x: $grammarName$grammarEntryName => x
                     |
                     |  $errorMessage
                     |}
                     |curToken = lexer.nextToken()
                     |""".stripMargin.split("\n").map(s => s"\t\t\t$s").mkString("\n")

                case RuleAssignment(variableName, grammarEntryName, arguments) =>
                  val argsString = arguments.arguments.mkString(", ")
                  s"\t\t\tvar $variableName = parse$grammarEntryName($argsString)"
              }

              val alignedCode = "\t\t" + entry.codeHolder.code.getOrElse("")

              alignedCode + "\n"  + assignmentString
            }

            val finalCode = actions.mkString("\n") + s"\n\t\t\t${currentBody.resultCode.code.getOrElse("")}"
            s"\tcase $tokensEnumeration => \n\t\t$finalCode\n\t\t\t${rule.result.name}"
        }
      }

      val cases = alternativesDescription.mkString("\n\n\t")

      val errorMessage = "case y => throw new runtime.ParseException(s\"In " + methodName + ", unexpected token $y\")"

      val methodText =
        s"""def $methodName($params): ${rule.result.paramType} = {
          |	var ${rule.result.name}: ${rule.result.paramType} = null
          |
          |	curToken match {
          |   $cases
          |
          |    $errorMessage
          |  }
          |}""".stripMargin.split("\n").map(s => s"\t$s").mkString("\n")

      methodText
    }
    methodsDescription.mkString("\n\n")
  }

  def generateParser(rulesHolder: RulesHolder, grammarName: String, first: Map[NonTerminal, Set[FirstEntry]],
                     follow: Map[NonTerminal, Set[FollowEntry]], header: Header): String = {
    val methods = generateMethods(rulesHolder, grammarName, first, follow)

    s"""${header.header}
      |
      |class ${grammarName}Parser(lexer: ${grammarName}Lexer) extends AutoCloseable {
      |  override def close(): Unit = {
      |    lexer.close()
      |  }
      |
      |  private var curToken = lexer.nextToken()
      |
      |$methods
      |}""".stripMargin
  }
}
