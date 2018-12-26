package generators

import java.nio.file.{Files, Path, Paths}

import generators.lexer.LexerGenerator
import generators.syntax._
import generators.tokens.{TokenGenerator, TokensInfo, TokensWriter}
import generators.writers.CodeWriter
import input._
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import parser.{InputLexer, InputParser}
import utils.IOUtils

class Generator(pathToGrammarFile: Path, pathToScalaDir: Path) {
  private def getGrammarName = {
    val filename = pathToGrammarFile.getFileName.toString
    if (filename.contains(".")) {
      val position = filename.lastIndexOf(".")
      filename.substring(0, position)
    } else {
      filename
    }
  }

  val grammarName: String = getGrammarName

  private def generateTokens(tokensHolder: TokensHolder, header: Header) = {
    val tokensInfo = TokenGenerator.createTokens(tokensHolder, grammarName)
    TokensWriter.writeTokens(pathToScalaDir.resolve(s"${grammarName}Tokens.scala"), tokensInfo, header)
    tokensInfo
  }

  private def generateLexer(tokensInfo: TokensInfo, skipTokensHolder: SkipTokensHolder, header: Header) {
    val lexerText = LexerGenerator.createLexer(tokensInfo, skipTokensHolder, grammarName, header)
    CodeWriter.writeCode(pathToScalaDir.resolve(s"${grammarName}Lexer.scala"), lexerText)
  }

  private def generateParser(rulesHolder: RulesHolder, startRule: NonTerminal, header: Header){
    /*
    Список правил вывода вида A -> entry1 entry2
     */
    val theoryRules = rulesHolder.rules.flatMap { rule =>
      rule.alternatives.alternatives.map { alternative =>
        val nterm = NonTerminal(rule.name)
        val rightSide = alternative.entries.entries.map {
          case OrdinaryRuleBodyEntry(_, assignment) =>
            assignment match {
              case TokenAssignment(_, grammarEntryName) => Terminal(grammarEntryName)
              case RuleAssignment(_, grammarEntryName, _) => NonTerminal(grammarEntryName)
            }
          case EpsilonRuleBodyEntry() => Epsilon
        }
        (nterm, rightSide)
      }
    }

    val first = ParserCalculator.calculateFirst(theoryRules)
    val follow = ParserCalculator.calculateFollow(theoryRules, startRule, first)

    val rulesForNterm: Map[NonTerminal, List[List[Entry]]] = theoryRules.groupBy { case (nterm, entries) =>
      nterm
    }.map { case (nterm, rules) =>
      val rulesWithoutNterm = rules.map { case (_, rule) =>
        rule
      }
      nterm -> rulesWithoutNterm
    }

    val isLL1 = GrammarChecker.checkLL1(rulesForNterm, first, follow)
    if (!isLL1) {
      throw new IllegalArgumentException("Grammar is not LL(1)")
    }

    val parserString = ParserGenerator.generateParser(rulesHolder, grammarName, first, follow, header)
    CodeWriter.writeCode(pathToScalaDir.resolve(s"${grammarName}Parser.scala"), parserString)
  }

  def generate() {
    IOUtils.using(Files.newInputStream(pathToGrammarFile)) { stream =>
      val charStream = CharStreams.fromStream(stream)
      val lexer = new InputLexer(charStream)
      val tokens = new CommonTokenStream(lexer)
      val parser = new InputParser(tokens)
      val description = parser.inputfile().desc

      Files.createDirectories(pathToScalaDir)
      val tokensInfo = generateTokens(description.tokensHolder, description.header)

      generateLexer(tokensInfo, description.skipTokensHolder, description.header)
      generateParser(description.rulesHolder, NonTerminal(description.startRuleName), description.header)
    }
  }
}

object Generator {
  def main(args: Array[String]): Unit = {
    val gen = new Generator(
      Paths.get("Input.txt"),
      Paths.get("src/main/scala/generated")
    )
    gen.generate()
  }
}
