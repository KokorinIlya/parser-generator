package generators

import java.nio.file.{Files, Path, Paths}

import generators.tokens.TokenGenerator
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import parser.{InputLexer, InputParser}
import utils.IOUtils

class Generator(pathToGrammarFile: Path, pathToDir: Path) {
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

  def generate() = {
    IOUtils.using(Files.newInputStream(pathToGrammarFile)) { stream =>
      val charStream = CharStreams.fromStream(stream)
      val lexer = new InputLexer(charStream)
      val tokens = new CommonTokenStream(lexer)
      val parser = new InputParser(tokens)
      val description = parser.inputfile().desc
      val tokensInfo = TokenGenerator.createTokens(description.tokensHolder, grammarName)
      println(tokensInfo.mainTokenDescription)
      println(tokensInfo.tokensDescription)
      println(tokensInfo.regexps)
    }
  }
}

object Generator {
  def main(args: Array[String]): Unit = {
    new Generator(Paths.get("Input.txt"), Paths.get("src/java/generated")).generate()
  }
}
