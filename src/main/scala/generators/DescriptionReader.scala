package generators

import java.nio.file.{Files, Path, Paths}

import input.FileDescription
import org.antlr.v4.runtime.{ANTLRInputStream, CharStreams, CommonTokenStream}
import parser.{InputLexer, InputParser}
import utils.IOUtils

class DescriptionReader(path: Path) {
  def getFileDescription(): FileDescription = {
    IOUtils.using(Files.newInputStream(path)) { stream =>
      val charStream = new ANTLRInputStream(stream)
      val lexer = new InputLexer(charStream)
      val tokens = new CommonTokenStream(lexer)
      val parser = new InputParser(tokens)
      parser.inputfile().desc
    }
  }
}

object DescriptionReader {
  def main(args: Array[String]): Unit = {
    println(new DescriptionReader(Paths.get("input.txt")).getFileDescription())
  }
}
