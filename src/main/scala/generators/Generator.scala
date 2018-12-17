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

  private def getFirst(ruleBody: List[Entry], first: Map[NonTerminal, Set[FirstEntry]]): Set[FirstEntry] = {
    ruleBody match {
      case Nil => Set(Epsilon)
      case head :: tail => head match {
        case Epsilon => Set(Epsilon)
        case term@Terminal(_) => Set(term)
        case nterm@NonTerminal(_) =>
          val firstSet = first.getOrElse(nterm, Set())
          (firstSet - Epsilon) ++ getFirst(tail, first)
      }
    }
  }

  def calculateFirst(rules: Map[NonTerminal, List[Entry]]): Map[NonTerminal, Set[FirstEntry]] = {
    /*
    first : нетерминалы -> подмножество терминальных символов и 'eps'
     */
    val first = mutable.Map[NonTerminal, Set[FirstEntry]]()
    var changed = true

    while (changed) {
      changed = false
      for ((nterm, ruleBody) <- rules) {
        val prevSet = first.getOrElse(nterm, Set())
        val delta = getFirst(ruleBody, first.toMap)
        if (!delta.subsetOf(prevSet)) {
          changed = true
        }
        val newSet = prevSet ++ delta
        first.update(nterm, newSet)
      }
    }
    first.toMap
  }

  def calculateFollow(rules: Map[NonTerminal, List[Entry]], startRule: NonTerminal,
                              first: Map[NonTerminal, Set[FirstEntry]]): Map[NonTerminal, Set[FollowEntry]] ={
    val follow = mutable.Map[NonTerminal, Set[FollowEntry]]()

    follow.update(startRule, Set(Dollar))
    var changed = true

    while (changed) {
      changed = false

      for {
        (nterm, rule) <- rules
        index <- rule.indices
        b = rule(index)
        if b.isInstanceOf[NonTerminal]
        gamma = rule.slice(index + 1, rule.size)
        // TODO: Empty
      } {
        val curNterm = b.asInstanceOf[NonTerminal]

        val curSet = follow.getOrElse(curNterm, Set())

        val firstSet = getFirst(gamma, first.toMap)
        val withoutEps = (firstSet - Epsilon).map(_.asInstanceOf[FollowEntry])
        if (!withoutEps.subsetOf(curSet)) {
          changed = true
        }
        val withDelta = curSet ++ withoutEps
        if (firstSet.contains(Epsilon)) {
          val aSet = follow.getOrElse(nterm, Set())
          if (!aSet.subsetOf(withDelta)) {
            changed = true
            follow.update(curNterm, withDelta ++ aSet)
          } else {
            follow.update(curNterm, withDelta)
          }
        } else {
          follow.update(curNterm, withDelta)
        }
      }
    }

    follow.toMap
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

    val first = calculateFirst(theoryRules)
    val follow = calculateFollow(theoryRules, startRule, first)
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
    val rules = Map(
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
  }
}
