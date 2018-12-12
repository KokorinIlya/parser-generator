package generators.lexer

import java.nio.file.{Files, Path, StandardOpenOption}

import utils.IOUtils

object LexerWriter {
  def writeLexer(pathToFileWithLexer: Path, lexerCode: String) = {
    IOUtils.using(Files.newBufferedWriter(pathToFileWithLexer, StandardOpenOption.CREATE)) { writer =>
      writer.write(lexerCode)
      writer.newLine()
      writer.close()
    }
  }
}
