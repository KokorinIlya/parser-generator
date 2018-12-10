package generators

import java.nio.file.{Files, Path, Paths}

import generators.tokens.{TokenGenerator, TokensWriter}
import input.{Header, TokensHolder}
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import parser.{InputLexer, InputParser}
import utils.IOUtils

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

  private def generateTokens(tokensHolder: TokensHolder, lexerHeader: Header){
    val tokensInfo = TokenGenerator.createTokens(tokensHolder, grammarName)
    TokensWriter.writeTokens(pathToScalaDir.resolve(s"${grammarName}Tokens.scala"), tokensInfo, lexerHeader)
  }

  def generate() = {
    IOUtils.using(Files.newInputStream(pathToGrammarFile)) { stream =>
      val charStream = CharStreams.fromStream(stream)
      val lexer = new InputLexer(charStream)
      val tokens = new CommonTokenStream(lexer)
      val parser = new InputParser(tokens)
      val description = parser.inputfile().desc

      Files.createDirectories(pathToScalaDir)
      generateTokens(description.tokensHolder, description.tokensHeader)
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