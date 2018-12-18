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

  private def getFirst(rule: List[Entry], curFirst: Map[NonTerminal, Set[FirstEntry]]): Set[FirstEntry] = {
    rule match {
      case Nil => Set(Epsilon)

      case Epsilon :: Nil => Set(Epsilon)

      case (term@Terminal(_)) :: _ => Set(term)

      case (nterm@NonTerminal(_)) :: tail =>
        val firstSet = curFirst.getOrElse(nterm, Set())
        if (firstSet.contains(Epsilon)) {
          // TODO: Remove epsilon?
          val tailFirst = getFirst(tail, curFirst)
          firstSet ++ tailFirst
        } else {
          firstSet
        }
    }
  }

  def calculateFirst(rules: List[(NonTerminal, List[Entry])]): Map[NonTerminal, Set[FirstEntry]] = {
    val first = mutable.Map[NonTerminal, Set[FirstEntry]]()

    var changed = true

    while (changed) {
      changed = false
      for {(nterm, rule) <- rules} {
        val prevSet = first.getOrElse(nterm, Set())
        val delta = getFirst(rule, first.toMap)
        if (!delta.subsetOf(prevSet)) {
          changed = true
          first.update(nterm, prevSet ++ delta)
        }
      }
    }
    first.toMap
  }

  def calculateFollow(rules: List[(NonTerminal, List[Entry])], start: NonTerminal,
                      first: Map[NonTerminal, Set[FirstEntry]]): Map[NonTerminal, Set[FollowEntry]] = {
    val answer = mutable.Map[NonTerminal, Set[FollowEntry]]()
    answer.update(start, Set(Dollar))

    var changed = true
    while (changed) {
      changed = false
      for {
        (nterm, rule) <- rules
        (entry, index) <- rule.zipWithIndex
        if entry.isInstanceOf[NonTerminal]
        curNterm = entry.asInstanceOf[NonTerminal]
        curFollow = answer.getOrElse(curNterm, Set())
        ruleReminder = rule.slice(index + 1, rule.size)
        curFirst = getFirst(ruleReminder, first)
      } {
        val prev = answer.getOrElse(curNterm, Set())
        for {firstEntry <- curFirst} {
          firstEntry match {
            case Epsilon =>
            case term@Terminal(name) =>
              val curFollow = answer.getOrElse(curNterm, Set())
              answer.update(curNterm, curFollow + term)
          }
        }
        if (curFirst.contains(Epsilon)) {
          val followForNterm = answer.getOrElse(nterm, Set())
          val followForCurNterm = answer.getOrElse(curNterm, Set())
          answer.update(curNterm, followForCurNterm ++ followForNterm)
        }
        if (answer.getOrElse(curNterm, Set()) != prev) {
          changed = true
        }
      }
    }
    answer.toMap
  }

  private def generateParser(rulesHolder: RulesHolder, startRule: NonTerminal) = {
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
      generateParser(description.rulesHolder, NonTerminal(description.startRuleName))
    }
  }
}

object Generator {
  def main(args: Array[String]): Unit = {
    val gen = new Generator(
      Paths.get("Input.txt"),
      Paths.get("src/main/java/generated"),
      Paths.get("src/main/scala/generated")
    )
    val rules = List(
      NonTerminal("S") -> List(
        Terminal("a"),
        Terminal("b"),
        NonTerminal("A")
      ),
      NonTerminal("A") -> List(
        Terminal("b"),
        Terminal("c")
      ),
      NonTerminal("A") -> List(
        Epsilon
      )
    )
    val first = gen.calculateFirst(rules)
    println(first)
    val follow = gen.calculateFollow(rules, NonTerminal("S"), first)
    println(follow)
  }
}
