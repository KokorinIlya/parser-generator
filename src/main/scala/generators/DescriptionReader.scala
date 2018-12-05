package generators

import java.nio.file.{Files, Path, Paths}

import input.FileDescription
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import parser.{InputLexer, InputParser}
import utils.IOUtils

class DescriptionReader(pathToGrammarFile: Path, pathToDir: Path) {
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
  val tokensName: String = grammarName + "Tokens"

  def getFileDescription(): FileDescription = {
    IOUtils.using(Files.newInputStream(pathToGrammarFile)) { stream =>
      val charStream = CharStreams.fromStream(stream)
      val lexer = new InputLexer(charStream)
      val tokens = new CommonTokenStream(lexer)
      val parser = new InputParser(tokens)
      parser.inputfile().desc
    }
  }
}

object DescriptionReader {
  def main(args: Array[String]): Unit = {
    println(new DescriptionReader(Paths.get("input.txt"), Paths.get("generated")).getFileDescription())
  }
}
