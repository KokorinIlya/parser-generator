package generators

import java.nio.file.{Files, Path, Paths}

import generators.lexer.{LexerGenerator, LexerWriter}
import generators.syntax._
import generators.tokens.{TokenGenerator, TokensInfo, TokensWriter}
import input._
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import parser.{InputLexer, InputParser}
import utils.IOUtils

import scala.collection.mutable

class Generator(pathToGrammarFile: Path, pathToJavaDir: Path, pathToScalaDir: Path) {
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
    LexerWriter.writeLexer(pathToScalaDir.resolve(s"${grammarName}Lexer.scala"), lexerText)
  }

  private def calculateFirst(rules: Map[NonTerminal, List[Entry]]) = {
    /*
    first : нетерминалы -> подмножество терминальных символов и 'eps'
     */
    val first = mutable.Map[NonTerminal, Set[FirstEntry]]()

    def getFirst(ruleBody: List[Entry]): Set[FirstEntry] = {
      ruleBody match {
        case Nil => Set(Epsilon)
        case head :: tail => head match {
          case Epsilon => Set(Epsilon)
          case term@Terminal(_) => Set(term)
          case nterm@NonTerminal(_) =>
            val firstSet = first.getOrElse(nterm, Set())
            (firstSet - Epsilon) ++ getFirst(tail)
        }
      }
    }

    var changed = true

    while (changed) {
      changed = false
      for ((nterm, ruleBody) <- rules) {
        val prevSet = first.getOrElse(nterm, Set())
        val newSet = prevSet ++ getFirst(ruleBody)
        if (newSet != prevSet) {
          changed = true
          first.update(nterm, newSet)
        }
      }
    }
    first.toMap
  }

  private def generateParser(rulesHolder: RulesHolder) = {
    /*
    Список правил вывода вида A -> entry1 entry2
     */
    val theoryRules = rulesHolder.rules.flatMap { rule =>
      rule.alternatives.alternatives.map { alternative =>
        val nterm = NonTerminal(rule.name)
        val rightSide = alternative.entries.entries.map {
          case OrdinaryRuleBodyEntry(codeHolder, assignment) =>
            assignment match {
              case TokenAssignment(variableName, grammarEntryName) => Terminal(grammarEntryName)
              case RuleAssignment(variableName, grammarEntryName, arguments) => NonTerminal(grammarEntryName)
            }
          case EpsilonRuleBodyEntry(codeHolder) => Epsilon
        }
        (nterm, rightSide)
      }
    }.toMap

    val first = calculateFirst(theoryRules)
  }

  def generate() = {
    IOUtils.using(Files.newInputStream(pathToGrammarFile)) { stream =>
      val charStream = CharStreams.fromStream(stream)
      val lexer = new InputLexer(charStream)
      val tokens = new CommonTokenStream(lexer)
      val parser = new InputParser(tokens)
      val description = parser.inputfile().desc

      Files.createDirectories(pathToScalaDir)
      val tokensInfo = generateTokens(description.tokensHolder, description.header)

      generateLexer(tokensInfo, description.skipTokensHolder, description.header)
      generateParser(description.rulesHolder)
    }
  }
}

object Generator {
  def main(args: Array[String]): Unit = {
    new Generator(
      Paths.get("Input.txt"),
      Paths.get("src/main/java/generated"),
      Paths.get("src/main/scala/generated")
    ).generate()
  }
}
